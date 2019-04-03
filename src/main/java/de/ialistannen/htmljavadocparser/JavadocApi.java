package de.ialistannen.htmljavadocparser;

import de.ialistannen.htmljavadocparser.model.types.JavadocClass;
import de.ialistannen.htmljavadocparser.resolving.DocumentResolver;
import de.ialistannen.htmljavadocparser.resolving.HtmlSummaryParser;
import de.ialistannen.htmljavadocparser.resolving.Index;
import de.ialistannen.htmljavadocparser.resolving.ResourcesResolver;

/**
 * The base class for a Javadoc API.
 */
public class JavadocApi {

  private final String baseUrl;
  private final Index index;

  /**
   * Creates a new javadoc API and starts indexing.
   *
   * @param baseUrl the base url of the API
   * @param documentResolver the document resolver to use
   */
  public JavadocApi(String baseUrl, DocumentResolver documentResolver) {
    this.baseUrl = baseUrl;
    this.index = new Index();

    HtmlSummaryParser summaryParser = new HtmlSummaryParser(documentResolver, baseUrl, index);
    index.addTypes(summaryParser.getTypes());
    index.addPackages(summaryParser.getPackages());
  }

  /**
   * Returns the javadoc index.
   *
   * @return the javadoc index
   */
  public Index getIndex() {
    return index;
  }

  @Override
  public String toString() {
    return "JavadocApi{" +
        "baseUrl='" + baseUrl + '\'' +
        '}';
  }

  public static void main(String[] args) {
    String baseUrl = "https://docs.oracle.com/javase/10/docs/api/";
    DocumentResolver documentResolver = new ResourcesResolver(
        baseUrl, "/testfiles"
    );

    JavadocApi javadocApi = new JavadocApi(baseUrl, documentResolver);

    JavadocClass string = (JavadocClass) javadocApi.getIndex()
        .getTypeForFullNameOrError("java.util.Map");
    string.getMethods().stream()
        .filter(m -> m.getSimpleName().equals("ofEntries"))
        .findFirst()
        .ifPresent(invocable -> System.out.println(invocable.getReturnType()));
  }
}
