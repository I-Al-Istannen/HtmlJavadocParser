package de.ialistannen.htmljavadocparser.model.properties;

/**
 * An object that has a visibility or access restriction.
 */
public interface HasVisibility {

  /**
   * Returns the visibility level.
   *
   * @return the visibility level
   */
  VisibilityLevel getVisibility();

  /**
   * The visibility level.
   */
  enum VisibilityLevel {
    PRIVATE,
    PACKAGE_PRIVATE,
    PROTECTED,
    PUBLIC
  }
}
