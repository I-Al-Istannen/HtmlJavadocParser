package de.ialistannen.htmljavadocparser.model.properties;

import de.ialistannen.htmljavadocparser.model.types.JavadocAnnotation;
import java.util.List;

/**
 * The target of annotations.
 */
public interface AnnotationTarget {

  /**
   * Returns all annotations.
   *
   * @return all annotations
   */
  List<JavadocAnnotation> getAnnotations();
}
