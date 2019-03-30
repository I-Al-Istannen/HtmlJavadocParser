package de.ialistannen.htmljavadocparser.resolving;

import static java.util.stream.Collectors.toMap;

import de.ialistannen.htmljavadocparser.exception.ResolveException;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class HtmlUrlResolver implements Resolver<String, String> {

  private final DocumentResolver documentResolver;
  private Map<String, String> cache;

  /**
   * Creates a new HTML resolver.
   *
   * @param documentResolver the document resolver to use
   */
  public HtmlUrlResolver(DocumentResolver documentResolver) {
    this.documentResolver = documentResolver;
  }

  @Override
  public Optional<String> resolve(String fullyQualifiedName) throws ResolveException {
    if (cache == null) {
      index();
    }

    return Optional.ofNullable(cache.get(fullyQualifiedName));
  }

  private void index() throws ResolveException {
    Document document = documentResolver.resolve("allclasses-noframe.html");
    Element main = document.getElementsByAttributeValue("role", "main").first();

    cache = main.getElementsByTag("a").stream()
        .map(element -> element.absUrl("href"))
        .collect(toMap(extractFqn(document), s -> s));
  }

  private Function<String, String> extractFqn(Document document) {
    return href -> href
        .replace(document.baseUri(), "")
        .replace(".html", "")
        .replace("/", ".");
  }

  public static void main(String[] args) throws ResolveException {
    System.out.println(
        new HtmlUrlResolver(
            new LocalFileResolver("https://docs.oracle.com/javase/10/docs/api/", "/testfiles"))
            .resolve("java.lang.String")
    );
  }
}
