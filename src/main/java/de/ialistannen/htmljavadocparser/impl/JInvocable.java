package de.ialistannen.htmljavadocparser.impl;

import de.ialistannen.htmljavadocparser.model.JavadocPackage;
import de.ialistannen.htmljavadocparser.model.doc.JavadocComment;
import de.ialistannen.htmljavadocparser.model.properties.Invocable;
import de.ialistannen.htmljavadocparser.model.types.JavadocAnnotation;
import de.ialistannen.htmljavadocparser.model.types.Type;
import de.ialistannen.htmljavadocparser.parsing.JInvocableParser;
import de.ialistannen.htmljavadocparser.resolving.Index;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class JInvocable implements Invocable {

  private Index index;
  private JInvocableParser parser;
  private String fullyQualifiedName;

  public JInvocable(String fullyQualifiedName, Index index, JInvocableParser parser) {
    this.index = index;
    this.parser = parser;
    this.fullyQualifiedName = fullyQualifiedName;
  }

  @Override
  public String getDeclaration() {
    return parser.parseDeclaration();
  }

  @Override
  public List<Parameter> getParameters() {
    List<Parameter> parameters = new ArrayList<>();

    Map<String, String> parsedParams = parser.parseParameters();
    for (Entry<String, String> entry : parsedParams.entrySet()) {
      parameters.add(new Parameter(
          index.getTypeForFullNameOrError(entry.getValue()),
          entry.getKey()
      ));
    }

    return parameters;
  }

  @Override
  public Type getReturnType() {
    return index.getTypeForFullNameOrError(parser.parseReturnType());
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
  public ControlModifier getOverrideControlModifier() {
    return parser.parseOverrideModifier();
  }

  @Override
  public Type getDeclaredOwner() {
    return index.getTypeForFullNameOrError(parser.parseActualOwner());
  }

  @Override
  public Type getOriginalOwner() {
    String originalOwner = parser.parseOriginalOwner();
    return originalOwner == null
        ? getDeclaredOwner()
        : index.getTypeForFullNameOrError(originalOwner);
  }

  @Override
  public List<JavadocAnnotation> getAnnotations() {
    return parser.parseAnnotations().stream()
        .map(index::getTypeForFullNameOrError)
        .filter(type -> type instanceof JavadocAnnotation)
        .map(type -> (JavadocAnnotation) type)
        .collect(Collectors.toList());
  }

  @Override
  public List<Type> getThrows() {
    return parser.parseThrows().stream()
        .map(index::getTypeForFullNameOrError)
        .collect(Collectors.toList());
  }

  @Override
  public String toString() {
    return "JInvocable{" + fullyQualifiedName + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    JInvocable that = (JInvocable) o;
    return Objects.equals(fullyQualifiedName, that.fullyQualifiedName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(fullyQualifiedName);
  }
}
