package de.ialistannen.htmljavadocparser.model;

import de.ialistannen.htmljavadocparser.model.properties.HasVisibility;
import de.ialistannen.htmljavadocparser.model.properties.Nameable;
import de.ialistannen.htmljavadocparser.model.properties.Overridable;
import de.ialistannen.htmljavadocparser.model.properties.Ownable;
import de.ialistannen.htmljavadocparser.model.types.Type;

public interface JavadocField extends Nameable, Overridable, Ownable, HasVisibility {

  /**
   * Returns the field declaration.
   *
   * @return the field declaration
   */
  String getDeclaration();

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
