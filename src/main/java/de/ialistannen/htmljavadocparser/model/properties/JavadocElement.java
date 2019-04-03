package de.ialistannen.htmljavadocparser.model.properties;

import de.ialistannen.htmljavadocparser.model.JavadocPackage;
import de.ialistannen.htmljavadocparser.model.doc.JavadocComment;
import java.util.Optional;

/**
 * The base for all javadoc elements.
 */
public interface JavadocElement {

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
   */
  String getFullyQualifiedName();

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

  /**
   * Returns the field declaration.
   *
   * @return the field declaration
   */
  String getDeclaration();

  /**
   * Returns the url to this element.
   *
   * @return the url to this element
   */
  String getUrl();
}
