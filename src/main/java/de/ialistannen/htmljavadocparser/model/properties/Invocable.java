package de.ialistannen.htmljavadocparser.model.properties;

import de.ialistannen.htmljavadocparser.model.GenericType;
import de.ialistannen.htmljavadocparser.model.types.Type;
import java.util.List;

/**
 * A method or constructor.
 */
public interface Invocable extends Nameable, Overridable, Deprecatable, Ownable {

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
   * Returns the generic types.
   *
   * @return the generic types
   */
  List<GenericType> getGenericTypes();


  /**
   * A method parameter.
   */
  class Parameter {

    private String type;
    private String name;

    public Parameter(String type, String name) {
      this.type = type;
      this.name = name;
    }

    public String getType() {
      return type;
    }

    public String getName() {
      return name;
    }
  }
}
