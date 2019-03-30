package de.ialistannen.htmljavadocparser.impl;

import de.ialistannen.htmljavadocparser.model.GenericType;
import de.ialistannen.htmljavadocparser.model.JavadocPackage;
import de.ialistannen.htmljavadocparser.model.doc.JavadocComment;
import de.ialistannen.htmljavadocparser.model.properties.Invocable;
import de.ialistannen.htmljavadocparser.model.types.Type;
import java.util.List;
import java.util.Optional;

public class JInvocable implements Invocable {

  @Override
  public String getDeclaration() {
    return null;
  }

  @Override
  public List<Parameter> getParameters() {
    return null;
  }

  @Override
  public Type getReturnType() {
    return null;
  }

  @Override
  public List<GenericType> getGenericTypes() {
    return null;
  }

  @Override
  public DeprecationStatus getDeprecationStatus() {
    return null;
  }

  @Override
  public String getSimpleName() {
    return null;
  }

  @Override
  public String getFullyQualifiedName() {
    return null;
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
    return null;
  }

  @Override
  public ControlModifier getOverrideControlModifier() {
    return null;
  }

  @Override
  public Type getDeclaredOwner() {
    return null;
  }

  @Override
  public Type getOriginalOwner() {
    return null;
  }
}
