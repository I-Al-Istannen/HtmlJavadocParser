package de.ialistannen.htmljavadocparser.impl;

import static java.util.stream.Collectors.toList;

import de.ialistannen.htmljavadocparser.model.JavadocPackage;
import de.ialistannen.htmljavadocparser.model.doc.JavadocComment;
import de.ialistannen.htmljavadocparser.model.properties.Invocable;
import de.ialistannen.htmljavadocparser.model.types.JavadocAnnotation;
import de.ialistannen.htmljavadocparser.model.types.Type;
import de.ialistannen.htmljavadocparser.parsing.JTypeParser;
import de.ialistannen.htmljavadocparser.resolving.DocumentResolver;
import de.ialistannen.htmljavadocparser.resolving.HtmlSummaryParser;
import de.ialistannen.htmljavadocparser.resolving.Index;
import de.ialistannen.htmljavadocparser.resolving.LocalFileResolver;
import de.ialistannen.htmljavadocparser.util.Memoized;
import java.util.List;
import java.util.Optional;

public class JType implements Type {

  private final String fullyQualifiedName;
  private final Index index;
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
    return null;
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

  public static void main(String[] args) throws Exception {
    DocumentResolver documentResolver = new LocalFileResolver(
        "https://docs.oracle.com/javase/10/docs/api/", "/testfiles"
    );
    Index index = new Index();

    HtmlSummaryParser summaryParser = new HtmlSummaryParser(documentResolver, index);
    index.addTypes(summaryParser.getTypes());
    index.addPackages(summaryParser.getPackages());

    Type type = index.getTypeForFullNameOrError("java.lang.String");
    System.out.println(type.getDeclaration());
    System.out.println(type.getSuperClass());
    System.out.println(type.getSuperInterfaces());
    System.out.println(type.getDeprecationStatus());
    System.out.println(type.getVisibility());
    System.out.println(type.getPackage());
    System.out.println(type.getDeclaredOwner());
    System.out.println(type.getAnnotations());
  }
}
