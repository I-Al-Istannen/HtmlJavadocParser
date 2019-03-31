package de.ialistannen.htmljavadocparser.impl;

import de.ialistannen.htmljavadocparser.model.JavadocField;
import de.ialistannen.htmljavadocparser.model.JavadocPackage;
import de.ialistannen.htmljavadocparser.model.doc.JavadocComment;
import de.ialistannen.htmljavadocparser.model.types.Type;
import de.ialistannen.htmljavadocparser.parsing.JFieldParser;
import de.ialistannen.htmljavadocparser.resolving.Index;
import java.util.Collection;
import java.util.Optional;

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
    return Optional.empty();
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
}
