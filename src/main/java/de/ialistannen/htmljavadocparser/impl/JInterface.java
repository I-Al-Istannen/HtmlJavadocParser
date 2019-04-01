package de.ialistannen.htmljavadocparser.impl;

import de.ialistannen.htmljavadocparser.model.types.JavadocInterface;
import de.ialistannen.htmljavadocparser.parsing.JClassParser;
import de.ialistannen.htmljavadocparser.resolving.Index;

/**
 * A javadoc interface.
 */
public class JInterface extends JClass implements JavadocInterface {

  public JInterface(String fullyQualifiedName, Index index, JClassParser parser) {
    super(fullyQualifiedName, index, parser);
  }

  @Override
  public String toString() {
    return "JInterface{" + getFullyQualifiedName() + "}";
  }
}
