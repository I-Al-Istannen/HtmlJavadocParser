package de.ialistannen.htmljavadocparser.resolving;


import static java.util.stream.Collectors.toList;

import de.ialistannen.htmljavadocparser.model.types.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Predicate;

/**
 * The main javadoc index.
 */
public class Index {

  private final BiFunction<Index, String, Optional<Type>> loader;
  private Map<String, Type> types;

  /**
   * Creates a new index.
   *
   * @param loader the loader function
   */
  public Index(BiFunction<Index, String, Optional<Type>> loader) {
    this.loader = loader;
    this.types = new HashMap<>();
  }

  /**
   * Finds a name by its fully qualified name.
   *
   * @param fullyQualifiedName the fully qualified type name
   * @return the found type
   */
  public Optional<Type> getTypeForFullName(String fullyQualifiedName) {
    if (types.containsKey(fullyQualifiedName)) {
      return Optional.of(types.get(fullyQualifiedName));
    }
    Optional<Type> loaded = loader.apply(this, fullyQualifiedName);

    if (loaded.isEmpty()) {
      return Optional.empty();
    }

    types.put(loaded.get().getFullyQualifiedName(), loaded.get());

    return loaded;
  }

  /**
   * Finds a name by its fully qualified name.
   *
   * @param fullyQualifiedName the fully qualified type name
   * @return the found type
   * @throws java.util.NoSuchElementException if the class was not found
   */
  public Type getTypeForFullNameOrError(String fullyQualifiedName) {
    return getTypeForFullName(fullyQualifiedName).orElseThrow();
  }

  /**
   * Finds all matching types.
   *
   * @param filter the filter
   * @return all matching types
   */
  public List<Type> findMatching(Predicate<Type> filter) {
    return types.values().stream()
        .filter(filter)
        .collect(toList());
  }

  /**
   * Finds all types with the given {@link Type#getSimpleName()}.
   *
   * @param name the name
   * @return all matching types
   */
  public List<Type> findWithName(String name) {
    return findMatching(type -> type.getSimpleName().equals(name));
  }
}
