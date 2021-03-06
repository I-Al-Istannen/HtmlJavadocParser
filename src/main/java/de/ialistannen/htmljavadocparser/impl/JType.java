package de.ialistannen.htmljavadocparser.impl;

import static java.util.stream.Collectors.toList;

import de.ialistannen.htmljavadocparser.model.JavadocField;
import de.ialistannen.htmljavadocparser.model.JavadocPackage;
import de.ialistannen.htmljavadocparser.model.doc.JavadocComment;
import de.ialistannen.htmljavadocparser.model.properties.Invocable;
import de.ialistannen.htmljavadocparser.model.properties.JavadocElement;
import de.ialistannen.htmljavadocparser.model.types.JavadocAnnotation;
import de.ialistannen.htmljavadocparser.model.types.JavadocClass;
import de.ialistannen.htmljavadocparser.model.types.JavadocEnum;
import de.ialistannen.htmljavadocparser.model.types.JavadocInterface;
import de.ialistannen.htmljavadocparser.model.types.Type;
import de.ialistannen.htmljavadocparser.parsing.JTypeParser;
import de.ialistannen.htmljavadocparser.parsing.doc.JavadocCommentParser;
import de.ialistannen.htmljavadocparser.resolving.DocumentResolver;
import de.ialistannen.htmljavadocparser.resolving.HtmlSummaryParser;
import de.ialistannen.htmljavadocparser.resolving.Index;
import de.ialistannen.htmljavadocparser.resolving.ResourcesResolver;
import java.util.List;
import java.util.Optional;

/**
 * A base javadoc {@link Type}.
 */
public class JType implements Type {

  private final String fullyQualifiedName;
  private final String simpleName;
  protected final Index index;
  private JTypeParser jTypeParser;

  public JType(String fullyQualifiedName, String simpleName, Index index, JTypeParser jTypeParser) {
    this.fullyQualifiedName = fullyQualifiedName;
    this.simpleName = simpleName;
    this.index = index;
    this.jTypeParser = jTypeParser;
  }

  @Override
  public String getDeclaration() {
    return jTypeParser.parseDeclaration();
  }

  @Override
  public Optional<Type> getSuperClass() {
    return jTypeParser.parseSuperClass().map(index::getTypeForFullNameOrError);
  }

  @Override
  public List<JavadocInterface> getSuperInterfaces() {
    return jTypeParser.parseSuperInterfaces().stream()
        .map(index::getTypeForFullNameOrError)
        .map(type -> (JavadocInterface) type)
        .collect(toList());
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
    return simpleName;
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
    return new JavadocCommentParser().parse(jTypeParser.parseJavadocComment());
  }

