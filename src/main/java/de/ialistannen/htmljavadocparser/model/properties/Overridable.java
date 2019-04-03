package de.ialistannen.htmljavadocparser.model.properties;

import java.util.Collection;

/**
 * An object that can be overridden (e.g. a method or class).
 */
public interface Overridable {

  /**
   * Returns the control modifier for overridable objects.
   *
   * @return the control modifier
   */
  Collection<ControlModifier> getOverrideControlModifier();

  /**
   * Returns true if the element is final.
   *
   * @return true if the element is final
   */
  default boolean isFinal() {
    return getOverrideControlModifier().contains(ControlModifier.FINAL);
  }

  /**
   * Returns true if the element is abstract.
   *
   * @return true if the element is abstract
   */
  default boolean isAbstract() {
    return getOverrideControlModifier().contains(ControlModifier.ABSTRACT);
  }

  /**
   * The different modifiers that control whether the object can be override.
   */
  enum ControlModifier {
    FINAL("final"),
    ABSTRACT("abstract");

    private String name;

    ControlModifier(String name) {
      this.name = name;
    }

    public String getName() {
      return name;
    }
  }
}
