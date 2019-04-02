package de.ialistannen.htmljavadocparser.exception;

/**
 * An exception that occurred while trying to resolve a document.
 */
public class ResolveException extends RuntimeException {

  public ResolveException(String message) {
    super(message);
  }

  public ResolveException(String message, Throwable cause) {
    super(message, cause);
  }
}
