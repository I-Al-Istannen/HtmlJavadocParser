package de.ialistannen.htmljavadocparser.resolving;

import de.ialistannen.htmljavadocparser.exception.ResolveException;
import org.jsoup.nodes.Document;

/**
 * Resolves an url to a document.
 */
public interface DocumentResolver {

  /**
   * Returns the document with the given URL.
   *
   * @param url the url
   * @return the document
   * @throws ResolveException if an error occurred
   */
  Document resolve(String url) throws ResolveException;
}
