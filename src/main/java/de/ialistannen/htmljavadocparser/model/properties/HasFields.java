package de.ialistannen.htmljavadocparser.model.properties;

import de.ialistannen.htmljavadocparser.model.JavadocField;
import java.util.List;

/**
 * An object that may have fields.
 */
public interface HasFields {

  /**
   * Returns all fields.
   *
   * @return all fields
   */
  List<JavadocField> getFields();
}