  @Override
  public VisibilityLevel getVisibility() {
    return jTypeParser.parseVisibilityLevel();
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
  public String getUrl() {
    return jTypeParser.parseUrl();
  }

  @Override
  public String toString() {
    return "JType{" + getFullyQualifiedName() + '}';
  }

  public static void main(String[] args) {
    String baseUrl = "https://docs.oracle.com/javase/10/docs/api/";
    DocumentResolver documentResolver = new ResourcesResolver(
        baseUrl, "/testfiles"
    );
    Index index = new Index();

    HtmlSummaryParser summaryParser = new HtmlSummaryParser(
        documentResolver,
        baseUrl,
        baseUrl + "/allclasses-noframe.html",
        index
    );
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

    System.out.println();
    System.out.println();
    printClassInfo(
        (JavadocClass) index.getTypeForFullNameOrError("javax.swing.JApplet")
    );

    System.out.println();
    System.out.println("----");
    printEnumInfo((JavadocEnum) index
        .getTypeForFullNameOrError("java.nio.file.StandardCopyOption")
    );

    System.out.println();
    System.out.println("----");
    JAnnotation annotation = (JAnnotation) index
        .getTypeForFullNameOrError("java.lang.annotation.Target");
    printTypeInfo(annotation);
    for (Invocable invocable : annotation.getMethods()) {
      System.out.println();
      printInvocableInfo(invocable);
    }

    System.out.println();
    System.out.println("----");
    printClassInfo((JavadocClass) index
        .getTypeForFullNameOrError("java.util.AbstractMap.SimpleEntry")
    );

    System.out.println();
    System.out.println("----");
    JavadocClass mapClass = (JavadocClass) index.getTypeForFullNameOrError("java.util.Map");
    printClassInfo(mapClass);
    System.out.println();
    mapClass.getMethods().stream()
        .filter(invocable -> invocable.getSimpleName().equals("ofEntries"))
        .findFirst()
        .ifPresent(JType::printInvocableInfo);

    System.out.println();
    System.out.println("----");
    JavadocClass listClass = (JavadocClass) index.getTypeForFullNameOrError("java.util.List");
    printClassInfo(listClass);
    System.out.println();
    listClass.getMethods().stream()
        .filter(invocable -> invocable.getSimpleName().equals("toArray"))
        .filter(invocable -> invocable.getParameters().size() == 1)
        .findFirst()
        .ifPresent(JType::printInvocableInfo);

    System.out.println();
    System.out.println("----");
    printClassInfo((JavadocClass) index.getTypeForFullNameOrError("javax.swing.SwingConstants"));
    System.out.println();
    printFieldInfo(
        ((JavadocInterface) index.getTypeForFullNameOrError("javax.swing.SwingConstants"))
            .getFields().get(0)
    );

    System.out.println();
    System.out.println();
    printPackageInfo(index.getPackageOrError("java.util"));

    System.out.println();
    System.out.println();
    printClassInfo((JavadocClass) index.getTypeForFullNameOrError("java.util.HashMap"));
  }

  private static void printClassInfo(JavadocClass javadocClass) {
    printTypeInfo(javadocClass);
    System.out.println("Fields:           : " + javadocClass.getFields());
    System.out.println("Constructors:     : " + javadocClass.getConstructors());
    System.out.println("Generic types:    : " + javadocClass.getGenericTypes());
    System.out.println("Override modifier : " + javadocClass.getOverrideControlModifier());
    System.out.println("Static            : " + javadocClass.isStatic());
  }

  private static void printEnumInfo(JavadocEnum javadocEnum) {
    printTypeInfo(javadocEnum);
    System.out.println("Constants         : " + javadocEnum.getConstants());
    for (JavadocElement constant : javadocEnum.getConstants()) {
      System.out.println();
      printFieldInfo((JavadocField) constant);
    }
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
    System.out.println("URL               : " + type.getUrl());
    System.out.println("Javadoc           : " + type.getJavadoc());
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
    System.out.println("Generic types     : " + method.getGenericTypes());
    System.out.println("Thrown exceptions : " + method.getThrows());
    System.out.println("Static            : " + method.isStatic());
    System.out.println("URL               : " + method.getUrl());
    System.out.println("Javadoc           : " + method.getJavadoc());
  }

  private static void printFieldInfo(JavadocField field) {
    System.out.println("Declaration       : " + field.getDeclaration());
    System.out.println("Simple name       : " + field.getSimpleName());
    System.out.println("Fully qualified   : " + field.getFullyQualifiedName());
    System.out.println("Type              : " + field.getType());
    System.out.println("Visibility        : " + field.getVisibility());
    System.out.println("Package           : " + field.getPackage());
    System.out.println("Declared owner    : " + field.getDeclaredOwner());
    System.out.println("Original owner    : " + field.getOriginalOwner());
    System.out.println("Override modifier : " + field.getOverrideControlModifier());
    System.out.println("Static            : " + field.isStatic());
    System.out.println("URL               : " + field.getUrl());
    System.out.println("Javadoc           : " + field.getJavadoc());
  }

  private static void printPackageInfo(JavadocPackage javadocPackage) {
    System.out.println("Simple name       : " + javadocPackage.getSimpleName());
    System.out.println("Fully qualified   : " + javadocPackage.getFullyQualifiedName());
    System.out.println("Package           : " + javadocPackage.getPackage());
    System.out.println("Contained classes : " + javadocPackage.getContainedTypes());
    System.out.println("URL               : " + javadocPackage.getUrl());
    System.out.println("Javadoc           : " + javadocPackage.getJavadoc());

  }
}
