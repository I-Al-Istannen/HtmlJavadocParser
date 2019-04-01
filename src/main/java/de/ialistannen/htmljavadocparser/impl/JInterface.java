package de.ialistannen.htmljavadocparser.impl;

import de.ialistannen.htmljavadocparser.model.properties.Invocable;
import de.ialistannen.htmljavadocparser.model.types.JavadocInterface;
import de.ialistannen.htmljavadocparser.parsing.JInterfaceParser;
import de.ialistannen.htmljavadocparser.resolving.Index;
import java.util.Collections;
import java.util.List;

/**
 * A javadoc interface.
 */
public class JInterface extends JClass implements JavadocInterface {

  public JInterface(String fullyQualifiedName, Index index, JInterfaceParser parser) {
    super(fullyQualifiedName, index, parser);
  }

  @Override
  public List<Invocable> getConstructors() {
    return Collections.emptyList();
  }

  @Override
  public String toString() {
    return "JInterface{" + getFullyQualifiedName() + "}";
  }
}
