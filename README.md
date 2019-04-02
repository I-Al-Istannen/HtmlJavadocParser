## About
This repo contains a small API that allows you interact and query a html-based Javadoc in a similar way to Java reflection.

## Usage
The starting point is the `JavadocApi`.
You can then search for types or packages via the index the `JavadocApi` will return you.

#### A small example to get the return type of the `ofEntries` method of `Map`:
```java
JavadocClass string = (JavadocClass) javadocApi.getIndex()
    .getTypeForFullNameOrError("java.util.Map");
string.getMethods().stream()
    .filter(m -> m.getSimpleName().equals("ofEntries"))
    .findFirst()
    .ifPresent(invocable -> System.out.println(invocable.getReturnType()));

// Prints:
JInterface{java.util.Map}
```

## Caveats
* Generic type parsing is implemented, but apart from the `getGenericType` methods the only information you will receive is the *name* of the type.  
  This means that `toArray(T[] ts)` will only tell you the parameter is of type `T[]`
  but won't provide any more information about `T`.
* I am fairly certain I have missed quite a few aspects of the HTML Javadoc format, so I'd expect it to randomly not like input

## Dependencies
* JSoup
