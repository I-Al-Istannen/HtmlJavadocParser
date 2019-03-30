package de.ialistannen.htmljavadocparser.impl;

import static java.util.stream.Collectors.toList;

import de.ialistannen.htmljavadocparser.exception.ResolveException;
import de.ialistannen.htmljavadocparser.model.JavadocPackage;
import de.ialistannen.htmljavadocparser.model.doc.JavadocComment;
import de.ialistannen.htmljavadocparser.model.properties.Invocable;
import de.ialistannen.htmljavadocparser.model.types.JavadocAnnotation;
import de.ialistannen.htmljavadocparser.model.types.Type;
import de.ialistannen.htmljavadocparser.parsing.JTypeParser;
import de.ialistannen.htmljavadocparser.resolving.DocumentResolver;
import de.ialistannen.htmljavadocparser.resolving.Index;
import de.ialistannen.htmljavadocparser.resolving.LocalFileResolver;
import de.ialistannen.htmljavadocparser.util.Memoized;
import java.util.List;
import java.util.Optional;
import org.jsoup.nodes.Document;

public class JType implements Type {

  private Index index;
  private DocumentResolver resolver;
  private JTypeParser jTypeParser;
  private Memoized<String> declaration;
  private Memoized<Optional<Type>> superclass;
  private Memoized<List<Type>> superInterfaces;
  private Memoized<VisibilityLevel> visiblity;

  public JType(Index index, DocumentResolver resolver, JTypeParser jTypeParser) {
    this.index = index;
    this.resolver = resolver;
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
    return null;
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
    return null;
  }

  @Override
  public String getFullyQualifiedName() {
    return jTypeParser.parsePackage() + "." + jTypeParser.parseSimpleName();
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
    return null;
  }

  @Override
  public Type getOriginalOwner() {
    return null;
  }

  @Override
  public String toString() {
    return "JType{" + getDeclaration() + '}';
  }

  public static void main(String[] args) throws Exception {
    DocumentResolver documentResolver = new LocalFileResolver(
        "https://docs.oracle.com/javase/10/docs/api/", "/testfiles"
    );
    Index index = new Index((ind, name) -> {
      try {
        Document document = documentResolver.resolve(name.replace(".", "/") + ".html");
        return Optional.of(new JType(ind, documentResolver, new JTypeParser(document)));
      } catch (ResolveException e) {
        return Optional.empty();
      }
    });

    Document document = documentResolver.resolve("java/lang/String.html");
    JType type = new JType(index, documentResolver, new JTypeParser(document));
    System.out.println(type.getDeclaration());
    System.out.println(type.getSuperClass());
    System.out.println(type.getSuperInterfaces());
    System.out.println(type.getDeprecationStatus());
    System.out.println(type.getVisibility());

    System.out.println(new JTypeParser(documentResolver.resolve("javax/swing/JApplet.html"))
        .parseDeprecationStatus());
  }
}
