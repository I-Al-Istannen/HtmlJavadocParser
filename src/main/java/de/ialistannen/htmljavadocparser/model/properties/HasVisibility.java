package de.ialistannen.htmljavadocparser.model.properties;

/**
 * Marks the element as having a visibility.
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
