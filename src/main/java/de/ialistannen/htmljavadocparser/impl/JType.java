package de.ialistannen.htmljavadocparser.impl;

import static java.util.stream.Collectors.toList;

import de.ialistannen.htmljavadocparser.model.JavadocField;
import de.ialistannen.htmljavadocparser.model.JavadocPackage;
import de.ialistannen.htmljavadocparser.model.doc.JavadocComment;
import de.ialistannen.htmljavadocparser.model.properties.Invocable;
import de.ialistannen.htmljavadocparser.model.types.JavadocAnnotation;
import de.ialistannen.htmljavadocparser.model.types.JavadocClass;
import de.ialistannen.htmljavadocparser.model.types.Type;
import de.ialistannen.htmljavadocparser.parsing.JTypeParser;
import de.ialistannen.htmljavadocparser.resolving.DocumentResolver;
import de.ialistannen.htmljavadocparser.resolving.HtmlSummaryParser;
import de.ialistannen.htmljavadocparser.resolving.Index;
import de.ialistannen.htmljavadocparser.resolving.LocalFileResolver;
import de.ialistannen.htmljavadocparser.util.Memoized;
import java.util.List;
import java.util.Optional;

/**
 * A base javadoc {@link Type}.
 */
public class JType implements Type {

  private final String fullyQualifiedName;
  protected final Index index;
  private JTypeParser jTypeParser;
  private Memoized<String> declaration;
  private Memoized<Optional<Type>> superclass;
  private Memoized<List<Type>> superInterfaces;
  private Memoized<VisibilityLevel> visiblity;

  public JType(String fullyQualifiedName, Index index, JTypeParser jTypeParser) {
    this.fullyQualifiedName = fullyQualifiedName;
    this.index = index;
    this.jTypeParser = jTypeParser;

    declaration = new Memoized<>(jTypeParser::parseDeclaration);
    superclass = new Memoized<>(
        () -> jTypeParser.parseSuperClass().map(index::getTypeForFullNameOrError)
    );
    superInterfaces = new Memoized<>(
        () -> jTypeParser.parseSuperInterfaces().stream()
            .map(index::getTypeForFullNameOrError)
            .collect(toList())
    );
    visiblity = new Memoized<>(jTypeParser::parseVisibilityLevel);
  }

  @Override
  public String getDeclaration() {
    return declaration.get();
  }

  @Override
  public Optional<Type> getSuperClass() {
    return superclass.get();
  }

  @Override
  public List<Type> getSuperInterfaces() {
    return superInterfaces.get();
  }

  @Override
  public List<Invocable> getMethods() {
    return jTypeParser.parseMethods(index);
  }

  @Override
  public List<JavadocAnnotation> getAnnotations() {
    return jTypeParser.parseAnnotations().stream()
        .map(index::getTypeForFullNameOrError)
        .filter(type -> type instanceof JavadocAnnotation)
        .map(type -> (JavadocAnnotation) type)
        .collect(toList());
  }

  @Override
  public DeprecationStatus getDeprecationStatus() {
    return jTypeParser.parseDeprecationStatus();
  }

  @Override
  public String getSimpleName() {
    return jTypeParser.parseSimpleName();
  }

  @Override
  public JavadocPackage getPackage() {
    return index.getPackageOrError(jTypeParser.parsePackage());
  }

  @Override
  public String getFullyQualifiedName() {
    return fullyQualifiedName;
  }

  @Override
  public Optional<JavadocComment> getJavadoc() {
    return Optional.empty();
  }

  @Override
  public VisibilityLevel getVisibility() {
    return visiblity.get();
  }

  @Override
  public Type getDeclaredOwner() {
    String simpleName = getSimpleName();
    if (simpleName.contains(".")) {
      String packageName = getPackage().getFullyQualifiedName();
      String outerFqn = packageName + "." + simpleName.split("\\.")[0];

      return index.getTypeForFullNameOrError(outerFqn);
    }
    return this;
  }

  @Override
  public Type getOriginalOwner() {
    return getDeclaredOwner();
  }

  @Override
  public String toString() {
    return "JType{" + getFullyQualifiedName() + '}';
  }

