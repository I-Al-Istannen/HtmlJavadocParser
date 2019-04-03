package de.ialistannen.htmljavadocparser.model.types;

import de.ialistannen.htmljavadocparser.model.properties.AnnotationTarget;
import de.ialistannen.htmljavadocparser.model.properties.Deprecatable;
import de.ialistannen.htmljavadocparser.model.properties.HasVisibility;
import de.ialistannen.htmljavadocparser.model.properties.Invocable;
import de.ialistannen.htmljavadocparser.model.properties.JavadocElement;
import de.ialistannen.htmljavadocparser.model.properties.Ownable;
import java.util.List;
import java.util.Optional;

/**
 * A java type.
 */
public interface Type extends JavadocElement, AnnotationTarget, Deprecatable, Ownable,
    HasVisibility {

  /**
   * Returns the superclass.
   *
   * @return the superclass
   */
  Optional<Type> getSuperClass();

  /**
   * Returns all super interfaces.
   *
   * @return all super interfaces
   */
  List<JavadocInterface> getSuperInterfaces();

  /**
   * Returns all methods in this type.
   *
   * @return all methods in this type
   */
  List<Invocable> getMethods();
}
