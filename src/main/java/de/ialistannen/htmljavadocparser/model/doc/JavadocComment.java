package de.ialistannen.htmljavadocparser.model.doc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A javadoc comment.
 */
public class JavadocComment {

  private List<JavadocComment> children;

  public JavadocComment(List<JavadocComment> children) {
    this.children = new ArrayList<>(children);
  }

  /**
   * Returns the children.
   *
   * @return the children
   */
  public List<JavadocComment> getChildren() {
    return Collections.unmodifiableList(children);
  }

  /**
   * Accepts the given visitor.
   *
   * @param visitor the visitor
   */
  public void acceot(JavadocCommentVisitor visitor) {
    children.forEach(comment -> comment.acceot(visitor));
  }

  @Override
  public String toString() {
    return "JavadocComment{" + children + '}';
  }
}
