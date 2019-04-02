package de.ialistannen.htmljavadocparser.model.properties;

/**
 * An object that can be deprecated.
 */
public interface Deprecatable {

  /**
   * Returns the deprecation status.
   *
   * @return the deprecation status
   */
  DeprecationStatus getDeprecationStatus();

  /**
   * Returns true if this object is deprecated.
   *
   * @return true if this object is deprecated
   */
  default boolean isDeprecated() {
    return getDeprecationStatus() != DeprecationStatus.NOT_DEPRECATED;
  }

  /**
   * Returns the deprecation status.
   */
  enum DeprecationStatus {
    NOT_DEPRECATED,
    DEPRECATED,
    DEPRECATED_FOR_REMOVAL
  }
}
