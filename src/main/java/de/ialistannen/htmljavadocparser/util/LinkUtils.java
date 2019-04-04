package de.ialistannen.htmljavadocparser.util;

public final class LinkUtils {

  private LinkUtils() {
    throw new UnsupportedOperationException();
  }

  /**
   * Converts a relative link (like {@code java/lang/String.html}) to a fully qualified name.
   *
   * @param link the link
   * @return the fully qualified name
   */
  public static String linkToFqn(String link) {
    return clearQueryFragment(link)
        // strip out module name (as they start with "java.base/package/path/"
        .replaceAll("^.+\\..+?/", "")
        .replace(".html", "")
        .replace("/", ".")
        // Clear constructors
        .replace("<init>", "");
  }

  /**
   * Clears the query fragment (i.e. the things following a ?).
   *
   * @param link the link
   * @return the url without the query
   */
  public static String clearQueryFragment(String link) {
    return link
        // clear query fragment. the or is to make sure stuff like "...?query#nameOfMethod()" works
        .replaceAll("\\?.+?(#|$)", "$1");
  }

  /**
   * Returns the url fragment.
   *
   * @param url the url
   * @return the url fragment
   */
  public static String urlFragment(String url) {
    return url.substring(url.lastIndexOf('#') + 1);
  }
}
