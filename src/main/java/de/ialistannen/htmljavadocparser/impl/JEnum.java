package de.ialistannen.htmljavadocparser.impl;

import de.ialistannen.htmljavadocparser.model.properties.JavadocElement;
import de.ialistannen.htmljavadocparser.model.types.JavadocEnum;
import de.ialistannen.htmljavadocparser.model.types.Type;
import de.ialistannen.htmljavadocparser.parsing.JEnumParser;
import de.ialistannen.htmljavadocparser.resolving.Index;
import java.util.List;
import java.util.Optional;

/**
 * An implementation of a javadoc enum.
 */
public class JEnum extends JClass implements JavadocEnum {

  private final JEnumParser parser;

  public JEnum(String fullyQualifiedName, String simpleName, Index index, JEnumParser parser) {
    super(fullyQualifiedName, simpleName, index, parser);
    this.parser = parser;
  }

  @Override
  public List<JavadocElement> getConstants() {
    return parser.parseEnumConstants(index);
  }

  @Override
  public Optional<Type> getSuperClass() {
    // All enums implicitly extend Enum
    return index.getTypeForFullName("java.lang.Enum");
  }

  @Override
  public String toString() {
    return "JEnum{" + getFullyQualifiedName() + "}";
  }
}
