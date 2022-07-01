# Context

A subproject containing contextual classes like [Session](src/main/java/com/mx/path/model/context/Session.java), [RequestContext](src/main/java/com/mx/path/model/context/RequestContext.java), and [Facilities](src/main/java/com/mx/path/model/context/facility/Facilities.java).

### What is a contextual class?

A contextual class is a class that contains data associated with the current request, client, and/or user.

These types of classes are usually backed by a ThreadLocal or an external datastore. They are generally populated for each
request before an Accessor operation is called.
