package de.ialistannen.htmljavadocparser.impl;

import de.ialistannen.htmljavadocparser.model.JavadocPackage;
import de.ialistannen.htmljavadocparser.model.doc.JavadocComment;
import de.ialistannen.htmljavadocparser.model.types.Type;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class JPackage implements JavadocPackage {

  private String fullyQualifiedName;

  public JPackage(String fullyQualifiedName) {
    this.fullyQualifiedName = fullyQualifiedName;
  }

  @Override
  public List<Type> getContainedTypes() {
    return null;
  }

  @Override
  public String getSimpleName() {
    return fullyQualifiedName.contains(".")
        ? fullyQualifiedName.substring(fullyQualifiedName.lastIndexOf('.'))
        : fullyQualifiedName;
  }

  @Override
  public String getFullyQualifiedName() {
    return fullyQualifiedName;
  }

  @Override
  public JavadocPackage getPackage() {
    return this;
  }

  @Override
  public Optional<JavadocComment> getJavadoc() {
    return Optional.empty();
  }

  @Override
  public VisibilityLevel getVisibility() {
    return null;
  }

  @Override
  public String toString() {
    return "JPackage{" + fullyQualifiedName + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    JPackage jPackage = (JPackage) o;
    return Objects.equals(fullyQualifiedName, jPackage.fullyQualifiedName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(fullyQualifiedName);
  }
}
