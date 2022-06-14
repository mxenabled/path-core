[Path SDK Issues](https://gitlab.mx.com/mx/money-experiences/path/path-issues/-/issues?scope=all&utf8=%E2%9C%93&state=opened&label_name[]=Path%20SDK)

# Path Common

Java jar containing common utility classes and dependency interface definitions.

## What belongs in this jar?

This jar should only include the following:

### Dependency Interface definitions

Interfaces for pluggable external dependencies and their configuration classes. These are interface definitions for "dependency inversion" purposes.

### Truly common utility

These should have only very light or no dependencies. The utilities should have general purpose application across all middleware applications. They should never be client or application specific.
