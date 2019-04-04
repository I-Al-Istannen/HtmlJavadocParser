package de.ialistannen.htmljavadocparser.resolving;

import de.ialistannen.htmljavadocparser.exception.ResolveException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * Resolves a url to a document.
 */
public interface DocumentResolver {

  /**
   * Returns the document with the given URL.
   *
   * @param url the url
   * @return the document
   * @throws ResolveException if an error occurred
   */
  Document resolve(String url);

  /**
   * Relativizes an absolute url: https://docs.oracle.com/javase/10/docs/api/lang/java/String.html
   * would become lang/java/String.html.
   *
   * @param absUrl the absolute url
   * @return the relative url
   */
  String relativizeAbsoluteUrl(String absUrl);

  /**
   * Relativizes an absolute url: https://docs.oracle.com/javase/10/docs/api/lang/java/String.html
   * would become lang/java/String.html.
   *
   * @param link the link
   * @return the relative url
   */
  default String relativizeAbsoluteUrl(Element link) {
    return relativizeAbsoluteUrl(link.absUrl("href"));
  }
}
