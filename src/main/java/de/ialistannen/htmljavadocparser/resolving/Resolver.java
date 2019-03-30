package de.ialistannen.htmljavadocparser.resolving;

import de.ialistannen.htmljavadocparser.exception.ResolveException;
import java.util.Optional;

/**
 * Resolves a type based on its name.
 *
 * @param <T> the type
 * @param <R> the result type
 */
public interface Resolver<T, R> {

  /**
   * Resolves a type.
   *
   * @param input the input
   * @return the resolved type
   * @throws ResolveException if an error occurred
   */
  Optional<R> resolve(T input);
}
