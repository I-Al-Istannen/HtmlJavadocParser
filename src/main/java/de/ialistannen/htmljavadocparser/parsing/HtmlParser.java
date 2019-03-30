package de.ialistannen.htmljavadocparser.parsing;

import de.ialistannen.htmljavadocparser.model.JavadocPackage;
import de.ialistannen.htmljavadocparser.model.types.Type;
import de.ialistannen.htmljavadocparser.resolving.Index;
import org.jsoup.nodes.Document;

/**
 * A HTML parser.
 */
public class HtmlParser {

  /**
   * Parses a given document to a type.
   *
   * @param document the document to parse
   * @param index the index to use for lookups
   * @return the parsed type
   */
  public Type parseType(Document document, Index index) {
    return null;
  }

  /**
   * Parses a package.
   *
   * @param document the document to parse
   * @param index the index to use
   * @return the parsed package
   */
  public JavadocPackage parsePackage(Document document, Index index) {
    return null;
  }
}
