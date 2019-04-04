package de.ialistannen.htmljavadocparser.resolving;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * A document resolver that caches documents to speed up operations.
 * <p>
 * This is particularly helpful as the lazy operations will request documents more than once, often
 * in rapid succession.
 */
public class CachingDocumentResolver implements DocumentResolver {

  private DocumentResolver wrapped;
  private SimpleCache<String, Document> cache;

  /**
   * Creates a new caching document resolver.
   *
   * @param wrapped the underlying resolver
   * @param cache the cahce to use
   */
  public CachingDocumentResolver(DocumentResolver wrapped, SimpleCache<String, Document> cache) {
    this.wrapped = wrapped;
    this.cache = cache;
  }

  @Override
  public String relativizeAbsoluteUrl(String absUrl) {
    return wrapped.relativizeAbsoluteUrl(absUrl);
  }

  @Override
  public String relativizeAbsoluteUrl(Element link) {
    return wrapped.relativizeAbsoluteUrl(link);
  }

  @Override
  public Document resolve(String url) {
    String cleanUrl = cleanUrl(url);

    Document cached = cache.get(cleanUrl);

    if (cached == null) {
      cached = wrapped.resolve(url);
      cache.put(cleanUrl, cached);
    }

    return cached;
  }

  private String cleanUrl(String url) {
    int fragmentIndex = url.indexOf('#');

    if (fragmentIndex < 0) {
      return url;
    }
    return url.substring(0, fragmentIndex);
  }

  /**
   * A very simple cache spec.
   *
   * @param <K> the type of the keys
   * @param <V> the type of the values
   */
  public interface SimpleCache<K, V> {

    /**
     * Adds an element to the cache.
     *
     * @param key the key of the value
     * @param value the value
     */
    void put(K key, V value);

    /**
     * Retrieves an element from the cache.
     *
     * @param key the cache key
     * @return the cached value, null if none
     */
    V get(K key);
  }
}
