package de.ialistannen.htmljavadocparser.parsing;

import de.ialistannen.htmljavadocparser.model.properties.HasVisibility.VisibilityLevel;
import de.ialistannen.htmljavadocparser.model.properties.Overridable.ControlModifier;
import de.ialistannen.htmljavadocparser.resolving.DocumentResolver;
import de.ialistannen.htmljavadocparser.util.LinkUtils;
import de.ialistannen.htmljavadocparser.util.StringUtils;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class JFieldParser {

  private DocumentResolver resolver;
  private String url;

  public JFieldParser(DocumentResolver documentResolver, String url) {
    this.resolver = documentResolver;
    this.url = url;
  }

  private Element element() {
    Document document = resolver.resolve(url);
    return document.getElementById(LinkUtils.urlFragment(url)).nextElementSibling();
  }

  public String parseOwner() {
    return new JInvocableParser(url, resolver).parseActualOwner();
  }

  public String parsePackage() {
    return new JTypeParser(url, resolver).parsePackage();
  }

  public String parseSimpleName() {
    return LinkUtils.urlFragment(url);
  }

  public String parseDeclaration() {
    Elements preElement = element().select("li > pre");
    return StringUtils.normalizeWhitespace(preElement.text());
  }

  public VisibilityLevel parseVisibilityLevel() {
    if (isInInterface()) {
      return VisibilityLevel.PUBLIC;
    }
    return ParserHelper.parseVisibilityLevel(parseDeclaration());
  }

  public Collection<ControlModifier> parseControlModifiers() {
    String declaration = parseDeclaration();

    Collection<ControlModifier> modifiers = Arrays.stream(ControlModifier.values())
        .filter(controlModifier -> declaration.contains(controlModifier.getName()))
        .collect(Collectors.toCollection(HashSet::new));

    if (isInInterface()) {
      modifiers.add(ControlModifier.FINAL);
    }

    return modifiers;
  }

  public boolean parseIsStatic() {
    return ParserHelper.parseIsStatic(parseDeclaration());
  }

  public String parseType() {
    Element href = element().ownerDocument()
        .getElementsByAttributeValueEnding("href", "#" + LinkUtils.urlFragment(url))
        .first();

    Element row = href;
    while (!row.tagName().equals("tr")) {
      row = row.parent();
    }

    return ParserHelper.parseReturnTypeFromMemberSummaryRow(row, parseOwner());
  }

  private boolean isInInterface() {
    return element().ownerDocument().getElementsByClass("title").first()
        .ownText().startsWith("Interface");
  }
}
