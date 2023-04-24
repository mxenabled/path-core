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
