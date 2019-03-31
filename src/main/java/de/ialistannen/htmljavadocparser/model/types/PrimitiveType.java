package de.ialistannen.htmljavadocparser.model.types;

import de.ialistannen.htmljavadocparser.model.JavadocPackage;
import de.ialistannen.htmljavadocparser.model.doc.JavadocComment;
import de.ialistannen.htmljavadocparser.model.properties.Invocable;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * A java primitive type
 */
public enum PrimitiveType implements Type {
  BYTE("byte"),
  SHORT("short"),
  INT("int"),
  LONG("long"),
  FLOAT("float"),
  DOUBLE("double"),
  BOOLEAN("boolean"),
  VOID("void");

  private String name;

  PrimitiveType(String name) {
    this.name = name;
  }

  @Override
  public String getDeclaration() {
    return name;
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
}
