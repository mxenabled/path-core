/**
 * Context classes for working with Stores
 *
 * <p><strong>Scoped Stores</strong>
 *
 * <p>The ScopedStore* classes are wrappers for a Store implementation. They scope the store keys.
 *
 * <p><strong>{@link com.mx.path.model.context.store.ScopedStoreGlobal}</strong>
 *
 * <p>No scope. The keys are globally accessible to all clients, users, and sessions.
 *
 * <p><strong>{@link com.mx.path.model.context.store.ScopedStoreClient}</strong>
 *
 * <p>Client id scoped. The keys are accessible to all users and sessions that belong to the client.
 *
 * <p><strong>{@link com.mx.path.model.context.store.ScopedStoreUser}</strong>
 *
 * <p>User id scoped. The keys are accessible to the given user.
 *
 * <p><strong>{@link com.mx.path.model.context.store.ScopedStoreCurrentUser}</strong>
 *
 * <p>User id scoped. The keys are accessible to the current user.
 *
 * <p><strong>{@link com.mx.path.model.context.store.ScopedStoreSession}</strong>
 *
 * <p>Session id scoped. The keys are accessible to the given session only.
 *
 * <p><strong>{@link com.mx.path.model.context.store.ScopedStoreCurrentSession}</strong>
 *
 * <p>Session id scoped. The keys are accessible to the current session only.
 *
 * <p><strong>{@link com.mx.path.model.context.store.StoreLock}</strong>
 *
 * <p>This implements Lock using provided implementation of Store.
 */
package com.mx.path.model.context.store;
