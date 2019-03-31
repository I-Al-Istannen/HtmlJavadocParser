package de.ialistannen.htmljavadocparser.parsing;

import static de.ialistannen.htmljavadocparser.util.LinkUtils.linkToFqn;

import de.ialistannen.htmljavadocparser.model.properties.Deprecatable.DeprecationStatus;
import de.ialistannen.htmljavadocparser.model.properties.HasVisibility.VisibilityLevel;
import de.ialistannen.htmljavadocparser.model.properties.Overridable.ControlModifier;
import de.ialistannen.htmljavadocparser.resolving.DocumentResolver;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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

  private Element element() {
    Document document = resolver.resolve(url);

    return document.getElementById(URLDecoder.decode(urlFragment(), StandardCharsets.UTF_8))
        .nextElementSibling();
  }

  private String urlFragment() {
    return url.substring(url.lastIndexOf('#') + 1);
  }

  private Element summaryLink() {
    Document document = resolver.resolve(url);
    Element href = document.getElementsByAttributeValueEnding("href", "#" + urlFragment())
        .first();
    return href;
  }

  public String parseDeclaration() {
    return element().getElementsByTag("pre").first().text()
        .replaceAll("\\s+", " ");
  }

  public VisibilityLevel parseVisibilityLevel() {
    String firstModifier = parseDeclaration().split(" ")[0];
    try {
      return VisibilityLevel.valueOf(firstModifier.toUpperCase());
    } catch (IllegalArgumentException e) {
      return VisibilityLevel.PACKAGE_PRIVATE;
    }
  }

  public String parseSimpleName() {
    return summaryLink().text();
  }

  public String parseReturnType() {
    Element row = summaryLink();
    while (!row.tagName().equals("tr")) {
      row = row.parent();
    }

    Element returnTypeTd = row.getElementsByClass("colFirst").first();
    Element returnTypeLink = returnTypeTd.getElementsByTag("a").first();

    // no link, so it was a primitive type
    if (returnTypeLink == null) {
      return returnTypeTd.text();
    }

    return linkToFqn(returnTypeLink.attr("href"));
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
      String type = parts[0];
      String name = parts[1];

      Element linkElement = codeSummary.getElementsMatchingText(removeArraySyntax(type)).last();
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
    return new JTypeParser(url, resolver).parsePackage();
  }

  public String parseActualOwner() {
    return parsePackage() + "." + new JTypeParser(url, resolver).parseSimpleName();
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

  public ControlModifier parseOverrideModifier() {
    String declaration = parseDeclaration();
    for (ControlModifier modifier : ControlModifier.values()) {
      if (declaration.contains(modifier.getName())) {
        return modifier;
      }
    }
    return ControlModifier.NONE;
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

  private String removeArraySyntax(String string) {
    return string.replace("...", "")
        .replace("[]", "");
  }
}
