package de.ialistannen.htmljavadocparser.model.properties;

import de.ialistannen.htmljavadocparser.model.JavadocField;
import java.util.List;

/**
 * Marks an element as having fields.
 */
public interface HasFields {

  /**
   * Returns all fields.
   *
   * @return all fields
   */
  List<JavadocField> getFields();
}
