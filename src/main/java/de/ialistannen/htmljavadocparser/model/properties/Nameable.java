package de.ialistannen.htmljavadocparser.model.properties;

import de.ialistannen.htmljavadocparser.model.JavadocPackage;
import de.ialistannen.htmljavadocparser.model.doc.JavadocComment;
import java.util.Optional;

/**
 * A field, method, type, constructor or other construct that can be named.
 */
public interface Nameable extends HasVisibility {

  /**
   * Returns the simple name.
   *
   * @return the simple name
   */
  String getSimpleName();

  /**
   * Returns the fully qualified name.
   *
   * @return the fully qualified name
   * @implNote The default implementation just appends the simple name to the package name
   */
  default String getFullyQualifiedName() {
    return getPackage().getFullyQualifiedName() + "." + getSimpleName();
  }

  /**
   * Returns the package.
   *
   * @return the package
   */
  JavadocPackage getPackage();

  /**
   * Returns the javadoc for this type.
   *
   * @return the javadoc for this type
   */
  Optional<JavadocComment> getJavadoc();
}
