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
    return link
//        // replace relative leadings: "../../whatever"
//        .replaceAll("(\\.\\./)+", "")
        // strip out module name (as they start with "java.base/package/path/"
        .replaceAll("^.+\\..+?/", "")
        .replace(".html", "")
        .replace("/", ".")
        // Clear constructors
        .replace("<init>", "");
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
