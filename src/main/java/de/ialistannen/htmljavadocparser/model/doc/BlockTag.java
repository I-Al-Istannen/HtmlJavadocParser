package de.ialistannen.htmljavadocparser.model.doc;

import java.util.Collections;

/**
 * A block tag like @author or @throws.
 */
public class BlockTag extends JavadocComment {

  private final HtmlTag name;
  private final HtmlTag value;

  /**
   * Creates a new block tag.
   *
   * @param name the name of the tag
   * @param value the value of the tag
   */
  public BlockTag(HtmlTag name, HtmlTag value) {
    super(Collections.emptyList());
    this.name = name;
    this.value = value;
  }

  /**
   * Returns the name of the tag.
   *
   * @return the name of the tag
   */
  public HtmlTag getName() {
    return name;
  }

  /**
   * Returns the value of the tag.
   *
   * @return the value of the tag
   */
  public HtmlTag getValue() {
    return value;
  }

  @Override
  public String toString() {
    return "BlockTag{" + name + ": " + value + '}';
  }
}
