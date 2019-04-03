package de.ialistannen.htmljavadocparser.impl;

import de.ialistannen.htmljavadocparser.model.JavadocPackage;
import de.ialistannen.htmljavadocparser.model.doc.JavadocComment;
import de.ialistannen.htmljavadocparser.model.generic.GenericType;
import de.ialistannen.htmljavadocparser.model.properties.Invocable;
import de.ialistannen.htmljavadocparser.model.types.JavadocAnnotation;
import de.ialistannen.htmljavadocparser.model.types.Type;
import de.ialistannen.htmljavadocparser.parsing.JAnnotationMethodParser;
import de.ialistannen.htmljavadocparser.resolving.Index;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * An implementation of a method in an annotation.
 */
public class JAnnotationMethod implements Invocable {

  private String fullyQualifiedName;
  private JAnnotationMethodParser parser;
  private Index index;

  public JAnnotationMethod(String fullyQualifiedName, Index index, JAnnotationMethodParser parser) {
    this.fullyQualifiedName = fullyQualifiedName;
    this.index = index;
    this.parser = parser;
  }

  @Override
  public String getDeclaration() {
    return parser.parseDeclaration();
  }

  @Override
  public List<Parameter> getParameters() {
    // we encode that as the return type
    return Collections.emptyList();
  }

  @Override
  public Type getReturnType() {
    return index.getTypeForFullNameOrError(parser.parseReturnType());
  }

  @Override
  public List<Type> getThrows() {
    return Collections.emptyList();
  }

  @Override
  public List<JavadocAnnotation> getAnnotations() {
    return parser.parseAnnotations().stream()
        .map(s -> (JavadocAnnotation) index.getTypeForFullNameOrError(s))
        .collect(Collectors.toList());
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
    // annotations have public members
    return VisibilityLevel.PUBLIC;
  }

  @Override
  public Collection<ControlModifier> getOverrideControlModifier() {
    // You can not extend annotations, so the methods might as well be final
    return List.of(ControlModifier.FINAL);
  }

  @Override
  public Type getDeclaredOwner() {
    return index.getTypeForFullNameOrError(parser.parseActualOwner());
  }

  @Override
  public Type getOriginalOwner() {
    return getDeclaredOwner();
  }

  @Override
  public boolean isStatic() {
    return false;
  }

  @Override
  public List<GenericType> getGenericTypes() {
    return Collections.emptyList();
  }

  @Override
  public String getUrl() {
    return parser.parseUrl();
  }

  @Override
  public String toString() {
    return "JAnnotationMethod{" + fullyQualifiedName + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    JAnnotationMethod method = (JAnnotationMethod) o;
    return Objects.equals(fullyQualifiedName, method.fullyQualifiedName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(fullyQualifiedName);
  }
}
