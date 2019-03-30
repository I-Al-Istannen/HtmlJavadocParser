package de.ialistannen.htmljavadocparser.model.doc;

import java.util.List;

/**
 * A html formatted tag.
 */
public class HtmlTag extends JavadocComment {

  private String html;

  public HtmlTag(String html, List<JavadocComment> children) {
    super(children);
    this.html = html;
  }

  public String getHtml() {
    return html;
  }
}
