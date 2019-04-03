package de.ialistannen.htmljavadocparser.model.doc;

/**
 * A visitor for Javadoc comments.
 */
public interface JavadocCommentVisitor {

  /**
   * Visits a block tag.
   *
   * @param tag the block tag
   */
  void visitBlockTag(BlockTag tag);

  /**
   * Visits a html tag.
   *
   * @param tag a html tag
   */
  void visitHtmlTag(HtmlTag tag);
}
