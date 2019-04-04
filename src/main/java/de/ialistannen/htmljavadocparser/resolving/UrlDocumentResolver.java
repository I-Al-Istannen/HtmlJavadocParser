package de.ialistannen.htmljavadocparser.resolving;

import de.ialistannen.htmljavadocparser.util.LinkUtils;
import java.io.IOException;
import java.lang.module.ResolutionException;
import java.nio.file.Path;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * A {@link DocumentResolver} that connects to the internet.
 */
public class UrlDocumentResolver implements DocumentResolver {

  private final String baseUrl;

  /**
   * The base url.
   *
   * @param baseUrl the base url
   */
  public UrlDocumentResolver(String baseUrl) {
    this.baseUrl = baseUrl;
  }

  @Override
  public Document resolve(String url) {
    try {
      return Jsoup.connect(url).get();
    } catch (IOException e) {
      e.printStackTrace();
      throw new ResolutionException(e);
    }
  }

  @Override
  public String relativizeAbsoluteUrl(String absUrl) {
    String cleanedUrl = LinkUtils.clearQueryFragment(absUrl);
    return Path.of(baseUrl).relativize(Path.of(cleanedUrl)).toString();
  }
}

