package de.ialistannen.htmljavadocparser.model;

import de.ialistannen.htmljavadocparser.model.properties.Nameable;
import de.ialistannen.htmljavadocparser.model.types.Type;
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
