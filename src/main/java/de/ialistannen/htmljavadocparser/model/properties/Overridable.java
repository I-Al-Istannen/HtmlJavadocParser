package de.ialistannen.htmljavadocparser.model.properties;

/**
 * An object that can be overridden.
 */
public interface Overridable {

  /**
   * Returns the control modifier for overridable objects.
   *
   * @return the control modifier
   */
  ControlModifier getOverrideControlModifier();

  /**
   * The different modifiers that control whether the object can be override.
   */
  enum ControlModifier {
    FINAL,
    ABSTRACT
  }
}
