package de.ialistannen.htmljavadocparser.impl;

import de.ialistannen.htmljavadocparser.model.properties.Nameable;
import de.ialistannen.htmljavadocparser.model.types.JavadocEnum;
import de.ialistannen.htmljavadocparser.model.types.Type;
import de.ialistannen.htmljavadocparser.parsing.JEnumParser;
import de.ialistannen.htmljavadocparser.resolving.Index;
import java.util.List;
import java.util.Optional;

public class JEnum extends JClass implements JavadocEnum {

  private final JEnumParser parser;

  public JEnum(String fullyQualifiedName, Index index, JEnumParser parser) {
    super(fullyQualifiedName, index, parser);
    this.parser = parser;
  }

  @Override
  public List<Nameable> getConstants() {
    return parser.parseEnumConstants(index);
  }

  @Override
  public Optional<Type> getSuperClass() {
    return index.getTypeForFullName("java.lang.Enum");
  }

  @Override
  public String toString() {
    return "JEnum{" + getFullyQualifiedName() + "}";
  }
}
