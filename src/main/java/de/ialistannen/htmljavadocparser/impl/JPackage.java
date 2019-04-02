package de.ialistannen.htmljavadocparser.impl;

import de.ialistannen.htmljavadocparser.model.JavadocPackage;
import de.ialistannen.htmljavadocparser.model.doc.JavadocComment;
import de.ialistannen.htmljavadocparser.model.types.Type;
import de.ialistannen.htmljavadocparser.parsing.JPackageParser;
import de.ialistannen.htmljavadocparser.parsing.doc.JavadocCommentParser;
import de.ialistannen.htmljavadocparser.resolving.Index;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class JPackage implements JavadocPackage {

  private JPackageParser parser;
  private String fullyQualifiedName;
  private Index index;

  public JPackage(JPackageParser parser, String fullyQualifiedName, Index index) {
    this.parser = parser;
    this.fullyQualifiedName = fullyQualifiedName;
    this.index = index;
  }

  @Override
  public List<Type> getContainedTypes() {
    return parser.parseTypes().stream()
        .map(index::getTypeForFullNameOrError)
        .collect(Collectors.toList());
  }

  @Override
  public String getSimpleName() {
    return fullyQualifiedName.contains(".")
        ? fullyQualifiedName.substring(fullyQualifiedName.lastIndexOf('.') + 1)
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
    return new JavadocCommentParser().parse(parser.parseJavadoc());
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
