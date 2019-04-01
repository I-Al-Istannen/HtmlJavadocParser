package de.ialistannen.htmljavadocparser.parsing;

import static de.ialistannen.htmljavadocparser.util.LinkUtils.linkToFqn;

import de.ialistannen.htmljavadocparser.model.properties.HasVisibility.VisibilityLevel;
import java.util.Arrays;
import org.jsoup.nodes.Element;

final class ParserHelper {

  /**
   * Extracts the visibility from the declaration.
   *
   * @param declaration the declaration
   * @return the visibility
   */
  static VisibilityLevel parseVisibilityLevel(String declaration) {
    String firstModifier = declaration
        // remove annotations with arguments
        .replaceAll("@.+?\\(.+?\\)", "")
        // remove annotations without arguments
        .replaceAll("@.+? ", "")
        .trim()
        .split("\\s")[0];

    try {
      return VisibilityLevel.valueOf(firstModifier.toUpperCase());
    } catch (IllegalArgumentException e) {
      return VisibilityLevel.PACKAGE_PRIVATE;
    }
  }

  /**
   * Extracts whether the object is static from the declaration.
   *
   * @param declaration the declaration
   * @return true if the object is static
   */
  static boolean parseIsStatic(String declaration) {
    String[] modifiers = declaration
        // remove annotations with arguments
        .replaceAll("@.+?\\(.+?\\)", "")
        // remove annotations without arguments
        .replaceAll("@.+? ", "")
        .trim()
        .split("\\s");

    return Arrays.asList(modifiers).contains("static");
  }

  /**
   * Parses the fqn of the return type from the member summary list row.
   *
   * @param row the row
   * @param owner the owning class (used if it is a constructor)
   * @return the return type
   */
  static String parseReturnTypeFromMemberSummaryRow(Element row, String owner) {
    // constructor
    if (!row.getElementsByClass("colConstructorName").isEmpty()) {
      return owner;
    }

    Element returnTypeTd = row.getElementsByClass("colFirst").first();
    Element returnTypeLink = returnTypeTd.getElementsByTag("a").first();

    // no link, so it was a primitive type
    if (returnTypeLink == null) {
      return returnTypeTd.text()
          .replace("static", "")
          .trim();
    }

    return linkToFqn(returnTypeLink.attr("href")).replaceAll("#.+", "");

  }
}
