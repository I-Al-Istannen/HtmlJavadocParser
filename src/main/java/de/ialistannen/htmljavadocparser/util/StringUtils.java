package de.ialistannen.htmljavadocparser.util;

public class StringUtils {

  private static final char NON_BREAKING_SPACE = '\u00A0';
  private static final char NON_BREAKING_SPACE_2 = '\u2007';
  private static final char NON_BREAKING_SPACE_3 = '\u202F';
  private static final char ZERO_WIDTH_SPACE = '\u200B';

  /**
   * Normalizes the whitespace by collapsing whitespace, removing newlines and converting every
   * blank to a normal space.
   *
   * @param input the input string
   * @return the normalized string
   */
  public static String normalizeWhitespace(String input) {
    return input
        .replace(NON_BREAKING_SPACE, ' ')
        .replace(NON_BREAKING_SPACE_2, ' ')
        .replace(NON_BREAKING_SPACE_3, ' ')
        .replace(ZERO_WIDTH_SPACE, ' ')
        .replaceAll("\\s+", " ");
  }
}
