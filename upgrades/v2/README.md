## Path SDK Upgrade - v2

Updates to path-core V2

### Change Summary

See [Release Notes](https://github.com/mxenabled/path-core/blob/master/CHANGELOG.md) (***Needs actual link with anchor***)

### Update Script

1. In terminal
2. `cd` to repository root
3. Run: `curl -L "https://raw.github.com/mxenabled/path-core/master/upgrades/v2/core-2-upgrade.sh" | bash -s ./ -d false`

### Potential Issues

#### AccountBehaviors

The AccountBehavior, AccountBehaviors, and SessionAccountOwner classes have been removed from the core libraries and
the functionality is no longer supported as part of the Path SDK Core. They were moved to the MX-internal MDX Web 
artifact for backward compatibility with some legacy apps. To get them back add the dependency:

```groovy
dependencies {
  implementation "com.mx.web.mdx-web:legacy:36.+"
}
```

The Session functions to get and save AccountBehaviors were removed as well. They were moved to the AccountBehaviors class.

```java
  AccountBehaviors accountBehaviors = AccountBehaviors.loadFromSession();
  accountBehaviors.saveToSession();
```

#### ServiceIdentifier

Session.ServiceIdentifier has been removed. If the code is still using it there are a few options:

`Session.ServiceIdentifier.Session` can be changed to `Scope.Session`.

If other values are used, a determination needs to be made. Is the value written or read from the session store, also written or read by another connector?

*If, no:*

`Session.ServiceIdentifier.??` can be changed to `Scope.Service`

*If, yes:*
  
The key needs to remain the same or measures need to be taken to migrate away from the current key.

The way to keep key the same, define a stand-in Scope:

```java
public enum ArchitectIdentifier implements ScopeKeyGenerator {
  Architect {
    @Override
    public String generate() {
      return "Architect";
    }
  }
}
```

Change the code referencing Service.ServiceIdentifier to use this new identifier. As long as `generate()` returns the 
same value as the previous `ServiceIdentifier`, other connectors shouldn't have any issue accessing the data.

For reference, here are the key values for the removed ServiceIdentifiers
```
public enum ServiceIdentifier implements ScopeKeyGenerator {
    AFCUWS {
      @Override
      public String generate() {
        return "AFCUWS";
      }
    },
    Architect {
      @Override
      public String generate() {
        return "Architect";
      }
    },
    Checkfree {
      @Override
      public String generate() {
        return "Checkfree";
      }
    },
    Corillian {
      @Override
      public String generate() {
        return "Corillian";
      }
    },
    DataExchange {
      @Override
      public String generate() {
        return "DataExchange";
      }
    },
    Ensenta {
      @Override
      public String generate() {
        return "Ensenta";
      }
    },
    MDXOnDemand {
      @Override
      public String generate() {
        return "MDXOnDemand";
      }
    },
    RSA {
      @Override
      public String generate() {
        return "RSA";
      }
    },
    Session {
      @Override
      public String generate() {
        return "Session";
      }
    }
  }
```