package de.ialistannen.htmljavadocparser.util;

public class StringUtils {

  private static final char NON_BREAKING_SPACE = '\u00A0';
  private static final char NON_BREAKING_SPACE_2 = '\u2007';
  private static final char NON_BREAKING_SPACE_3 = '\u202F';

  /**
   * Normalizes the whitespace by collapsing whitespace, removing newlines and converting every
   * blank to a normal space.
   *
   * @param input the input string
   * @return the normalized string
   */
  public static String normalizeWhitespace(String input) {
    return input.replaceAll("\\s+", " ")
        .replace(NON_BREAKING_SPACE, ' ')
        .replace(NON_BREAKING_SPACE_2, ' ')
        .replace(NON_BREAKING_SPACE_3, ' ');
  }
}
