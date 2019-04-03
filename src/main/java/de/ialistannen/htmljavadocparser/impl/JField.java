package de.ialistannen.htmljavadocparser.impl;

import de.ialistannen.htmljavadocparser.model.JavadocField;
import de.ialistannen.htmljavadocparser.model.JavadocPackage;
import de.ialistannen.htmljavadocparser.model.doc.JavadocComment;
import de.ialistannen.htmljavadocparser.model.types.Type;
import de.ialistannen.htmljavadocparser.parsing.JFieldParser;
import de.ialistannen.htmljavadocparser.parsing.doc.JavadocCommentParser;
import de.ialistannen.htmljavadocparser.resolving.Index;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

/**
 * An implementation of a javadoc field.
 */
public class JField implements JavadocField {

  private String fullyQualifiedName;
  private JFieldParser parser;
  private Index index;

  public JField(String fullyQualifiedName, JFieldParser parser, Index index) {
    this.fullyQualifiedName = fullyQualifiedName;
    this.parser = parser;
    this.index = index;
  }

  @Override
  public String getDeclaration() {
    return parser.parseDeclaration();
  }

  @Override
  public String getSimpleName() {
    return parser.parseSimpleName();
  }

  @Override
  public String getFullyQualifiedName() {
    return fullyQualifiedName;
  }

  @Override
  public JavadocPackage getPackage() {
    return index.getPackageOrError(parser.parsePackage());
  }

  @Override
  public Optional<JavadocComment> getJavadoc() {
    return new JavadocCommentParser().parse(parser.parseJavadoc());
  }

  @Override
  public VisibilityLevel getVisibility() {
    return parser.parseVisibilityLevel();
  }

  @Override
  public Collection<ControlModifier> getOverrideControlModifier() {
    return parser.parseControlModifiers();
  }

  @Override
  public Type getDeclaredOwner() {
    return index.getTypeForFullNameOrError(parser.parseOwner());
  }

  @Override
  public Type getOriginalOwner() {
    return getDeclaredOwner();
  }

  @Override
  public boolean isStatic() {
    return parser.parseIsStatic();
  }

  @Override
  public Type getType() {
    return index.getTypeForFullNameOrError(parser.parseType());
  }

  @Override
  public String getUrl() {
    return parser.parseUrl();
  }

  @Override
  public String toString() {
    return "JField{" + fullyQualifiedName + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    JField jField = (JField) o;
    return Objects.equals(fullyQualifiedName, jField.fullyQualifiedName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(fullyQualifiedName);
  }
}
