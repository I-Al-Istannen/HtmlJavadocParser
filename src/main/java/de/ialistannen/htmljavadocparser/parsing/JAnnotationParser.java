package de.ialistannen.htmljavadocparser.parsing;

import de.ialistannen.htmljavadocparser.impl.JAnnotationMethod;
import de.ialistannen.htmljavadocparser.model.properties.Invocable;
import de.ialistannen.htmljavadocparser.resolving.DocumentResolver;
import de.ialistannen.htmljavadocparser.resolving.Index;
import de.ialistannen.htmljavadocparser.util.LinkUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.jsoup.nodes.Element;

/**
 * A parser for annotations.
 */
public class JAnnotationParser extends JTypeParser {

  public JAnnotationParser(String url, DocumentResolver resolver) {
    super(url, resolver);
  }

  @Override
  protected Element getTypeDeclarationPre() {
    return document().getElementsByClass("memberNameLabel").first().parent();
  }

  @Override
  public String parseSimpleName() {
    return document().getElementsByClass("memberNameLabel").first().text();
  }

  @Override
  public List<Invocable> parseMethods(Index index) {
    // methods in annotations are identified by different keys and have a slightly different
    // structure

    Element anchor = document().getElementById("annotation.type.required.element.summary");

    if (anchor == null) {
      return Collections.emptyList();
    }
    Element wrapper = anchor.parent();

    List<Invocable> methods = new ArrayList<>();

    for (Element linkSpan : wrapper.getElementsByClass("memberNameLink")) {
      Element link = linkSpan.child(0);

      JAnnotationMethodParser parser = new JAnnotationMethodParser(link.absUrl("href"), resolver());
      JAnnotationMethod method = new JAnnotationMethod(
          LinkUtils.linkToFqn(resolver().relativizeAbsoluteUrl(link)),
          index,
          parser
      );

      methods.add(method);
    }
    return methods;
  }
}
