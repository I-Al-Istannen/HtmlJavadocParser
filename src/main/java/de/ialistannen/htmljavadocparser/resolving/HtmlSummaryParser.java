package de.ialistannen.htmljavadocparser.resolving;

import de.ialistannen.htmljavadocparser.impl.JAnnotation;
import de.ialistannen.htmljavadocparser.impl.JClass;
import de.ialistannen.htmljavadocparser.impl.JEnum;
import de.ialistannen.htmljavadocparser.impl.JInterface;
import de.ialistannen.htmljavadocparser.impl.JPackage;
import de.ialistannen.htmljavadocparser.model.JavadocPackage;
import de.ialistannen.htmljavadocparser.model.types.Type;
import de.ialistannen.htmljavadocparser.parsing.JAnnotationParser;
import de.ialistannen.htmljavadocparser.parsing.JClassParser;
import de.ialistannen.htmljavadocparser.parsing.JEnumParser;
import de.ialistannen.htmljavadocparser.parsing.JInterfaceParser;
import de.ialistannen.htmljavadocparser.parsing.JPackageParser;
import de.ialistannen.htmljavadocparser.util.LinkUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * A HTML summary page parser.
 */
public class HtmlSummaryParser {

  private final DocumentResolver documentResolver;
  private final String baseUrl;
  private final String allClassesUrl;
  private final Index index;
  private Collection<Type> typeCache;
  private Map<String, JavadocPackage> packageCache;

  /**
   * Creates a new HTML resolver.
   *
   * @param documentResolver the document resolver to use
   * @param baseUrl the base url
   * @param allClassesUrl the url to find the class list at
   * @param index the index
   */
  public HtmlSummaryParser(DocumentResolver documentResolver, String baseUrl, String allClassesUrl,
      Index index) {
    this.documentResolver = documentResolver;
    this.baseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
    this.allClassesUrl = allClassesUrl;
    this.index = index;
  }

  private void index() {
    Document document = documentResolver.resolve(allClassesUrl);
    Element main = document.getElementsByAttributeValue("role", "main").first();

    typeCache = new ArrayList<>();
    packageCache = new HashMap<>();

    for (Element a : main.getElementsByTag("a")) {
      String url = a.absUrl("href");

      if (a.attr("title").contains("annotation in")) {
        typeCache.add(new JAnnotation(
            extractFqn(a),
            extractSimpleName(a),
            new JAnnotationParser(url, documentResolver),
            index
        ));
      } else if (a.attr("title").contains("class in")) {
        JClass jClass = new JClass(
            extractFqn(a),
            extractSimpleName(a),
            index,
            new JClassParser(url, documentResolver)
        );
        typeCache.add(jClass);
      } else if (a.attr("title").contains("interface in")) {
        JInterface jInterface = new JInterface(
            extractFqn(a),
            extractSimpleName(a),
            index,
            new JInterfaceParser(url, documentResolver)
        );
        typeCache.add(jInterface);
      } else if (a.attr("title").contains("enum in")) {
        JEnum jjEnum = new JEnum(
            extractFqn(a),
            extractSimpleName(a),
            index,
            new JEnumParser(url, documentResolver)
        );
        typeCache.add(jjEnum);
      } else {
        // Ignore unknown types
        continue;
      }

      String packageName = extractPackageName(a);
      if (!packageCache.containsKey(packageName)) {
        String packageUrl = extractPackageUrl(a);
        JPackageParser parser = new JPackageParser(documentResolver, packageUrl);
        packageCache.put(packageName, new JPackage(parser, packageName, index));
      }
    }
  }

  private String extractFqn(Element link) {
    return LinkUtils.linkToFqn(documentResolver.relativizeAbsoluteUrl(link));
  }

  private String extractPackageName(Element link) {
    return link.attr("title").replaceAll(".+? in (.+)", "$1");
  }

  private String extractPackageUrl(Element link) {
    String packageNameUrlFormat = extractPackageName(link).replace('.', '/');
    String href = link.attr("href");
    int startIndex = href.indexOf(packageNameUrlFormat);
    String fullNameUrlFormat = href.substring(0, startIndex + packageNameUrlFormat.length());
    return baseUrl + "/" + fullNameUrlFormat + "/package-summary.html";
  }

  private String extractSimpleName(Element link) {
    String packageName = extractPackageName(link);
    String fqn = extractFqn(link);

    return fqn.replace(packageName + ".", "");
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
    return Collections.unmodifiableCollection(packageCache.values());
  }
}
