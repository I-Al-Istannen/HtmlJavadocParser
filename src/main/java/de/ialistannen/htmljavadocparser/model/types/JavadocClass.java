package de.ialistannen.htmljavadocparser.model.types;

import de.ialistannen.htmljavadocparser.model.properties.GenericTarget;
import de.ialistannen.htmljavadocparser.model.properties.HasFields;
import de.ialistannen.htmljavadocparser.model.properties.Invocable;
import de.ialistannen.htmljavadocparser.model.properties.Overridable;
import java.util.List;

/**
 * A class.
 */
public interface JavadocClass extends Type, Overridable, HasFields, GenericTarget {

  /**
   * Returns all class constructors.
   *
   * @return the constructors
   */
  List<Invocable> getConstructors();

  /**
   * Returns whether this is a static class.
   *
   * @return true if this is a static class
   */
  boolean isStatic();
}
