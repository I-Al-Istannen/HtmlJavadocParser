package de.ialistannen.htmljavadocparser.parsing;

import static de.ialistannen.htmljavadocparser.util.LinkUtils.linkToFqn;

import de.ialistannen.htmljavadocparser.model.generic.GenericType;
import de.ialistannen.htmljavadocparser.model.generic.GenericType.Bound;
import de.ialistannen.htmljavadocparser.model.generic.GenericType.Bound.BindingType;
import de.ialistannen.htmljavadocparser.model.generic.GenericTypeProxy;
import de.ialistannen.htmljavadocparser.model.properties.HasVisibility.VisibilityLevel;
import de.ialistannen.htmljavadocparser.model.types.Type;
import de.ialistannen.htmljavadocparser.resolving.Index;
import de.ialistannen.htmljavadocparser.util.LinkUtils;
import de.ialistannen.htmljavadocparser.util.StringUtils;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.jsoup.nodes.Element;

final class ParserHelper {

  /**
   * Extracts the visibility from the declaration.
   *
   * @param declaration the declaration
   * @return the visibility
   */
  static VisibilityLevel parseVisibilityLevel(String declaration) {
    String firstModifier = declaration
        // remove annotations with arguments
        .replaceAll("@.+?\\(.+?\\)", "")
        // remove annotations without arguments
        .replaceAll("@.+? ", "")
        .trim()
        .split("\\s")[0];

    try {
      return VisibilityLevel.valueOf(firstModifier.toUpperCase());
    } catch (IllegalArgumentException e) {
      return VisibilityLevel.PACKAGE_PRIVATE;
    }
  }

  /**
   * Extracts whether the object is static from the declaration.
   *
   * @param declaration the declaration
   * @return true if the object is static
   */
  static boolean parseIsStatic(String declaration) {
    String[] modifiers = declaration
        // remove annotations with arguments
        .replaceAll("@.+?\\(.+?\\)", "")
        // remove annotations without arguments
        .replaceAll("@.+? ", "")
        .trim()
        .split("\\s");

    return Arrays.asList(modifiers).contains("static");
  }

  /**
   * Parses the fqn of the return type from the member summary list row.
   *
   * @param row the row
   * @param owner the owning class (used if it is a constructor)
   * @return the return type
   */
  static String parseReturnTypeFromMemberSummaryRow(Element row, String owner) {
    // constructor
    if (!row.getElementsByClass("colConstructorName").isEmpty()) {
      return owner;
    }

    Element returnTypeTd = row.getElementsByClass("colFirst").first();
    Element returnTypeLink = returnTypeTd.getElementsByTag("a").first();

    // no link, so it was a primitive type
    if (returnTypeLink == null) {
      return returnTypeTd.text()
          .replace("static", "")
          .trim();
    }

    return linkToFqn(returnTypeLink.attr("href")).replaceAll("#.+", "");
  }

  /**
   * Parses the generic type from a String in the format {@code (Name)?<...>}.
   *
   * @param index the index to use
   * @param root the root element to extract links from
   * @param input the input string
   * @param simpleName the simple name of the object the type is in
   * @param fqn the fully qualified name of the object the type is in
   * @return the parsed generic types
   */
  public static List<GenericType> parseGenericTypes(Index index, Element root, String input,
      String simpleName, String fqn) {
    String typeName = StringUtils.normalizeWhitespace(input);

    if (!typeName.contains("<")) {
      return Collections.emptyList();
    }

    // Replace "Name<"
    typeName = typeName.replaceAll(".*?<(.+)", "$1");
    // Remove the last closing >
    typeName = typeName.substring(0, typeName.length() - 1);

    String[] parts = typeName.split(",");

    return Arrays.stream(parts)
        .map(genericTypes -> parseGenericType(index, root, genericTypes.trim(), simpleName, fqn))
        .collect(Collectors.toList());
  }

  private static GenericType parseGenericType(Index index, Element root, String string,
      String simpleName, String fqn) {
    // Just the type
    if (!string.contains("<") && !string.contains(" ")) {
      Type type = findTypeForName(index, root, string, simpleName, fqn);
      return new GenericType(type, List.of());
    }

    // It is sth like "Enum<E>"
    if (!string.contains(" ")) {
      String name = string.substring(0, string.indexOf('<'));
      Type type = findTypeForName(index, root, name, simpleName, fqn);
      String rest = string.substring(string.indexOf('<') + 1, string.lastIndexOf('>'));
      GenericType restType = parseGenericType(index, root, rest, simpleName, fqn);

      Bound bound = new Bound(restType, BindingType.EXACT);

      return new GenericType(type, List.of(bound));
    }

    // Has a bound like X extends|super Y
    String[] parts = string.split("\\s");
    String name = parts[0];
    Type rawType = findTypeForName(index, root, name, simpleName, fqn);

    BindingType type = BindingType.forName(parts[1]);
    String rest = parts[2];

    GenericType genericType = parseGenericType(index, root, rest, simpleName, fqn);
    Bound bound = new Bound(genericType, type);
    return new GenericType(rawType, List.of(bound));
  }

  private static Type findTypeForName(Index index, Element root, String name, String simpleName,
      String fqn) {
    for (Element link : root.getElementsByTag("a")) {
      if (link.ownText().matches(name + "\\b")) {
        return index.getTypeForFullNameOrError(LinkUtils.linkToFqn(link.attr("href")));
      }
    }
    if (name.equals(simpleName)) {
      return index.getTypeForFullNameOrError(fqn);
    }
    return new GenericTypeProxy(StringUtils.normalizeWhitespace(root.text()), name);
  }
}
