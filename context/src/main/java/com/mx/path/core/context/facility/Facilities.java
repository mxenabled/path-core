package com.mx.path.core.context.facility;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.NonNull;

import com.mx.path.core.common.collection.ObjectMap;
import com.mx.path.core.common.event.EventBus;
import com.mx.path.core.common.exception.ExceptionReporter;
import com.mx.path.core.common.messaging.MessageBroker;
import com.mx.path.core.common.process.FaultTolerantExecutor;
import com.mx.path.core.common.security.EncryptionService;
import com.mx.path.core.common.store.Store;
import com.mx.path.core.context.GatewayContextException;

/**
 * Facilities are global objects that can be configured and provide services to gateways and accessors.
 *
 * <p>Configuration:
 * <ul>
 *   <li>cacheStore         - Volatile cache storage</li>
 *   <li>sessionStore       - Less volatile storage for session state</li>
 *   <li>encryptionService  - Service used to secure data</li>
 *   <li>eventBus           - In-memory publish/subscriber event bus</li>
 * </ul>
 *
 * <p>Example:
 *
 * <pre>
 * clientA:
 *   facilities:
 *     cacheStore:
 *       class: implementation.CacheStorage
 *       configuration:
 *         key: value
 *     sessionStore:
 *       class: implementation.SessionStorage
 *       configuration:
 *         key: value
 *     encryptionService:
 *       class: implementation.EncryptionService
 *       configuration:
 *         key: value
 *     eventBus: {} # no parameters
 * </pre>
 */
public class Facilities {

  private static final Map<String, Store> CACHE_STORES = new ConcurrentHashMap<>();
  private static final Map<String, EncryptionService> ENCRYPTION_SERVICES = new ConcurrentHashMap<>();
  private static final Map<String, ExceptionReporter> EXCEPTION_REPORTERS = new ConcurrentHashMap<>();
  private static final Map<String, EventBus> EVENT_BUSES = new ConcurrentHashMap<>();
  private static final Map<String, FaultTolerantExecutor> FAULT_TOLERANT_EXECUTORS = new ConcurrentHashMap<>();
  private static final Map<String, MessageBroker> MESSAGE_BROKERS = new ConcurrentHashMap<>();
  private static final Map<String, Store> SECRET_STORES = new ConcurrentHashMap<>();
  private static final Map<String, Store> SESSION_STORES = new ConcurrentHashMap<>();

  /**
   * Get cache store for given client id
   *
   * @param clientId client id
   * @return store
   */
  public static Store getCacheStore(String clientId) {
    return CACHE_STORES.get(clientId);
  }

  /**
   * Get encryption service for given client id.
   *
   * @param clientId client id
   * @return encryption service
   */
  public static EncryptionService getEncryptionService(String clientId) {
    return ENCRYPTION_SERVICES.get(clientId);
  }

  /**
   * Get event bus for given client id.
   *
   * @param clientId client id
   * @return event bus
   */
  public static EventBus getEventBus(String clientId) {
    return EVENT_BUSES.get(clientId);
  }

  /**
   * Get exception reporter for given client id.
   *
   * @param clientId client id
   * @return exception reporter
   */
  public static ExceptionReporter getExceptionReporter(String clientId) {
    return EXCEPTION_REPORTERS.get(clientId);
  }

  /**
   * Get fault-tolerant executor for given client id.
   *
   * @param clientId client id
   * @return fault-tolerance executor
   */
  public static FaultTolerantExecutor getFaultTolerantExecutor(String clientId) {
    return FAULT_TOLERANT_EXECUTORS.get(clientId);
  }

  /**
   * Get message broker for given client id.
   *
   * @param clientId client id
   * @return message broker
   */
  public static MessageBroker getMessageBroker(String clientId) {
    return MESSAGE_BROKERS.get(clientId);
  }

  public static Store getSecretStore(String clientId) {
    return SECRET_STORES.get(clientId);
  }

  /**
   * Get session store for given client id.
   *
   * @param clientId client id
   * @return store
   */
  public static Store getSessionStore(String clientId) {
    return SESSION_STORES.get(clientId);
  }

