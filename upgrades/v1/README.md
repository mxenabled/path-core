## Path SDK Upgrade - v1

Cleans up from model relocation and adds new exception hierarchy to replace MdxApiException.

### Change Summary

See [Release Notes](https://github.com/mxenabled/path-sdk/blob/master/CHANGELOG.md#100-2022-10-07)

### Update package names

1. In terminal
2. `cd` to repository root
3. Run: `curl -L "https://raw.github.com/mxenabled/path-sdk/master/upgrades/v1/sdk-1-upgrade.sh" | bash -s ./ -d false`

### Update to new facilities

_Example_

```groovy
dependencies {
  implementation "com.github.mxenabled.path-facilities:store-redis:1.+"
  implementation "com.github.mxenabled.path-facilities:store-vault:1.+"
  implementation "com.github.mxenabled.path-facilities:encryption-service-vault:1.+"
  implementation "com.github.mxenabled.path-facilities:encryption-service-jasypt:1.+"
  implementation "com.github.mxenabled.path-facilities:message-broker-nats:1.+"
  implementation "com.github.mxenabled.path-facilities:fault-tolerant-executor-resilience4j:1.+"
  implementation "com.github.mxenabled.path-facilities:exception-reporter-honeybadger:1.+"
}
```

### Use new exceptions

MdxApiException is deprecated and should not be used in new code. We can start replacing references to MdxApiException with more specific exceptions. See [exception README](https://github.com/mxenabled/path-sdk/tree/master/common/src/main/java/com/mx/common/exception) for details.

### Move ClientErrors to new ClientErrors Behavior (in web)

_For now, this is internal MX functionality only_

ClientErrors is deprecated. While it remains functional for MdxApiExceptions, it will not work with new exceptions.

See [ClientErrorsBehavior](https://gitlab.mx.com/mx/java-mdx-web/-/blob/master/src/main/java/com/mx/web/mdx/behaviors/ClientErrorsBehavior.java) for details.

### Potential Issues

#### cannot find symbol AccessorMethodNotImplementedException

We replaced all instances of:

```java
  throw new AccessorException(PathResponseStatus.NOT_IMPLEMENTED);
```

with

```java
  throw new AccessorMethodNotImplementedException();
```

There wasn't a perfect way to do this without making a bigger mess. To fix this you will need to add an import in each impacted file.

```java
  import com.mx.common.exception.AccessorMethodNotImplementedException;
```

#### cannot find symbol StoreMutex or SessionRepositoryMutex

StoreMutex and SessionRepositoryMutex have been removed. See com.mx.path.model.context.store.StoreLock for replacement.
