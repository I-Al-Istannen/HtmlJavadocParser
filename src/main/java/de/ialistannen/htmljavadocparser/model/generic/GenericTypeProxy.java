package de.ialistannen.htmljavadocparser.model.generic;

import de.ialistannen.htmljavadocparser.model.JavadocPackage;
import de.ialistannen.htmljavadocparser.model.doc.JavadocComment;
import de.ialistannen.htmljavadocparser.model.properties.Invocable;
import de.ialistannen.htmljavadocparser.model.types.JavadocAnnotation;
import de.ialistannen.htmljavadocparser.model.types.Type;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * A proxy type for a generic type variable.
 */
public class GenericTypeProxy implements Type {

  private String declaration;
  private String name;

  public GenericTypeProxy(String declaration, String name) {
    this.declaration = declaration;
    this.name = name;
  }

  @Override
  public String getDeclaration() {
    return declaration;
  }

  @Override
  public Optional<Type> getSuperClass() {
    return Optional.empty();
  }

  @Override
  public List<Type> getSuperInterfaces() {
    return Collections.emptyList();
  }

  @Override
  public List<Invocable> getMethods() {
    return Collections.emptyList();
  }

  @Override
  public List<JavadocAnnotation> getAnnotations() {
    return Collections.emptyList();
  }

  @Override
  public DeprecationStatus getDeprecationStatus() {
    return DeprecationStatus.NOT_DEPRECATED;
  }

  @Override
  public String getSimpleName() {
    return name;
  }

  @Override
  public String getFullyQualifiedName() {
    return name;
  }

  @Override
  public JavadocPackage getPackage() {
    return null;
  }

  @Override
  public Optional<JavadocComment> getJavadoc() {
    return Optional.empty();
  }

  @Override
  public VisibilityLevel getVisibility() {
    return VisibilityLevel.PUBLIC;
  }

  @Override
  public Type getDeclaredOwner() {
    return this;
  }

  @Override
  public Type getOriginalOwner() {
    return this;
  }

  @Override
  public String toString() {
    return name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GenericTypeProxy that = (GenericTypeProxy) o;
    return Objects.equals(declaration, that.declaration);
  }

  @Override
  public int hashCode() {
    return Objects.hash(declaration);
  }
}
