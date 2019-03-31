package de.ialistannen.htmljavadocparser.resolving;

import de.ialistannen.htmljavadocparser.exception.ResolveException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class LocalFileResolver implements DocumentResolver {

  private String baseUrl;
  private String basePath;

  public LocalFileResolver(String baseUrl, String basePath) {
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
