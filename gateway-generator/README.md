# Gateway Generator

A subproject containing an annotation processor that generates Gateways, remote Accessors, and remote Gateways.

### Annotation Processor

All generated classes are derived from [BaseAccessor.java](../models/src/main/java/com/mx/accessors/BaseAccessor.java).

Accessors annotated with `@GatewayClass` will have an associated `Gateway`, `RemoteService` (acting as a remote Gateway),
and `RemoteRequester` (acting as a request interface for the remote Gateway) generated for it. The generated request interface will be
available in the classpath as `RemoteAccessor`.

The operations these generated classes support must be annotated by `@GatewayAPI` in the associated Accessor class.

If method overloading is used in an Accessor, all the overloaded methods must be annotated with `@RemoteOperation("operationName")`
in order to allow a `RemoteService` to be correctly generated for it.