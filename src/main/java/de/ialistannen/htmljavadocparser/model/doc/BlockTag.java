package de.ialistannen.htmljavadocparser.model.doc;

import java.util.List;

/**
 * A block tag like @author or @throws.
 */
public class BlockTag extends JavadocComment {

  private final String name;
  private final String value;

  public BlockTag(String name, String value, List<JavadocComment> children) {
    super(children);
    this.name = name;
    this.value = value;
  }

  public String getName() {
    return name;
  }

  public String getValue() {
    return value;
  }
}
