package de.ialistannen.htmljavadocparser.parsing;

import de.ialistannen.htmljavadocparser.model.properties.HasVisibility.VisibilityLevel;
import de.ialistannen.htmljavadocparser.model.properties.Overridable.ControlModifier;
import de.ialistannen.htmljavadocparser.resolving.DocumentResolver;
import de.ialistannen.htmljavadocparser.util.LinkUtils;
import de.ialistannen.htmljavadocparser.util.StringUtils;
import java.util.Arrays;
import java.util.Collection;
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
    return ParserHelper.parseVisibilityLevel(parseDeclaration());
  }

  public Collection<ControlModifier> parseControlModifiers() {
    String declaration = parseDeclaration();

    return Arrays.stream(ControlModifier.values())
        .filter(controlModifier -> declaration.contains(controlModifier.getName()))
        .collect(Collectors.toList());
  }
}
