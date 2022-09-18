# Path Exceptions

Throwing the correct exception is vital for the Path system to behave in an expected way. The use of more specific exceptions is encouraged. 

## Path Exception Categories

### System

[PathSystemException](./PathSystemException.java) type exceptions are typically thrown at application boot time, when the Path gateway is being configured for use. It is mostly used to convey configuration issues or unexpected states.

Example:

```java
Gateway gateway;
try {
  gateway = new Configurator().buildFromYaml(yamlConfiguration); 
} catch (PathSystemException e) {
  throw new RuntimeException("Unable to initialize the Path Gateway. Unable to continue.", e)
}
```

### Request

[PathRequestException](./PathRequestException.java) type exceptions are thrown on errors when _using_ the Path Gateway.

Example:

```java
try{
  gateway.accounts().get("A-1234");
} catch (PathRequestException e) {
  throw new Exception("Unable to load account A-1234", e);
}
```

All request-type exceptions are children of [PathRequestException](./PathRequestException.java). When choosing the correct exception to throw in a given situation, it is better to use the more specific exceptions and avoid using the high-level exceptions like `PathRequestException` or `AccessorException`. If an appropriate exception does not exist, the creation of a new exception is encouraged, and the new exception should derive from the appropriate, existing exception. If the situation is likely to appear in other accessors, we may want to add the exception to the SDK for use in other accessors. If the situation is very specific to the current accessor, a custom exception can be created and kept in accessor repo.

Example:

```java
import com.mx.common.accessors.PathResponseStatus;
import com.mx.common.exception.AccessorUserException;

public class AccountLockedException extends AccessorUserException {
  public AccountLockedException() {
    super("User's account is locked", "Your account is locked, please contact customer support.", PathResponseStatus.NOT_ALLOWED);
  }
}
```
