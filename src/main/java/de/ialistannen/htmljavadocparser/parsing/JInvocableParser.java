package de.ialistannen.htmljavadocparser.parsing;

import static de.ialistannen.htmljavadocparser.util.LinkUtils.linkToFqn;
import static de.ialistannen.htmljavadocparser.util.LinkUtils.urlFragment;

import de.ialistannen.htmljavadocparser.model.generic.GenericType;
import de.ialistannen.htmljavadocparser.model.properties.Deprecatable.DeprecationStatus;
import de.ialistannen.htmljavadocparser.model.properties.HasVisibility.VisibilityLevel;
import de.ialistannen.htmljavadocparser.model.properties.Overridable.ControlModifier;
import de.ialistannen.htmljavadocparser.resolving.DocumentResolver;
import de.ialistannen.htmljavadocparser.resolving.Index;
import de.ialistannen.htmljavadocparser.util.StringUtils;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

public class JInvocableParser {

  private String url;
  private final DocumentResolver resolver;

  public JInvocableParser(String url, DocumentResolver resolver) {
    this.url = url;
    this.resolver = resolver;
  }

  protected String url() {
    return url;
  }

  protected DocumentResolver resolver() {
    return resolver;
  }

  private Element element() {
    Document document = resolver().resolve(url);

    return document.getElementById(URLDecoder.decode(urlFragment(url), StandardCharsets.UTF_8))
        .nextElementSibling();
  }

  private Element summaryLink() {
    Document document = resolver().resolve(url);
    return document.getElementsByAttributeValueEnding("href", "#" + urlFragment(url))
        .stream()
        .filter(element -> element.parent().hasClass("memberNameLink"))
        .findFirst()
        .orElseThrow();
  }

  public String parseDeclaration() {
    return StringUtils.normalizeWhitespace(element().getElementsByTag("pre").first().text());
  }

  public VisibilityLevel parseVisibilityLevel() {
    if (isInInterface()) {
      return VisibilityLevel.PUBLIC;
    }
    return ParserHelper.parseVisibilityLevel(parseDeclaration());
  }

  private boolean isInInterface() {
    return element().ownerDocument().getElementsByClass("title").first().ownText()
        .startsWith("Interface");
  }

  public String parseSimpleName() {
    return summaryLink().text();
  }

  public String parseReturnType() {
    Element row = summaryLink();
    while (!row.tagName().equals("tr")) {
      row = row.parent();
    }
    return ParserHelper.parseReturnTypeFromMemberSummaryRow(row, parseActualOwner());
  }

  public Map<String, String> parseParameters() {
    Map<String, String> map = new LinkedHashMap<>();

    // Layout is: code > span > link
    Element codeSummary = summaryLink().parent().parent();

    String fullLink = codeSummary.text();

    // no params
    if (fullLink.endsWith("()")) {
      return Collections.emptyMap();
    }

    String parameters = fullLink.replaceAll(".+\\((.+)\\).*", "$1");
    for (String parameter : parameters.split(", ")) {
      String[] parts = parameter.split(" ");
      String type = parts[0].replaceAll("<.+", ""); // remove generics
      String name = parts[1].replaceAll("<.+", ""); // remove generics

      Element linkElement = codeSummary.getElementsMatchingText(Pattern.quote(type)).last();

      if (linkElement != null && linkElement.tagName().equals("a")) {
        type = linkToFqn(linkElement.attr("href"));
      }

      map.put(name, type);
    }

    return map;
  }

  public DeprecationStatus parseDeprecationStatus() {
    return element().select(".deprecationBlock > span").isEmpty()
        ? DeprecationStatus.NOT_DEPRECATED
        : DeprecationStatus.DEPRECATED;
  }

  public String parsePackage() {
    return new JTypeParser(url, resolver()).parsePackage();
  }

  public String parseActualOwner() {
    return parsePackage() + "." + new JTypeParser(url, resolver()).parseSimpleName();
  }

  public String parseOriginalOwner() {
    for (Element overrideSpecifyLabel : element().getElementsByClass("overrideSpecifyLabel")) {
      // dl > dt > span
      // dl > dd > code > a
      Elements links = overrideSpecifyLabel.parent().parent().getElementsByTag("a");
      for (Element link : links) {
        String href = link.attr("href");
        if (href.contains("#" + parseSimpleName())) {
          return linkToFqn(href.substring(0, href.lastIndexOf('#')));
        }
      }
    }
    return null;
  }

  public Collection<ControlModifier> parseOverrideModifier() {
    Collection<ControlModifier> modifiers = EnumSet.noneOf(ControlModifier.class);
    String declaration = parseDeclaration();
    for (ControlModifier modifier : ControlModifier.values()) {
      if (declaration.contains(modifier.getName())) {
        modifiers.add(modifier);
      }
    }
    return modifiers;
  }

  public List<String> parseAnnotations() {
    return element().getElementsByTag("a").stream()
        .filter(element -> element.attr("title").contains("annotation in"))
        .map(element -> linkToFqn(element.attr("href")))
        .collect(Collectors.toList());
  }

  public Collection<String> parseThrows() {
    return element().getElementsByTag("a").stream()
        .filter(this::hasPreviousThrowsTag)
        .map(element -> linkToFqn(element.attr("href")))
        .collect(Collectors.toSet());
  }

  private boolean hasPreviousThrowsTag(Element link) {
    Node previous = link;
    while ((previous = previous.previousSibling()) != null) {
      if (previous instanceof TextNode && ((TextNode) previous).text().contains("throws")) {
        return true;
      }
    }

    return false;
  }

  public boolean parseIsStatic() {
    return ParserHelper.parseIsStatic(parseDeclaration());
  }

  public List<GenericType> parseGenericTypes(Index index) {
    String declaration = parseDeclaration();
    String returnType = parseReturnType(); // is a raw type without generics

    int returnTypeStartIndex = -1;
    for (String part : declaration.split("[ (]")) {
      String cleanedPart = part.replaceAll("<.+>", "");
      if (!cleanedPart.trim().isEmpty() && returnType.endsWith(cleanedPart)) {
        returnTypeStartIndex = declaration.indexOf(part);
        break;
      }
    }

    if (returnTypeStartIndex < 0) {
      return Collections.emptyList();
    }

    String partBeforeReturn = declaration.substring(0, returnTypeStartIndex).trim();

    String generics = partBeforeReturn.replaceAll(".*?(<.+>).*?", "$1");

    return ParserHelper.parseGenericTypes(
        index,
        element(),
        generics,
        parseSimpleName(),
        parsePackage() + "." + parseSimpleName()
    );
  }

  public List<Element> parseJavadoc() {
    return element().select("li > div, dl");
  }
}
