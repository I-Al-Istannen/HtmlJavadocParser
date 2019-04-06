package de.ialistannen.htmljavadocparser.resolving;

import de.ialistannen.htmljavadocparser.exception.ResolveException;
import de.ialistannen.htmljavadocparser.util.LinkUtils;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.file.Path;
import java.time.Duration;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Document;

/**
 * A {@link DocumentResolver} that connects to the internet.
 */
public class UrlDocumentResolver implements DocumentResolver {

  private final String baseUrl;
  private final HttpClient httpClient;

  /**
   * The base url.
   *
   * @param baseUrl the base url
   */
  public UrlDocumentResolver(String baseUrl) {
    this.baseUrl = baseUrl;
    this.httpClient = HttpClient.newBuilder()
        .connectTimeout(Duration.ofSeconds(10))
        .followRedirects(Redirect.NORMAL)
        .build();
  }

  @Override
  public Document resolve(String url) {
    try {

      HttpRequest request = HttpRequest.newBuilder(URI.create(url))
          .header("User-Agent", HttpConnection.DEFAULT_UA)
          .build();
      HttpResponse<String> response = httpClient
          .send(request, BodyHandlers.ofString());

      return Jsoup.parse(response.body(), url);
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
      throw new ResolveException("Error fetching url '" + url + "'", e);
    }
  }

  @Override
  public String relativizeAbsoluteUrl(String absUrl) {
    String cleanedUrl = LinkUtils.clearQueryFragment(absUrl);
    return Path.of(baseUrl).relativize(Path.of(cleanedUrl)).toString();
  }
}

