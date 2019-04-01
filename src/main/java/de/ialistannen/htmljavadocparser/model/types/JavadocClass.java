package de.ialistannen.htmljavadocparser.model.types;

import de.ialistannen.htmljavadocparser.model.JavadocField;
import de.ialistannen.htmljavadocparser.model.generic.GenericType;
import de.ialistannen.htmljavadocparser.model.properties.Invocable;
import de.ialistannen.htmljavadocparser.model.properties.Overridable;
import java.util.List;

/**
 * A class.
 */
public interface JavadocClass extends Type, Overridable {

  /**
   * Returns the generic types for this class.
   *
   * @return the generic types for the class
   */
  List<GenericType> getGenericTypes();

  /**
   * Returns all class constructors.
   *
   * @return the constructors
   */
  List<Invocable> getConstructors();

  /**
   * Returns all fields.
   *
   * @return all fields
   */
  List<JavadocField> getFields();

  /**
   * Returns whether this is a static class.
   *
   * @return true if this is a static class
   */
  boolean isStatic();
}
