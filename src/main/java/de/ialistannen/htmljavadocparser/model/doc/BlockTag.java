package de.ialistannen.htmljavadocparser.model.doc;

import java.util.Collections;

/**
 * A block tag like @author or @throws.
 */
public class BlockTag extends JavadocComment {

  private final HtmlTag name;
  private final HtmlTag value;

  public BlockTag(HtmlTag name, HtmlTag value) {
    super(Collections.emptyList());
    this.name = name;
    this.value = value;
  }

  public HtmlTag getName() {
    return name;
  }

  public HtmlTag getValue() {
    return value;
  }

  @Override
  public String toString() {
    return "BlockTag{" + name + ": " + value + '}';
  }
}
