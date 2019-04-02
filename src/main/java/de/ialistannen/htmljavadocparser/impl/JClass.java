package de.ialistannen.htmljavadocparser.impl;

import de.ialistannen.htmljavadocparser.model.JavadocField;
import de.ialistannen.htmljavadocparser.model.generic.GenericType;
import de.ialistannen.htmljavadocparser.model.properties.Invocable;
import de.ialistannen.htmljavadocparser.model.types.JavadocClass;
import de.ialistannen.htmljavadocparser.parsing.JClassParser;
import de.ialistannen.htmljavadocparser.resolving.Index;
import java.util.Collection;
import java.util.List;

/**
 * An implementation of a javadoc class.
 */
public class JClass extends JType implements JavadocClass {

  private JClassParser parser;

  public JClass(String fullyQualifiedName, String simpleName, Index index, JClassParser parser) {
    super(fullyQualifiedName, simpleName, index, parser);
    this.parser = parser;
  }

  @Override
  public List<GenericType> getGenericTypes() {
    return parser.parseGenericTypes(index);
  }

  @Override
  public List<Invocable> getConstructors() {
    return parser.parseConstructors(index);
  }

  @Override
  public List<JavadocField> getFields() {
    return parser.parseFields(index);
  }

  @Override
  public Collection<ControlModifier> getOverrideControlModifier() {
    return parser.parseControlModifiers();
  }

  @Override
  public boolean isStatic() {
    return parser.parseIsStatic();
  }

  @Override
  public String toString() {
    return "JClass{" + getFullyQualifiedName() + '}';
  }
}
