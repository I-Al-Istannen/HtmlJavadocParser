package de.ialistannen.htmljavadocparser.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GenericType {

  private String name;
  private List<Bound> bounds;

  public GenericType(String name, List<Bound> bounds) {
    this.name = name;
    this.bounds = new ArrayList<>(bounds);
  }

  public String getName() {
    return name;
  }

  public List<Bound> getBounds() {
    return Collections.unmodifiableList(bounds);
  }

  public static class Bound {

    private String typeName;
    private Type bindingType;

    public Bound(String typeName, Type bindingType) {
      this.typeName = typeName;
      this.bindingType = bindingType;
    }

    public String getTypeName() {
      return typeName;
    }

    public Type getBindingType() {
      return bindingType;
    }

    public enum Type {
      SUPER,
      EXTENDS
    }
  }
}
