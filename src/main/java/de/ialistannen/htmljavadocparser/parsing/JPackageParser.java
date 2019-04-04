package de.ialistannen.htmljavadocparser.parsing;

import de.ialistannen.htmljavadocparser.resolving.DocumentResolver;
import de.ialistannen.htmljavadocparser.util.LinkUtils;
import de.ialistannen.htmljavadocparser.util.StringUtils;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * A parser for javadoc packages.
 */
public class JPackageParser {

  private DocumentResolver resolver;
  private String url;

  public JPackageParser(DocumentResolver resolver, String url) {
    this.resolver = resolver;
    this.url = url;
  }

  private Document document() {
    return resolver.resolve(url);
  }

  public List<Element> parseJavadoc() {
    return document().getElementById("package.description").siblingElements();
  }

  private String parseQualifiedName() {
    String name = document().getElementsByClass("title").first()
        .text()
        .replace("Package", "");
    return StringUtils.normalizeWhitespace(name).trim();
  }

  public Collection<String> parseContainedTypes() {
    return document().getElementsByClass("typeSummary").stream()
        .map(element -> element
            .getElementsByAttributeValueEnding("title", "in " + parseQualifiedName())
        )
        .flatMap(Collection::stream)
        .map(element -> LinkUtils.linkToFqn(resolver.relativizeAbsoluteUrl(element)))
        .collect(Collectors.toSet());
  }

  public String parseUrl() {
    return url;
  }
}
