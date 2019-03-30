package de.ialistannen.htmljavadocparser.exception;

public class ResolveException extends RuntimeException {

  public ResolveException(String message) {
    super(message);
  }

  public ResolveException(String message, Throwable cause) {
    super(message, cause);
  }
}
