package de.ialistannen.htmljavadocparser.parsing;

import de.ialistannen.htmljavadocparser.impl.JField;
import de.ialistannen.htmljavadocparser.model.JavadocField;
import de.ialistannen.htmljavadocparser.model.generic.GenericType;
import de.ialistannen.htmljavadocparser.model.generic.GenericType.Bound;
import de.ialistannen.htmljavadocparser.model.generic.GenericType.Bound.BindingType;
import de.ialistannen.htmljavadocparser.model.generic.GenericTypeProxy;
import de.ialistannen.htmljavadocparser.model.properties.Invocable;
import de.ialistannen.htmljavadocparser.model.properties.Overridable.ControlModifier;
import de.ialistannen.htmljavadocparser.model.types.Type;
import de.ialistannen.htmljavadocparser.resolving.DocumentResolver;
import de.ialistannen.htmljavadocparser.resolving.Index;
import de.ialistannen.htmljavadocparser.util.LinkUtils;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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
    Elements links = document().getElementById("constructor.summary").parent()
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
    String fullyQualifiedName = LinkUtils.linkToFqn(link.attr("href"));
    return new JField(
        fullyQualifiedName,
        new JFieldParser(resolver(), link.absUrl("href")),
        index
    );
  }

  public List<GenericType> parseGenericTypes(Index index) {
    Element typeNameLabel = document().getElementsByClass("typeNameLabel").first();
    String typeName = typeNameLabel.text();

    if (!typeName.contains("<")) {
      return Collections.emptyList();
    }

    // Replace "Name<"
    typeName = typeName.replaceAll(".+?<(.+)", "$1");
    // Remove the last closing >
    typeName = typeName.substring(0, typeName.length() - 1);

    String[] parts = typeName.split(", ");

    return Arrays.stream(parts)
        .map(genericTypes -> parseGenericType(index, typeNameLabel, genericTypes))
        .collect(Collectors.toList());
  }

  private GenericType parseGenericType(Index index, Element root, String string) {
    // Just the type
    if (!string.contains("<") && !string.contains(" ")) {
      Type type = findTypeForName(index, root, string);
      return new GenericType(type, List.of());
    }

    // It is sth like "Enum<E>"
    if (!string.contains(" ")) {
      String name = string.substring(0, string.indexOf('<'));
      Type type = findTypeForName(index, root, name);
      String rest = string.substring(string.indexOf('<') + 1, string.lastIndexOf('>'));
      GenericType restType = parseGenericType(index, root, rest);

      Bound bound = new Bound(restType, BindingType.EXACT);

      return new GenericType(type, List.of(bound));
    }

    // Has a bound like X extends|super Y
    String[] parts = string.split("\\s");
    String name = parts[0];
    Type rawType = findTypeForName(index, root, name);

    BindingType type = BindingType.forName(parts[1]);
    String rest = parts[2];

    GenericType genericType = parseGenericType(index, root, rest);
    Bound bound = new Bound(genericType, type);
    return new GenericType(rawType, List.of(bound));
  }

  private Type findTypeForName(Index index, Element root, String name) {
    for (Element link : root.getElementsByTag("a")) {
      if (link.ownText().matches(name + "\\b")) {
        return index.getTypeForFullNameOrError(LinkUtils.linkToFqn(link.attr("href")));
      }
    }
    if (name.equals(parseSimpleName())) {
      return index.getTypeForFullNameOrError(parsePackage() + "." + parseSimpleName());
    }
    return new GenericTypeProxy(root.text(), name);
  }
}
