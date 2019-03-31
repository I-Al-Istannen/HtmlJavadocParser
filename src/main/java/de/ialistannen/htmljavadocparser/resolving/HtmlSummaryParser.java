package de.ialistannen.htmljavadocparser.resolving;

import de.ialistannen.htmljavadocparser.impl.JAnnotation;
import de.ialistannen.htmljavadocparser.impl.JClass;
import de.ialistannen.htmljavadocparser.impl.JPackage;
import de.ialistannen.htmljavadocparser.impl.JType;
import de.ialistannen.htmljavadocparser.model.JavadocPackage;
import de.ialistannen.htmljavadocparser.model.types.Type;
import de.ialistannen.htmljavadocparser.parsing.JClassParser;
import de.ialistannen.htmljavadocparser.parsing.JTypeParser;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * A HTML summary page parser.
 */
public class HtmlSummaryParser {

  private final DocumentResolver documentResolver;
  private final String baseUrl;
  private final Index index;
  private Collection<Type> typeCache;
  private Collection<JavadocPackage> packageCache;

  /**
   * Creates a new HTML resolver.
   *
   * @param documentResolver the document resolver to use
   * @param baseUrl the base url
   * @param index the index
   */
  public HtmlSummaryParser(DocumentResolver documentResolver, String baseUrl, Index index) {
    this.documentResolver = documentResolver;
    this.baseUrl = baseUrl;
    this.index = index;
  }

  private void index() {
    Document document = documentResolver.resolve(baseUrl + "/allclasses-noframe.html");
    Element main = document.getElementsByAttributeValue("role", "main").first();

    typeCache = new ArrayList<>();
    packageCache = new HashSet<>();

    for (Element a : main.getElementsByTag("a")) {
      String url = a.absUrl("href");

      if (a.attr("title").contains("annotation in")) {
        typeCache.add(new JAnnotation(
            extractFqn(document, url), new JTypeParser(url, documentResolver), index
        ));
      } else if (a.attr("title").contains("class in")) {
        JClass jClass = new JClass(
            extractFqn(document, url),
            index,
            new JClassParser(url, documentResolver)
        );
        typeCache.add(jClass);
      } else {
        JType type = new JType(
            extractFqn(document, url),
            index,
            new JTypeParser(url, documentResolver)
        );
        typeCache.add(type);
      }

      packageCache.add(new JPackage(extractPackageName(document, url)));
    }
  }

  private String extractFqn(Document document, String url) {
    return url
        .replace(document.baseUri(), "")
        .replace(".html", "")
        .replace("/", ".");
  }

  private String extractPackageName(Document document, String url) {
    String fqn = extractFqn(document, url);
    return fqn.substring(0, fqn.lastIndexOf("."));
  }

  /**
   * Returns all types.
   *
   * @return all types
   */
  public Collection<Type> getTypes() {
    if (typeCache == null) {
      index();
    }

    return Collections.unmodifiableCollection(typeCache);
  }

  /**
   * Returns all packages.
   *
   * @return all packages
   */
  public Collection<JavadocPackage> getPackages() {
    return Collections.unmodifiableCollection(packageCache);
  }
}
