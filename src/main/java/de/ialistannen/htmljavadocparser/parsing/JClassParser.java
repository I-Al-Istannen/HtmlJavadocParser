package de.ialistannen.htmljavadocparser.parsing;

import de.ialistannen.htmljavadocparser.impl.JField;
import de.ialistannen.htmljavadocparser.model.JavadocField;
import de.ialistannen.htmljavadocparser.model.generic.GenericType;
import de.ialistannen.htmljavadocparser.model.properties.Invocable;
import de.ialistannen.htmljavadocparser.model.properties.Overridable.ControlModifier;
import de.ialistannen.htmljavadocparser.resolving.DocumentResolver;
import de.ialistannen.htmljavadocparser.resolving.Index;
import de.ialistannen.htmljavadocparser.util.LinkUtils;
import de.ialistannen.htmljavadocparser.util.StringUtils;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * A parser for javadoc classes.
 */
public class JClassParser extends JTypeParser {

  public JClassParser(String url, DocumentResolver resolver) {
    super(url, resolver);
  }

  public Collection<ControlModifier> parseControlModifiers() {
    String declaration = parseDeclaration();

    return Arrays.stream(ControlModifier.values())
        .filter(controlModifier -> declaration.contains(controlModifier.getName()))
        .collect(Collectors.toList());
  }

  public List<Invocable> parseConstructors(Index index) {
    Element constructorSummary = document().getElementById("constructor.summary");

    if (constructorSummary == null) {
      return Collections.emptyList();
    }

    Elements links = constructorSummary.parent()
        .getElementsByClass("memberNameLink");

    return links.stream()
        .map(element -> invocableFromLink(element.child(0), index))
        .collect(Collectors.toList());
  }

  public List<JavadocField> parseFields(Index index) {
    Element fieldSummary = document().getElementById("field.summary");

    if (fieldSummary == null) {
      return Collections.emptyList();
    }

    Elements links = fieldSummary.parent()
        .getElementsByClass("memberNameLink");

    return links.stream()
        .map(element -> fieldFromLink(element.child(0), index))
        .collect(Collectors.toList());
  }

  private JavadocField fieldFromLink(Element link, Index index) {
    String fullyQualifiedName = LinkUtils.linkToFqn(resolver().relativizeAbsoluteUrl(link));
    return new JField(
        fullyQualifiedName,
        new JFieldParser(resolver(), link.absUrl("href")),
        index
    );
  }

  public List<GenericType> parseGenericTypes(Index index) {
    Element typeNameLabel = document().getElementsByClass("typeNameLabel").first();
    String typeName = StringUtils.normalizeWhitespace(typeNameLabel.text());

    return ParserHelper.parseGenericTypes(
        index,
        typeNameLabel,
        typeName,
        parseSimpleName(),
        parsePackage() + "." + parseSimpleName(),
        resolver()
    );
  }

  public boolean parseIsStatic() {
    return ParserHelper.parseIsStatic(parseDeclaration());
  }
}
