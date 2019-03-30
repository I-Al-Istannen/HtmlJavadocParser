package de.ialistannen.htmljavadocparser.model;

import de.ialistannen.htmljavadocparser.model.GenericType.Bound.Type;
import de.ialistannen.htmljavadocparser.model.properties.Nameable;
import java.util.List;

/**
 * A javadoc package.
 */
public interface JavadocPackage extends Nameable {

  /**
   * Returns all contained types.
   *
   * @return the contained types
   */
  List<Type> getContainedTypes();
}
