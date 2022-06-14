package com.mx.path.model.context.facility;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.NonNull;

import com.mx.common.collections.ObjectMap;
import com.mx.common.events.EventBus;
import com.mx.common.messaging.MessageBroker;
import com.mx.common.process.FaultTolerantExecutor;
import com.mx.common.security.EncryptionService;
import com.mx.common.store.Store;
import com.mx.path.model.context.GatewayContextException;
import com.mx.path.utilities.reflection.ClassHelper;

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
 *
 * <p><i>Notes:</i>
 * <p>A client EventBus can be registered here for deployment or client-specific handlers. If events are required by
 *    an implementation (accessor or behavior), they are required to make sure that the event bus is registered in
 *    their initialization code by using {@link Facilities#addEventBus(String, EventBus)}.
 */
public class Facilities {

  private static final Map<String, Store> CACHE_STORES = new ConcurrentHashMap<>();
  private static final Map<String, EncryptionService> ENCRYPTION_SERVICES = new ConcurrentHashMap<>();
  private static final Map<String, EventBus> EVENT_BUSES = new ConcurrentHashMap<>();
  private static final Map<String, FaultTolerantExecutor> FAULT_TOLERANT_EXECUTORS = new ConcurrentHashMap<>();
  private static final Map<String, MessageBroker> MESSAGE_BROKERS = new ConcurrentHashMap<>();
  private static final Map<String, Store> SECRET_STORES = new ConcurrentHashMap<>();
  private static final Map<String, Store> SESSION_STORES = new ConcurrentHashMap<>();

  public static Store getCacheStore(String clientId) {
    return CACHE_STORES.get(clientId);
  }

  public static EncryptionService getEncryptionService(String clientId) {
    return ENCRYPTION_SERVICES.get(clientId);
  }

  public static EventBus getEventBus(String clientId) {
    return EVENT_BUSES.get(clientId);
  }

  public static FaultTolerantExecutor getFaultTolerantExecutor(String clientId) {
    return FAULT_TOLERANT_EXECUTORS.get(clientId);
  }

  public static MessageBroker getMessageBroker(String clientId) {
    return MESSAGE_BROKERS.get(clientId);
  }

  public static Store getSecretStore(String clientId) {
    return SECRET_STORES.get(clientId);
  }

  public static Store getSessionStore(String clientId) {
    return SESSION_STORES.get(clientId);
  }

  public static void addEventBus(String clientId, EventBus eventBus) {
    if (EVENT_BUSES.containsKey(clientId)) {
      throw new GatewayContextException("Attempting to overwrite GatewayEventBus for client: " + clientId + ". Only one can be registered. Use #getEventBus().");
    }
    EVENT_BUSES.put(clientId, eventBus);
  }

  // Facilities should be instantiated in the GatewayConfigurator and then added via
  // the set<<Facility>>() methods.
  @Deprecated
  public static void populate(String clientId, ObjectMap map) {
    map.keySet().forEach(key -> {
      switch (key) {
        case "cacheStore":
          CACHE_STORES.put(clientId, buildCacheStore(map.getMap(key)));
          break;

        case "encryptionService":
          ENCRYPTION_SERVICES.put(clientId, buildEncryptionService(map.getMap(key)));
          break;

        case "eventBus":
          EVENT_BUSES.put(clientId, buildEventBus(map.getMap(key)));
          break;

        case "messageBroker":
          MESSAGE_BROKERS.put(clientId, buildMessageBroker(map.getMap(key)));
          break;

        case "sessionStore":
          SESSION_STORES.put(clientId, buildSessionStore(map.getMap(key)));
          break;

        case "secretStore":
          SECRET_STORES.put(clientId, buildSecretStore(map.getMap(key)));
          break;

        default:
          throw new RuntimeException("Invalid facility: " + key);
      }
    });
  }

  public static void setCacheStore(@NonNull String clientId, @NonNull Store store) {
    CACHE_STORES.put(clientId, store);
  }

  public static void setEncryptionService(@NonNull String clientId, @NonNull EncryptionService encryptionService) {
    ENCRYPTION_SERVICES.put(clientId, encryptionService);
  }

  public static void setFaultTolerantExecutor(@NonNull String clientId, @NonNull FaultTolerantExecutor faultTolerantExecutor) {
    FAULT_TOLERANT_EXECUTORS.put(clientId, faultTolerantExecutor);
  }

  public static void setMessageBroker(@NonNull String clientId, @NonNull MessageBroker messageBroker) {
    MESSAGE_BROKERS.put(clientId, messageBroker);
  }

  public static void setSecretStore(@NonNull String clientId, @NonNull Store store) {
    SECRET_STORES.put(clientId, store);
  }

  public static void setSessionStore(@NonNull String clientId, @NonNull Store store) {
    SESSION_STORES.put(clientId, store);
  }

  public static void reset() {
    CACHE_STORES.clear();
    ENCRYPTION_SERVICES.clear();
    EVENT_BUSES.clear();
    FAULT_TOLERANT_EXECUTORS.clear();
    MESSAGE_BROKERS.clear();
    SECRET_STORES.clear();
    SESSION_STORES.clear();
  }

  public static void describe(String clientId, ObjectMap description) {
    describeFacility(CACHE_STORES.get(clientId), description.createMap("cacheStore"));
    describeFacility(ENCRYPTION_SERVICES.get(clientId), description.createMap("encryptionService"));
    describeFacility(FAULT_TOLERANT_EXECUTORS.get(clientId), description.createMap("faultTolerantExecutor"));
    describeFacility(MESSAGE_BROKERS.get(clientId), description.createMap("messageBroker"));
    describeFacility(SECRET_STORES.get(clientId), description.createMap("secretStore"));
    describeFacility(SESSION_STORES.get(clientId), description.createMap("sessionStore"));
  }

  private static Store buildCacheStore(ObjectMap map) {
    return getInstanceByClass(Store.class, map, map.getMap("configurations"));
  }

  private static Store buildSessionStore(ObjectMap map) {
    return getInstanceByClass(Store.class, map, map.getMap("configurations"));
  }

  private static Store buildSecretStore(ObjectMap map) {
    return getInstanceByClass(Store.class, map, map.getMap("configurations"));
  }

  private static EncryptionService buildEncryptionService(ObjectMap map) {
    return getInstanceByClass(EncryptionService.class, map, map.getMap("configurations"));
  }

  private static MessageBroker buildMessageBroker(ObjectMap map) {
    return getInstanceByClass(MessageBroker.class, map, map.getMap("configurations"));
  }

  private static EventBus buildEventBus(ObjectMap map) {
    return getInstanceByClass(EventBus.class, map, map.getMap("configurations"));
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

  private static <T> T getInstanceByClass(Class<T> baseClass, ObjectMap node, Object... args) {
    Class<?> klass;
    klass = new ClassHelper().getClass(node.getAsString("class"));

    return new ClassHelper().buildInstance(baseClass, klass, args);
  }

}
