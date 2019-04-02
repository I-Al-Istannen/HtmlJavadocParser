package de.ialistannen.htmljavadocparser.model.doc;

import java.util.Collections;
import org.jsoup.nodes.Element;

/**
 * A html formatted tag.
 */
public class HtmlTag extends JavadocComment {

  private Element html;

  public HtmlTag(Element html) {
    super(Collections.emptyList());
    this.html = html;
  }

  public Element getHtml() {
    return html;
  }

  @Override
  public String toString() {
    String htmlString = html.text();
    String trimmed = htmlString.substring(0, Math.min(htmlString.length(), 200));

    if (htmlString.length() > trimmed.length()) {
      trimmed = trimmed + "...";
    }

    return "HtmlTag{" + trimmed + '}';
  }
}
