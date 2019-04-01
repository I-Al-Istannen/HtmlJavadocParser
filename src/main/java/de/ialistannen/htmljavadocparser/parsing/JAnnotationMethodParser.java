package de.ialistannen.htmljavadocparser.parsing;

import de.ialistannen.htmljavadocparser.resolving.DocumentResolver;

public class JAnnotationMethodParser extends JInvocableParser {

  public JAnnotationMethodParser(String url, DocumentResolver resolver) {
    super(url, resolver);
  }

  @Override
  public String parseActualOwner() {
    return parsePackage() + "." + new JAnnotationParser(url(), resolver()).parseSimpleName();
  }

  @Override
  public String parseOriginalOwner() {
    return parseActualOwner();
  }
}
