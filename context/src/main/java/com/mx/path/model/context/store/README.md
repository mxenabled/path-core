# Store

## Scoped Stores

The ScopedStore* classes are wrappers for a Store implementation. They scope the store keys.

### ScopedStoreGlobal

No scope. The keys are globally accessible to all clients, users, and sessions.

### ScopedStoreClient

Client id scoped. The keys are accessible to all users and sessions that belong to the client.

### ScopedStoreUser

User id scoped. The keys are accessible to all sessions that belong to the user.

### ScopedStoreSession

User id scoped. The keys are accessible to the current session only.


## StoreLock

This implements Lock using an implementation of Store.