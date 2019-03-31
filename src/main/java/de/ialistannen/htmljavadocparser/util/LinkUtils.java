package de.ialistannen.htmljavadocparser.util;

public final class LinkUtils {

  private LinkUtils() {
    throw new UnsupportedOperationException();
  }

  /**
   * Converts a relative link (like {@code ../../java/lang/String.html}) to a fully qualified name.
   *
   * @param link the link
   * @return the fully qualified name
   */
  public static String linkToFqn(String link) {
    return link.replaceAll("(\\.\\./)+", "")
        .replace(".html", "")
        .replace("/", ".");
  }

}
