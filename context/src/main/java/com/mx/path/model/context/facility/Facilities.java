package com.mx.path.model.context.facility;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.NonNull;

import com.mx.common.collections.ObjectMap;
import com.mx.common.events.EventBus;
import com.mx.common.exception.ExceptionReporter;
import com.mx.common.messaging.MessageBroker;
import com.mx.common.process.FaultTolerantExecutor;
import com.mx.common.security.EncryptionService;
import com.mx.common.store.Store;
import com.mx.path.model.context.event.GatewayEventBus;

/**
 * Facilities are global objects that can be configured and provide services to gateways and accessors.
 *
 * <p>Configuration:
 * <ul>
 *   <li>cacheStore         - Volatile cache storage</li>
 *   <li>sessionStore       - Less volatile storage for session state</li>
 *   <li>encryptionService  - Service used to secure data</li>
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
 *     exceptionReporter:
 *       class: implementation.ExceptionReporter
 *       configuration:
 *         key: value
 * </pre>
 */
public class Facilities {

  private static final Map<String, Store> CACHE_STORES = new ConcurrentHashMap<>();
  private static final Map<String, EncryptionService> ENCRYPTION_SERVICES = new ConcurrentHashMap<>();
  private static final Map<String, ExceptionReporter> EXCEPTION_REPORTERS = new ConcurrentHashMap<>();
  private static final EventBus EVENT_BUS = new GatewayEventBus();
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

  /**
   * @deprecated per-client EventBus is no longer supported. Use {@link #getEventBus()}
   * @param clientId
   * @return Global EventBus
   */
  @Deprecated
  public static EventBus getEventBus(String clientId) {
    return EVENT_BUS;
  }

  public static EventBus getEventBus() {
    return EVENT_BUS;
  }

  public static ExceptionReporter getExceptionReporter(String clientId) {
    return EXCEPTION_REPORTERS.get(clientId);
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

  /**
   * @deprecated Setting of the event bus is no longer supported
   * @param clientId
   * @param eventBus
   */
  @Deprecated
  public static void addEventBus(String clientId, EventBus eventBus) {
  }

  public static void setCacheStore(@NonNull String clientId, @NonNull Store store) {
    CACHE_STORES.put(clientId, store);
  }

  public static void setEncryptionService(@NonNull String clientId, @NonNull EncryptionService encryptionService) {
    ENCRYPTION_SERVICES.put(clientId, encryptionService);
  }

  public static void setExceptionReporter(@NonNull String clientId, @NonNull ExceptionReporter exceptionReporter) {
    EXCEPTION_REPORTERS.put(clientId, exceptionReporter);
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
    EXCEPTION_REPORTERS.clear();
    FAULT_TOLERANT_EXECUTORS.clear();
    MESSAGE_BROKERS.clear();
    SECRET_STORES.clear();
    SESSION_STORES.clear();
  }

  public static void describe(String clientId, ObjectMap description) {
    describeFacility(CACHE_STORES.get(clientId), description.createMap("cacheStore"));
    describeFacility(ENCRYPTION_SERVICES.get(clientId), description.createMap("encryptionService"));
    describeFacility(EXCEPTION_REPORTERS.get(clientId), description.createMap("exceptionReporters"));
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
