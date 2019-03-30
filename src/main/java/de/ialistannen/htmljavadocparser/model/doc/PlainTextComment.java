package de.ialistannen.htmljavadocparser.model.doc;

import java.util.Collections;

/**
 * A javadoc comment part without any formatting.
 */
public class PlainTextComment extends JavadocComment {

  private String text;

  public PlainTextComment(String text) {
    // can not contain anything
    super(Collections.emptyList());
    this.text = text;
  }

  public String getText() {
    return text;
  }
}
