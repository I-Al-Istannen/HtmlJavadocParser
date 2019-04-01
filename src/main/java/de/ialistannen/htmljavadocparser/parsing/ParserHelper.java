package de.ialistannen.htmljavadocparser.parsing;

import de.ialistannen.htmljavadocparser.model.properties.HasVisibility.VisibilityLevel;

final class ParserHelper {

  /**
   * Extracts the visibility from the declaration.
   *
   * @param declaration the declaration
   * @return the visibility
   */
  static VisibilityLevel parseVisibilityLevel(String declaration) {
    String firstModifier = declaration
        .replaceAll("@.+? ", "")
        .split("\\s")[0];
    try {
      return VisibilityLevel.valueOf(firstModifier.toUpperCase());
    } catch (IllegalArgumentException e) {
      return VisibilityLevel.PACKAGE_PRIVATE;
    }
  }

}
