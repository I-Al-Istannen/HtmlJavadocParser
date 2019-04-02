package de.ialistannen.htmljavadocparser.resolving;

import de.ialistannen.htmljavadocparser.exception.ResolveException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * A resolver that serves files from jar/file resources via {@link Class#getResource(String)}.
 */
public class ResourcesResolver implements DocumentResolver {

  private String baseUrl;
  private String basePath;

  /**
   * Creates a new resources resolver.
   *
   * @param baseUrl the base url all urls are relative to
   * @param basePath the base resources path
   */
  public ResourcesResolver(String baseUrl, String basePath) {
    this.baseUrl = baseUrl;
    this.basePath = basePath;
  }

  @Override
  public Document resolve(String url) {
    try {
      return Jsoup.parse(
          getClass().getResourceAsStream(resolvePath(url)),
          StandardCharsets.UTF_8.name(),
          baseUrl + getRelative(url)
      );
    } catch (IOException e) {
      throw new ResolveException("Error reading local file for '" + url + "'", e);
    }
  }

  private String getRelative(String input) {
    Path base = Path.of(baseUrl);
    Path given = Path.of(input);

    Path relative = base.relativize(given);
    return relative.getParent() == null ? "" : "/" + relative.getParent().toString();
  }

  private String resolvePath(String input) {
    return basePath + "/" + input
        .replace(baseUrl, "")
        .replaceAll("#.+", ""); // remove fragment
  }
}
