package de.ialistannen.htmljavadocparser.model.properties;

import de.ialistannen.htmljavadocparser.model.generic.GenericType;
import java.util.List;

/**
 * An object that may have generic parameters.
 */
public interface GenericTarget {

  /**
   * Returns the generic types for this class.
   *
   * @return the generic types for the class
   */
  List<GenericType> getGenericTypes();

}
