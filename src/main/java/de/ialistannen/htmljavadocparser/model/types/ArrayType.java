package de.ialistannen.htmljavadocparser.model.types;

import de.ialistannen.htmljavadocparser.model.JavadocPackage;
import de.ialistannen.htmljavadocparser.model.doc.JavadocComment;
import de.ialistannen.htmljavadocparser.model.properties.Invocable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * An array type of some wrapped type.
 */
public class ArrayType implements Type {

  private Type componentType;

  /**
   * Creates a new array type.
   *
   * @param componentType the underlying type
   */
  public ArrayType(Type componentType) {
    this.componentType = componentType;
  }

  /**
   * Returns the wrapped type.
   *
   * @return the wrapped type
   */
  public Type getComponentType() {
    return componentType;
  }


  @Override
  public String getDeclaration() {
    return getSimpleName();
  }

  @Override
  public Optional<Type> getSuperClass() {
    return componentType.getSuperClass().map(ArrayType::new);
  }

  @Override
  public List<JavadocInterface> getSuperInterfaces() {
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
    return componentType.getSimpleName() + "[]";
  }

  @Override
  public String getFullyQualifiedName() {
    return componentType.getFullyQualifiedName() + "[]";
  }

  @Override
  public JavadocPackage getPackage() {
    return componentType.getPackage();
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
  public String getUrl() {
    return componentType.getUrl();
  }

  @Override
  public String toString() {
    return "ArrayType{" + componentType + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ArrayType arrayType = (ArrayType) o;
    return Objects.equals(componentType, arrayType.componentType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(componentType);
  }
}
