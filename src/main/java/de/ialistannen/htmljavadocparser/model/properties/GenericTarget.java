package de.ialistannen.htmljavadocparser.model.properties;

import de.ialistannen.htmljavadocparser.model.generic.GenericType;
import java.util.List;

public interface GenericTarget {

  /**
   * Returns the generic types for this class.
   *
   * @return the generic types for the class
   */
  List<GenericType> getGenericTypes();

}
