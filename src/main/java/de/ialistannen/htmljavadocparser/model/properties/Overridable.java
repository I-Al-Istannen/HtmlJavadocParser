package de.ialistannen.htmljavadocparser.model.properties;

import java.util.Collection;

/**
 * An object that can be overridden.
 */
public interface Overridable {

  /**
   * Returns the control modifier for overridable objects.
   *
   * @return the control modifier
   */
  Collection<ControlModifier> getOverrideControlModifier();

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
