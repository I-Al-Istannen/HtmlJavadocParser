package de.ialistannen.htmljavadocparser.parsing.doc;

import de.ialistannen.htmljavadocparser.model.doc.BlockTag;
import de.ialistannen.htmljavadocparser.model.doc.HtmlTag;
import de.ialistannen.htmljavadocparser.model.doc.JavadocComment;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.jsoup.nodes.Element;

/**
 * A parser for {@link JavadocComment}s.
 */
public class JavadocCommentParser {

  /**
   * Parses a list of elements to a javadoc comment.
   *
   * @param elements the elements to parse
   * @return the parsed comment or an empty optional if it had no children
   */
  public Optional<JavadocComment> parse(Collection<Element> elements) {
    List<JavadocComment> children = new ArrayList<>();
    for (Element element : elements) {
      if (isBlockTagList(element)) {
        children.addAll(parseBlockTagList(element));
      } else {
        children.add(new HtmlTag(element));
      }
    }

    if (children.isEmpty()) {
      return Optional.empty();
    }

    return Optional.of(new JavadocComment(children));
  }

  private boolean isBlockTagList(Element element) {
    return element.tagName().equals("dl");
  }

  private List<JavadocComment> parseBlockTagList(Element list) {
    return list.children().stream()
        .filter(element -> element.tagName().equals("dt"))
        .map(this::parseBlockTag)
        .collect(Collectors.toList());
  }

  private BlockTag parseBlockTag(Element dt) {
    Element name = dt.child(0);
    Element dd = dt.nextElementSibling();

    return new BlockTag(new HtmlTag(name), new HtmlTag(dd));
  }
}
