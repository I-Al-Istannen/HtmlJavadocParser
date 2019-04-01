package de.ialistannen.htmljavadocparser.parsing;

import static de.ialistannen.htmljavadocparser.util.LinkUtils.linkToFqn;

import de.ialistannen.htmljavadocparser.impl.JInvocable;
import de.ialistannen.htmljavadocparser.model.properties.Deprecatable.DeprecationStatus;
import de.ialistannen.htmljavadocparser.model.properties.HasVisibility.VisibilityLevel;
import de.ialistannen.htmljavadocparser.model.properties.Invocable;
import de.ialistannen.htmljavadocparser.resolving.DocumentResolver;
import de.ialistannen.htmljavadocparser.resolving.Index;
import de.ialistannen.htmljavadocparser.util.StringUtils;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.jsoup.select.NodeVisitor;

public class JTypeParser {

  private final String url;
  private final DocumentResolver resolver;

  public JTypeParser(String url, DocumentResolver resolver) {
    this.url = url;
    this.resolver = resolver;
  }

  protected Document document() {
    return resolver.resolve(url);
  }

  protected DocumentResolver resolver() {
    return resolver;
  }

  /**
   * Extracts the type declaration from the document.
   *
   * @return the parsed declaration
   */
  public String parseDeclaration() {
    Element typeDescriptionPre = getTypeDeclarationPre();

    return StringUtils.normalizeWhitespace(typeDescriptionPre.text());
  }

  protected Element getTypeDeclarationPre() {
    Element typeNameLabel = document().getElementsByClass("typeNameLabel").first();
    return typeNameLabel.parent();
  }

  /**
   * Extracts the superclass name.
   *
   * @return the superclass name
   */
  public Optional<String> parseSuperClass() {
    Element list = document().getElementsByClass("inheritance").first();

    if (list == null) {
      return Optional.empty();
    }

    list = list.child(0);

    Element relevantChild = list.child(list.children().size() - 1);
    return Optional.of(relevantChild.text());
  }

  public String parsePackage() {
    return document().getElementsByClass("packageLabelInType").first()
        .nextElementSibling()
        .text();
  }

  public String parseSimpleName() {
    return StringUtils.normalizeWhitespace(document().selectFirst(".typeNameLabel").text())
        .replaceAll("<.+", "");
  }

  /**
   * Extracts the superinterfaces.
   *
   * @return the superinterfaces
   */
  public List<String> parseSuperInterfaces() {
    Element dd = document().getElementsContainingOwnText("All Implemented Interfaces:")
        .first();

    if (dd == null) {
      return Collections.emptyList();
    }

    dd = dd.nextElementSibling();

    return dd.children().stream()
        .map(code -> linkToFqn(code.child(0).attr("href")))
        .collect(Collectors.toList());
  }

  public DeprecationStatus parseDeprecationStatus() {
    return document().select(".description .deprecationBlock > span").isEmpty()
        ? DeprecationStatus.NOT_DEPRECATED
        : DeprecationStatus.DEPRECATED;
  }

  public VisibilityLevel parseVisibilityLevel() {
    return ParserHelper.parseVisibilityLevel(parseDeclaration());
  }

  public List<String> parseAnnotations() {
    return getTypeDeclarationPre().getElementsByTag("a").stream()
        .filter(element -> element.attr("title").contains("annotation in"))
        .map(element -> linkToFqn(element.attr("href")))
        .collect(Collectors.toList());
  }

  public List<Invocable> parseMethods(Index index) {
    List<Invocable> methods = new ArrayList<>();

    Element methodSummary = document().getElementById("method.summary");

    if (methodSummary == null) {
      return Collections.emptyList();
    }

    Elements rows = methodSummary.parent().getElementsByTag("tr");
    for (Element row : rows) {
      Element link = row.getElementsByClass("colSecond").first()
          .getElementsByTag("a").first();

      if (link == null) {
        continue;
      }

      methods.add(invocableFromLink(link, index));
    }

    methods.addAll(getInheritedMethods(index));
    return methods;
  }

  protected Invocable invocableFromLink(Element link, Index index) {
    JInvocableParser parser = new JInvocableParser(link.absUrl("href"), resolver());
    String decodedLink = URLDecoder.decode(link.attr("href"), StandardCharsets.UTF_8);
    String fullyQualifiedName = linkToFqn(decodedLink);
    return new JInvocable(fullyQualifiedName, index, parser);
  }

  private List<Invocable> getInheritedMethods(Index index) {
    List<Invocable> methods = new ArrayList<>();
    document().traverse(new NodeVisitor() {
      @Override
      public void head(Node node, int depth) {
        if (!node.attr("id").startsWith("methods.inherited.from") || !(node instanceof Element)) {
          return;
        }

        Element parent = (Element) node.parent();
        for (Element link : parent.getElementsByTag("a")) {
          if (!link.attr("href").contains("#")) {
            continue;
          }
          methods.add(invocableFromLink(link, index));
        }
      }

      @Override
      public void tail(Node node, int depth) {
      }
    });

    return methods;
  }
}
