package de.ialistannen.htmljavadocparser.util;

import java.util.function.Supplier;

/**
 * A memoizing supplier.
 *
 * @param <T> the type of the supplier
 */
public class Memoized<T> implements Supplier<T> {

  private Supplier<T> get;
  private T cached;
  private boolean initialized;

  /**
   * Creates a new memoized supplier.
   *
   * @param get the underlying supplier
   */
  public Memoized(Supplier<T> get) {
    this.get = get;
  }

  @Override
  public T get() {
    if (!initialized) {
      cached = get.get();
      initialized = true;
    }
    return cached;
  }
}
