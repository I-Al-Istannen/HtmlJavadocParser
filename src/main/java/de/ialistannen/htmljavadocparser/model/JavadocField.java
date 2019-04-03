package de.ialistannen.htmljavadocparser.model;

import de.ialistannen.htmljavadocparser.model.properties.HasVisibility;
import de.ialistannen.htmljavadocparser.model.properties.JavadocElement;
import de.ialistannen.htmljavadocparser.model.properties.Overridable;
import de.ialistannen.htmljavadocparser.model.properties.Ownable;
import de.ialistannen.htmljavadocparser.model.types.Type;

/**
 * A javadoc field.
 */
public interface JavadocField extends JavadocElement, Overridable, Ownable, HasVisibility {

  /**
   * Returns the type of the field.
   *
   * @return the type of the field
   */
  Type getType();

  /**
   * Returns whether this field is static.
   *
   * @return true if this field is static.
   */
  boolean isStatic();
}