  public static void main(String[] args) {
    String baseUrl = "https://docs.oracle.com/javase/10/docs/api/";
    DocumentResolver documentResolver = new LocalFileResolver(
        baseUrl, "/testfiles"
    );
    Index index = new Index();

    HtmlSummaryParser summaryParser = new HtmlSummaryParser(documentResolver, baseUrl, index);
    index.addTypes(summaryParser.getTypes());
    index.addPackages(summaryParser.getPackages());

    Type type = index.getTypeForFullNameOrError("java.lang.String");
    printClassInfo((JavadocClass) type);

    System.out.println();
    printInvocableInfo(((JavadocClass) type).getConstructors().get(2));

    System.out.println();
    printFieldInfo(((JavadocClass) type).getFields().get(0));

    System.out.println();
    System.out.println();
    Invocable method = type.getMethods().stream()
        .filter(invocable -> invocable.getSimpleName().equals("getBytes"))
        .filter(invocable -> invocable.getParameters().size() == 1)
        .findFirst()
        .orElseThrow();
    printInvocableInfo(method);

    System.out.println();
    System.out.println();
    printClassInfo(
        (JavadocClass) index.getTypeForFullNameOrError("javax.tools.ForwardingFileObject")
    );

    System.out.println();
    System.out.println();
    printClassInfo(
        (JavadocClass) index.getTypeForFullNameOrError("java.lang.Enum")
    );
  }

  private static void printClassInfo(JavadocClass javadocClass) {
    printTypeInfo(javadocClass);
    System.out.println("Fields:           : " + javadocClass.getFields());
    System.out.println("Constructors:     : " + javadocClass.getConstructors());
    System.out.println("Generic types:    : " + javadocClass.getGenericTypes());
    System.out.println("Override modifier : " + javadocClass.getOverrideControlModifier());
  }

  private static void printTypeInfo(Type type) {
    System.out.println("Declaration       : " + type.getDeclaration());
    System.out.println("Simple name       : " + type.getSimpleName());
    System.out.println("Fully qualified   : " + type.getFullyQualifiedName());
    System.out.println("Super class       : " + type.getSuperClass());
    System.out.println("Super interfaces  : " + type.getSuperInterfaces());
    System.out.println("Visibility        : " + type.getVisibility());
    System.out.println("Deprecation       : " + type.getDeprecationStatus());
    System.out.println("Package           : " + type.getPackage());
    System.out.println("Declared owner    : " + type.getDeclaredOwner());
    System.out.println("Original owner    : " + type.getOriginalOwner());
    System.out.println("Annotations       : " + type.getAnnotations());
    System.out.println("Methods           : " + type.getMethods());
  }

  private static void printInvocableInfo(Invocable method) {
    System.out.println("Declaration       : " + method.getDeclaration());
    System.out.println("Simple name       : " + method.getSimpleName());
    System.out.println("Fully qualified   : " + method.getFullyQualifiedName());
    System.out.println("Visibility        : " + method.getVisibility());
    System.out.println("Return type       : " + method.getReturnType());
    System.out.println("Parameters        : " + method.getParameters());
    System.out.println("Deprecation       : " + method.getDeprecationStatus());
    System.out.println("Package           : " + method.getPackage());
    System.out.println("Declared owner    : " + method.getDeclaredOwner());
    System.out.println("Original owner    : " + method.getOriginalOwner());
    System.out.println("Override modifier : " + method.getOverrideControlModifier());
    System.out.println("Annotations       : " + method.getAnnotations());
    System.out.println("Thrown exceptions : " + method.getThrows());
  }

  private static void printFieldInfo(JavadocField field) {
    System.out.println("Declaration       : " + field.getDeclaration());
    System.out.println("Simple name       : " + field.getSimpleName());
    System.out.println("Fully qualified   : " + field.getFullyQualifiedName());
    System.out.println("Visibility        : " + field.getVisibility());
    System.out.println("Package           : " + field.getPackage());
    System.out.println("Declared owner    : " + field.getDeclaredOwner());
    System.out.println("Original owner    : " + field.getOriginalOwner());
    System.out.println("Override modifier : " + field.getOverrideControlModifier());
  }
}
