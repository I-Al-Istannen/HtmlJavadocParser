package de.ialistannen.htmljavadocparser.model.properties;

import de.ialistannen.htmljavadocparser.model.types.Type;

/**
 * An object that can be owned by another (e.g. a method or nested classes).
 */
public interface Ownable {

  /**
   * Returns the owner this object was declared in (if possible).
   *
   * @return the owner
   */
  Type getDeclaredOwner();


  /**
   * Returns the owner this object was first declared in. This would be Object for hashCode. Will be
   * the object itself if there is no such connection.
   *
   * @return the original owner
   */
  Type getOriginalOwner();
}
