package de.ialistannen.htmljavadocparser.parsing;

import de.ialistannen.htmljavadocparser.model.properties.HasVisibility.VisibilityLevel;
import java.util.Arrays;

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

}
