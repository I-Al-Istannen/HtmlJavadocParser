package de.ialistannen.htmljavadocparser.parsing;

import de.ialistannen.htmljavadocparser.impl.JField;
import de.ialistannen.htmljavadocparser.model.properties.Nameable;
import de.ialistannen.htmljavadocparser.resolving.DocumentResolver;
import de.ialistannen.htmljavadocparser.resolving.Index;
import de.ialistannen.htmljavadocparser.util.LinkUtils;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.nodes.Element;

/**
 * A parser for javadoc enums.
 */
public class JEnumParser extends JClassParser {

  public JEnumParser(String url, DocumentResolver resolver) {
    super(url, resolver);
  }

  public List<Nameable> parseEnumConstants(Index index) {
    List<Nameable> fields = new ArrayList<>();

    Element table = document().getElementById("enum.constant.summary").parent();
    for (Element wrapping : table.getElementsByClass("memberNameLink")) {
      Element link = wrapping.child(0);

      // enum constants are basically fields
      JFieldParser fieldParser = new JFieldParser(resolver(), link.absUrl("href"));
      String fqn = LinkUtils.linkToFqn(link.attr("href"));
      JField field = new JField(fqn, fieldParser, index);

      fields.add(field);
    }

    return fields;
  }
}
