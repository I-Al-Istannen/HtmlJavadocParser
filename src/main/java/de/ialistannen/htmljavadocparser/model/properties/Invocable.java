package de.ialistannen.htmljavadocparser.model.properties;

import de.ialistannen.htmljavadocparser.model.types.Type;
import java.util.List;
import java.util.Objects;

/**
 * A method or constructor.
 */
public interface Invocable extends Nameable, Overridable, Deprecatable, Ownable, AnnotationTarget {

  String getDeclaration();

  /**
   * Returns the parameters.
   *
   * @return the parameters
   */
  List<Parameter> getParameters();

  /**
   * Returns the return type.
   *
   * @return the return type
   */
  Type getReturnType();

  /**
   * Returns all (checked) exceptions the invocable may throw.
   *
   * @return all (checked) exceptions the invocable may throw.
   */
  List<Type> getThrows();

  /**
   * A method parameter.
   */
  class Parameter {

    private Type type;
    private String name;

    public Parameter(Type type, String name) {
      this.type = type;
      this.name = name;
    }

    public Type getType() {
      return type;
    }

    public String getName() {
      return name;
    }

    @Override
    public String toString() {
      return "Parameter{" + name + "=" + type + '}';
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      Parameter parameter = (Parameter) o;
      return Objects.equals(type, parameter.type) &&
          Objects.equals(name, parameter.name);
    }

    @Override
    public int hashCode() {
      return Objects.hash(type, name);
    }
  }
}
