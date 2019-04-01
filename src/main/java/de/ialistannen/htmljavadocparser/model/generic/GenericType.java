package de.ialistannen.htmljavadocparser.model.generic;

import de.ialistannen.htmljavadocparser.model.types.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * A generic type variable.
 */
public class GenericType {

  private Type type;
  private List<Bound> bounds;

  /**
   * Creates a new generic type.
   *
   * @param type the type of it
   * @param bounds the bounds of it
   */
  public GenericType(Type type, List<Bound> bounds) {
    this.type = type;
    this.bounds = new ArrayList<>(bounds);
  }

  /***
   * Returns the type.
   *
   * @return the type
   */
  public Type getType() {
    return type;
  }

  /**
   * Returns the bound.
   *
   * @return the bunds
   */
  public List<Bound> getBounds() {
    return Collections.unmodifiableList(bounds);
  }

  @Override
  public String toString() {
    String bounds = this.bounds.stream()
        .map(Objects::toString)
        .collect(Collectors.joining(", "));

    if (bounds.isEmpty()) {
      return "GenericType{" + type + '}';
    } else {
      return "GenericType{" + type + " " + bounds + '}';
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GenericType that = (GenericType) o;
    return Objects.equals(type, that.type) &&
        Objects.equals(bounds, that.bounds);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, bounds);
  }

  /**
   * A generic type bound.
   */
  public static class Bound {

    private GenericType type;
    private BindingType bindingType;

    public Bound(GenericType type, BindingType bindingType) {
      this.type = type;
      this.bindingType = bindingType;
    }

    /**
     * Returns the type in the bound (e.g. {@code String} for {@code T extends String}).
     *
     * @return the type
     */
    public GenericType getType() {
      return type;
    }

    /**
     * Returns the type of the binding.
     *
     * @return the type of the binding.
     */
    public BindingType getBindingType() {
      return bindingType;
    }

    @Override
    public String toString() {
      return bindingType.getName() + " " + type;
    }

    /**
     * The binding type.
     */
    public enum BindingType {
      /**
       * A contravariant binding.
       */
      SUPER("super"),
      /**
       * A covariant binding.
       */
      EXTENDS("extends"),
      /**
       * An exact invariant bound.
       */
      EXACT("exactly");

      private final String name;

      BindingType(String name) {
        this.name = name;
      }

      /**
       * Returns the name of the bound type.
       *
       * @return the name of the bound type
       */
      public String getName() {
        return name;
      }

      /**
       * Returns the type with the given name, falling back to {@link #EXACT}.
       *
       * @param name the name
       * @return the found type
       */
      public static BindingType forName(String name) {
        return Arrays.stream(values())
            .filter(it -> it.getName().equals(name))
            .findFirst()
            .orElse(EXACT);
      }
    }
  }
}