  /**
   * Add event bus to specified client.
   *
   * @param clientId client id
   * @param eventBus event bus
   */
  public static void addEventBus(String clientId, EventBus eventBus) {
    if (EVENT_BUSES.containsKey(clientId)) {
      throw new GatewayContextException("Attempting to overwrite GatewayEventBus for client: " + clientId + ". Only one can be registered. Use #getEventBus().");
    }
    EVENT_BUSES.put(clientId, eventBus);
  }

  /**
   * Add store to specified client.
   *
   * @param clientId client id
   * @param store store
   */
  public static void setCacheStore(@NonNull String clientId, @NonNull Store store) {
    CACHE_STORES.put(clientId, store);
  }

  /**
   * Add encryption service to specified client.
   *
   * @param clientId client id
   * @param encryptionService encryption service
   */
  public static void setEncryptionService(@NonNull String clientId, @NonNull EncryptionService encryptionService) {
    ENCRYPTION_SERVICES.put(clientId, encryptionService);
  }

  /**
   * Add exception reporter to specified cleint.
   *
   * @param clientId client id
   * @param exceptionReporter exception reporter
   */
  public static void setExceptionReporter(@NonNull String clientId, @NonNull ExceptionReporter exceptionReporter) {
    EXCEPTION_REPORTERS.put(clientId, exceptionReporter);
  }

  /**
   * Add fault-tolerant executor to specified client.
   *
   * @param clientId client id
   * @param faultTolerantExecutor fault-tolerant executor
   */
  public static void setFaultTolerantExecutor(@NonNull String clientId, @NonNull FaultTolerantExecutor faultTolerantExecutor) {
    FAULT_TOLERANT_EXECUTORS.put(clientId, faultTolerantExecutor);
  }

  /**
   * Add message broker to specified client.
   *
   * @param clientId client id
   * @param messageBroker message broker
   */
  public static void setMessageBroker(@NonNull String clientId, @NonNull MessageBroker messageBroker) {
    MESSAGE_BROKERS.put(clientId, messageBroker);
  }

  /**
   * Add secret store to specified client.
   *
   * @param clientId client id
   * @param store store
   */
  public static void setSecretStore(@NonNull String clientId, @NonNull Store store) {
    SECRET_STORES.put(clientId, store);
  }

  /**
   * Add session store to specified client.
   *
   * @param clientId client id
   * @param store store
   */
  public static void setSessionStore(@NonNull String clientId, @NonNull Store store) {
    SESSION_STORES.put(clientId, store);
  }

  /**
   * Reset all facilities maps.
   */
  public static void reset() {
    CACHE_STORES.clear();
    ENCRYPTION_SERVICES.clear();
    EVENT_BUSES.clear();
    FAULT_TOLERANT_EXECUTORS.clear();
    MESSAGE_BROKERS.clear();
    SECRET_STORES.clear();
    SESSION_STORES.clear();
  }

  /**
   * Fill description with description of all facilities maps for specified client.
   *
   * @param clientId client id
   * @param description object to fill with descriptions
   */
  public static void describe(String clientId, ObjectMap description) {
    describeFacility(CACHE_STORES.get(clientId), description.createMap("cacheStore"));
    describeFacility(ENCRYPTION_SERVICES.get(clientId), description.createMap("encryptionService"));
    describeFacility(FAULT_TOLERANT_EXECUTORS.get(clientId), description.createMap("faultTolerantExecutor"));
    describeFacility(MESSAGE_BROKERS.get(clientId), description.createMap("messageBroker"));
    describeFacility(SECRET_STORES.get(clientId), description.createMap("secretStore"));
    describeFacility(SESSION_STORES.get(clientId), description.createMap("sessionStore"));
  }

  private static void describeFacility(Object facility, ObjectMap description) {
    if (facility == null) {
      return;
    }
    try {
      description.put("class", facility.getClass().getCanonicalName());
      Method configurationGetter = facility.getClass().getMethod("getConfigurations");
      description.put("configurations", (ObjectMap) configurationGetter.invoke(facility));
    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
      description.put("configurations", "no description provided");
    }
  }
}
