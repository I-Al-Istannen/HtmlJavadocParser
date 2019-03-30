package de.ialistannen.htmljavadocparser.parsing;

import de.ialistannen.htmljavadocparser.model.properties.Deprecatable.DeprecationStatus;
import de.ialistannen.htmljavadocparser.model.properties.HasVisibility.VisibilityLevel;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class JTypeParser {

  private Document document;

  public JTypeParser(Document document) {
    this.document = document;
  }

  /**
   * Extracts the type declaration from the document.
   *
   * @return the parsed declaration
   */
  public String parseDeclaration() {
    Element typeNameLabel = document.getElementsByClass("typeNameLabel").first();
    Element typeDescriptionPre = typeNameLabel.parent();

    return typeDescriptionPre.text().replaceAll("\\n", " ");
  }

  /**
   * Extracts the superclass name.
   *
   * @return the superclass name
   */
  public Optional<String> parseSuperClass() {
    Element list = document.getElementsByClass("inheritance").first().child(0);
    Element relevantChild = list.child(list.children().size() - 1);
    return Optional.of(relevantChild.text());
  }

  public String parsePackage() {
    return document.getElementsByClass("packageLabelInType").first()
        .nextElementSibling()
        .text();
  }

  public String parseSimpleName() {
    return document.selectFirst(".title").text().split(" ")[1];
  }

  /**
   * Extracts the superinterfaces.
   *
   * @return the superinterfaces
   */
  public List<String> parseSuperInterfaces() {
    Element path = document.selectFirst(".description > ul > li > dl > dd");

    return path.children().stream()
        .map(code -> linkToFqn(code.child(0).attr("href")))
        .collect(Collectors.toList());
  }

  public DeprecationStatus parseDeprecationStatus() {
    return document.select(".description .deprecationBlock > span").isEmpty()
        ? DeprecationStatus.NOT_DEPRECATED
        : DeprecationStatus.DEPRECATED;
  }

  public VisibilityLevel parseVisibilityLevel() {
    String firstModifier = parseDeclaration().split(" ")[0];
    try {
      return VisibilityLevel.valueOf(firstModifier.toUpperCase());
    } catch (IllegalArgumentException e) {
      return VisibilityLevel.PACKAGE_PRIVATE;
    }
  }

  public List<String> parseMethods() {
    Elements rows = document.getElementById("method.summary").parent().getElementsByTag("tr");
    for (Element row : rows) {

    }
  }

  private String linkToFqn(String link) {
    return link.replaceAll("(\\.\\./)+", "")
        .replace(".html", "")
        .replace("/", ".");
  }
}
