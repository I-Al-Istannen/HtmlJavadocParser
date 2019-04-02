package de.ialistannen.htmljavadocparser.parsing;

import static de.ialistannen.htmljavadocparser.util.LinkUtils.linkToFqn;

import de.ialistannen.htmljavadocparser.resolving.DocumentResolver;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.jsoup.nodes.Element;

/**
 * A parser for javadoc interfaces.
 */
public class JInterfaceParser extends JClassParser {

  public JInterfaceParser(String url, DocumentResolver resolver) {
    super(url, resolver);
  }

  @Override
  public List<String> parseSuperInterfaces() {
    // The text for interfaces is different as interfaces *extend* other interfaces

    Element dd = document().getElementsContainingOwnText("All Superinterfaces:")
        .first();

    if (dd == null) {
      return Collections.emptyList();
    }

    dd = dd.nextElementSibling();

    return dd.children().stream()
        .map(code -> linkToFqn(code.child(0).attr("href")))
        .collect(Collectors.toList());
  }
}
