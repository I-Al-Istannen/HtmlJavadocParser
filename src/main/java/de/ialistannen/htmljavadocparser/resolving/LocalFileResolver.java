package de.ialistannen.htmljavadocparser.resolving;

import de.ialistannen.htmljavadocparser.exception.ResolveException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
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
          baseUrl
      );
    } catch (IOException e) {
      throw new ResolveException("Error reading local file for '" + url + "'", e);
    }
  }

  private String resolvePath(String input) {
    return basePath + "/" + input.replace(baseUrl, "");
  }
}
