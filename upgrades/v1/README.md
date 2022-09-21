## Path SDK Upgrade - v1

Cleans up from model relocation.

### Change Summary

* Move relocated classes into appropriate packages
* Remove deprecated com.mx.path.gateway.net.Serializer
* Remove Hystrix
* Deprecate MdxApiException
* Add new exception hierarchy
* Mark Response#checkStatus deprecated

### Update package names

1. In terminal
2. `cd` to repository root
3. Run: `curl --header "PRIVATE-TOKEN: glpat-HsCJsfzWj2KAWQQeqgky" "https://gitlab.mx.com/api/v4/projects/3071/repository/files/upgrades%2Fv1%2Fsdk-1-upgrade.sh/raw?ref=master" | bash -s ./ -d false`
4. Update dependencies: `./gradlew dependencies --write-locks`

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
