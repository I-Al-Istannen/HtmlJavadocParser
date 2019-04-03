package de.ialistannen.htmljavadocparser.model.types;

import de.ialistannen.htmljavadocparser.model.properties.GenericTarget;
import de.ialistannen.htmljavadocparser.model.properties.JavadocElement;
import java.util.List;

/**
 * A java enum.
 */
public interface JavadocEnum extends Type, GenericTarget {

  /**
   * Returns all enum constants.
   *
   * @return the enum constants
   */
  List<JavadocElement> getConstants();
}
