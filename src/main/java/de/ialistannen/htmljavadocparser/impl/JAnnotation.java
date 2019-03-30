package de.ialistannen.htmljavadocparser.impl;

import de.ialistannen.htmljavadocparser.model.JavadocPackage;
import de.ialistannen.htmljavadocparser.model.doc.JavadocComment;
import de.ialistannen.htmljavadocparser.model.properties.Invocable;
import de.ialistannen.htmljavadocparser.model.types.JavadocAnnotation;
import de.ialistannen.htmljavadocparser.model.types.Type;
import de.ialistannen.htmljavadocparser.parsing.JTypeParser;
import de.ialistannen.htmljavadocparser.resolving.Index;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class JAnnotation implements JavadocAnnotation {

  private String fullyQualifiedName;
  private JTypeParser parser;
  private Index index;

  public JAnnotation(String fullyQualifiedName, JTypeParser parser, Index index) {
    this.fullyQualifiedName = fullyQualifiedName;
    this.parser = parser;
    this.index = index;
  }

  @Override
  public String getDeclaration() {
    return parser.parseDeclaration();
  }

  @Override
  public Optional<Type> getSuperClass() {
    return parser.parseSuperClass()
        .map(index::getTypeForFullNameOrError);
  }

  @Override
  public List<Type> getSuperInterfaces() {
    return Collections.emptyList();
  }

  @Override
  public List<Invocable> getMethods() {
    return null;
  }

  @Override
  public List<JavadocAnnotation> getAnnotations() {
    return null;
  }

  @Override
  public DeprecationStatus getDeprecationStatus() {
    return parser.parseDeprecationStatus();
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
    return Optional.empty();
  }

  @Override
  public VisibilityLevel getVisibility() {
    return parser.parseVisibilityLevel();
  }

  @Override
  public Type getDeclaredOwner() {
    String simpleName = getSimpleName();
    if (simpleName.contains(".")) {
      String packageName = getPackage().getFullyQualifiedName();
      String outerFqn = packageName + "." + simpleName.split("\\.")[0];

      return index.getTypeForFullNameOrError(outerFqn);
    }
    return this;
  }

  @Override
  public Type getOriginalOwner() {
    return getDeclaredOwner();
  }

  @Override
  public String toString() {
    return "JAnnotation{" + fullyQualifiedName + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    JAnnotation that = (JAnnotation) o;
    return Objects.equals(fullyQualifiedName, that.fullyQualifiedName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(fullyQualifiedName);
  }
}
