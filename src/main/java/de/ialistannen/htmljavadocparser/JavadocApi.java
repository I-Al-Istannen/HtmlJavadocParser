package de.ialistannen.htmljavadocparser;

import de.ialistannen.htmljavadocparser.model.JavadocPackage;
import de.ialistannen.htmljavadocparser.model.types.Type;
import de.ialistannen.htmljavadocparser.resolving.DocumentResolver;
import de.ialistannen.htmljavadocparser.resolving.HtmlSummaryParser;
import de.ialistannen.htmljavadocparser.resolving.Index;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * The base class for a Javadoc API.
 */
public class JavadocApi extends Index {

  private List<ApiEntry> apis;

  /**
   * Creates a new javadoc api.
   */
  public JavadocApi() {
    this.apis = new ArrayList<>();
  }

  /**
   * Adds an API.
   *
   * @param baseUrl the base url of the api
   * @param allClassesSuffix the suffix for the all classes url. "/allclasses-noframe.html" for
   *     *     10, "/allclasses.html" for java 11
   * @param resolver the document resolver to use
   * @return this object
   */
  public JavadocApi addApi(String baseUrl, String allClassesSuffix, DocumentResolver resolver) {
    this.apis.add(new ApiEntry(baseUrl, resolver, allClassesSuffix));

    return this;
  }

  /**
   * Returns all indices.
   *
   * @return all indices
   */
  public List<Index> getIndices() {
    return apis.stream()
        .map(ApiEntry::getIndex)
        .collect(Collectors.toList());
  }

  @Override
  public Optional<Type> getTypeForFullName(String fullyQualifiedName) {
    return getIndices().stream()
        .flatMap(index -> index.getTypeForFullName(fullyQualifiedName).stream())
        .findFirst();
  }

  @Override
  public Type getTypeForFullNameOrError(String fullyQualifiedName) {
    return getTypeForFullName(fullyQualifiedName)
        .orElseThrow(() -> new NoSuchElementException("Type not found: " + fullyQualifiedName));
  }

  @Override
  public List<Type> findMatching(Predicate<Type> filter) {
    return getIndices().stream()
        .flatMap(index -> index.findMatching(filter).stream())
        .collect(Collectors.toList());
  }

  @Override
  public List<Type> findWithSimpleName(String name) {
    return getIndices().stream()
        .flatMap(index -> index.findWithSimpleName(name).stream())
        .collect(Collectors.toList());
  }

  @Override
  public Optional<JavadocPackage> getPackage(String name) {
    return getIndices().stream()
        .flatMap(index -> index.getPackage(name).stream())
        .findFirst();
  }

  @Override
  public JavadocPackage getPackageOrError(String name) {
    return getPackage(name)
        .orElseThrow(() -> new NoSuchElementException("Packahe not found: " + name));
  }

  @Override
  public String toString() {
    return "JavadocApi{" +
        "apis=" + apis.stream().map(ApiEntry::getBaseUrl).collect(Collectors.joining(", "))
        + '}';
  }

  /**
   * A single API entry.
   */
  private class ApiEntry {

    private final String baseUrl;
    private Index index;

    ApiEntry(String baseUrl, DocumentResolver documentResolver, String allClassesSuffix) {
      this.baseUrl = baseUrl;
      this.index = new Index();

      HtmlSummaryParser summaryParser = new HtmlSummaryParser(
          documentResolver,
          baseUrl,
          baseUrl + allClassesSuffix,
          JavadocApi.this
      );
      index.addTypes(summaryParser.getTypes());
      index.addPackages(summaryParser.getPackages());
    }

    /**
     * Returns the index.
     *
     * @return the index
     */
    Index getIndex() {
      return index;
    }

    /**
     * Returns the base url.
     *
     * @return the base url
     */
    String getBaseUrl() {
      return baseUrl;
    }
  }
}
