package de.ialistannen.htmljavadocparser.resolving;


import static java.util.stream.Collectors.toList;

import de.ialistannen.htmljavadocparser.model.JavadocPackage;
import de.ialistannen.htmljavadocparser.model.generic.GenericTypeProxy;
import de.ialistannen.htmljavadocparser.model.types.ArrayType;
import de.ialistannen.htmljavadocparser.model.types.PrimitiveType;
import de.ialistannen.htmljavadocparser.model.types.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * The main javadoc index.
 */
public class Index {

  private Map<String, Type> types;
  private Map<String, JavadocPackage> packages;

  /**
   * Creates a new index.
   */
  public Index() {
    this.types = new HashMap<>();
    this.packages = new HashMap<>();

    addTypes(Arrays.asList(PrimitiveType.values()));
  }

  /**
   * Adds the given types.
   *
   * @param types the types
   */
  public void addTypes(Iterable<? extends Type> types) {
    for (Type type : types) {
      this.types.put(type.getFullyQualifiedName(), type);
    }
  }

  /**
   * Adds the given packages.
   *
   * @param packages the package
   */
  public void addPackages(Iterable<? extends JavadocPackage> packages) {
    for (JavadocPackage javadocPackage : packages) {
      this.packages.put(javadocPackage.getFullyQualifiedName(), javadocPackage);
    }
  }

  /**
   * Finds a name by its fully qualified name.
   *
   * @param fullyQualifiedName the fully qualified type name
   * @return the found type
   */
  public Optional<Type> getTypeForFullName(String fullyQualifiedName) {
    Type type = this.types.get(fullyQualifiedName);
    if (type == null) {
      String wrappedName = removeArraySyntax(fullyQualifiedName);
      Type wrapped = this.types.get(wrappedName);

      if (wrapped == null) {
        return tryGetGenericProxy(fullyQualifiedName);
      }

      return Optional.of(new ArrayType(wrapped));
    }
    return Optional.of(type);
  }

  private Optional<Type> tryGetGenericProxy(String name) {
    if (name.contains(".")) {
      return Optional.empty();
    }
    return Optional.of(new GenericTypeProxy(name, name));
  }

  /**
   * Finds a name by its fully qualified name.
   *
   * @param fullyQualifiedName the fully qualified type name
   * @return the found type
   * @throws java.util.NoSuchElementException if the class was not found
   */
  public Type getTypeForFullNameOrError(String fullyQualifiedName) {
    return getTypeForFullName(fullyQualifiedName).orElseThrow(
        () -> new NoSuchElementException("Could not find type " + fullyQualifiedName)
    );
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
  public List<Type> findWithSimpleName(String name) {
    return findMatching(type -> type.getSimpleName().equals(name));
  }

  /**
   * Returns the package with the given name.
   *
   * @param name the name of the package
   * @return the package
   */
  public Optional<JavadocPackage> getPackage(String name) {
    return Optional.ofNullable(packages.get(name));
  }

  /**
   * Returns the package with the given name.
   *
   * @param name the name of the package
   * @return the package
   */
  public JavadocPackage getPackageOrError(String name) {
    return getPackage(name).orElseThrow(
        () -> new NoSuchElementException("Could not find package " + name)
    );
  }

  private String removeArraySyntax(String name) {
    return name.replace("[]", "")
        .replace("...", "");
  }
}
