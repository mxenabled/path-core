package com.mx.path.model.context;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Supplier;

import javax.xml.datatype.XMLGregorianCalendar;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mx.common.lang.Strings;
import com.mx.common.security.EncryptionService;
import com.mx.common.serialization.LocalDateDeserializer;
import com.mx.common.serialization.LocalDateTimeDeserializer;
import com.mx.common.session.SessionInfo;
import com.mx.path.model.context.store.SessionRepository;
import com.mx.path.model.context.util.XMLGregorianCalendarConverter;

/**
 * Represents a user session across Path services. The {@code .current()} session
 * exists as a Thread Local singleton. It MUST therefore be removed to prevent a memory leak.
 *
 * MAINTENANCE NOTICE:
 * This class MUST be backward compatible. New fields are ok, but renaming fields or
 * removing fields needs to be done in multiple, backward compatible steps.
 */
@SuppressWarnings("PMD.CyclomaticComplexity")
public class Session implements SessionInfo {
  /**
   * Used to scope session data by service.
   *
   * @deprecated Use {@link com.mx.path.gateway.context.Scope}
   */
  @Deprecated
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

  // Static
  private static Supplier<SessionRepository> repositorySupplier = new SessionRepositorySupplier();

  private static Supplier<EncryptionService> encryptionServiceSupplier = new EncryptionServiceSupplier();

  private static final int DEFAULT_TIMEOUT_SECONDS = 1800;

  private static Gson gson = new GsonBuilder()
      .registerTypeAdapter(XMLGregorianCalendar.class, new XMLGregorianCalendarConverter.Deserializer())
      .registerTypeAdapter(XMLGregorianCalendar.class, new XMLGregorianCalendarConverter.Serializer())
      .registerTypeAdapter(LocalDateTime.class, LocalDateTimeDeserializer.builder().build())
      .registerTypeAdapter(LocalDate.class, LocalDateDeserializer.builder().build())
      .create();

  public static SessionRepository getRepository() {
    if (Objects.isNull(repositorySupplier)) {
      throw new GatewayContextException("No repositorySupplier registered for Session");
    }

    return repositorySupplier.get();
  }

  public static EncryptionService getEncryptionService() {
    if (Objects.isNull(encryptionServiceSupplier)) {
      throw new GatewayContextException("No encryptionServiceSupplier registered for Session");
    }

    return encryptionServiceSupplier.get();
  }

  // Singleton
  private static ThreadLocal<List<SessionEventListener>> eventListenersThreadLocal = new ThreadLocal<List<SessionEventListener>>();
  private static ThreadLocal<Session> sessionThreadLocal = new ThreadLocal<>();

  /**
   * Remove current session from memory. (Does not remove from repository)
   */
  public static void clearSession() {
    sessionThreadLocal.remove();
    eventListenersThreadLocal.remove();
  }

  /**
   * Creates a new session object and sets it as the current session
   */
  public static void createSession() {
    UUID uuid = UUID.randomUUID();
    String sessionId = uuid.toString();

    Session session = Session.start(sessionId);
    sessionThreadLocal.set(session);
  }

  /**
   * Current, active session for LocalThread
   * @return Session
   */
  public static Session current() {
    return sessionThreadLocal.get();
  }

  /**
   * Deletes and clears the current session.
   *
   * Will only attempt to delete if there is a current session.
   */
  public static void deleteCurrent() {
    if (current() != null) {
      current().delete();
    }
  }

  public static List<SessionEventListener> getSessionEventListeners() {
    List<SessionEventListener> listeners = eventListenersThreadLocal.get();

    if (listeners == null) {
      listeners = new ArrayList<SessionEventListener>();
      eventListenersThreadLocal.set(listeners);
    }

    return listeners;
  }

  public static void registerSessionEventListener(SessionEventListener listener) {
    getSessionEventListeners().add(listener);
  }

  /**
   * Loads new session by given sessionId and sets it as the current session
   * @param sessionId
   */
  public static void loadSession(String sessionId) {
    if (sessionId == null) {
      return;
    }

    Session session = getRepository().load(sessionId);
    if (session == null) {
      return;
    }

    sessionThreadLocal.set(session);
  }

  /**
   * Sets Session.current() to given session instance.
   *
   * <p>Used to set in new threads forked by normal request thread. Prefer {@link Session#loadSession}
   * @param session
   */
  public static void setCurrent(Session session) {
    sessionThreadLocal.set(session);
  }

  // Static methods

  public static Supplier<SessionRepository> getRepositorySupplier() {
    return repositorySupplier;
  }

  public static Supplier<EncryptionService> getEncryptionServiceSupplier() {
    return encryptionServiceSupplier;
  }

  public static void setRepositorySupplier(Supplier<SessionRepository> supplier) {
    repositorySupplier = supplier;
  }

  public static void setEncryptionServiceSupplier(Supplier<EncryptionService> supplier) {
    encryptionServiceSupplier = supplier;
  }

  /**
   * Generate a new session. Does not persist.
   * @param sessionId new session's ID
   * @return new Session
   */
  protected static Session start(String sessionId) {
    Session newSession = new Session();
    newSession.id = sessionId;
    newSession.startedAt = LocalDateTime.now(ZoneId.of("UTC"));
    newSession.expiresAt = newSession.startedAt.plusSeconds(DEFAULT_TIMEOUT_SECONDS).toEpochSecond(ZoneOffset.UTC);
    return newSession;
  }

  /**
   * Generate a new session. Does not persist.
   * @param sessionId new session's ID
   * @param userId the userId for this session
   * @return new Session
   */
  protected static Session start(String sessionId, String userId) {
    Session newSession = start(sessionId);
    newSession.userId = userId;

    return newSession;
  }

  public enum SessionState {
    UNAUTHENTICATED, AUTHENTICATED, CHALLENGED, PINTOPASSWORD
  }

  // Fields

  private String clientId;
  private Long expiresAt;
  private String deviceId;
  private String deviceMake;
  private String deviceModel;
  private String deviceOperatingSystem;
  private String deviceOperatingSystemVersion;
  private Integer deviceHeight;
  private Double deviceLatitude;
  private Double deviceLongitude;
  private Integer deviceWidth;
  private String email;
  private String id;
  private String firstName;
  private String lastName;
  private List<SessionAccountOwner> jointOwners = new ArrayList<>();
  private SessionState sessionState = SessionState.UNAUTHENTICATED;
  private LocalDateTime startedAt;
  private String userId;

  // Getter/setters

  /**
   * @return the clientId - replaced by Request object encapsulating clientid
   */
  @Deprecated
  public final String getClientId() {
    return clientId;
  }

  /**
   * @param clientId the clientId to set
   */
  public final void setClientId(String clientId) {
    this.clientId = clientId;
  }

  /**
   * @return the session's accountBehaviors
   */
  public final AccountBehaviors getAccountBehaviors() {
    AccountBehaviors accountBehaviors = getObj(ServiceIdentifier.Session, "accountBehaviors", AccountBehaviors.class);

    return Objects.isNull(accountBehaviors) ? new AccountBehaviors() : accountBehaviors;
  }

  /**
   * @param accountBehaviors the session's accountBehaviors
   */
  public final void setAccountBehaviors(AccountBehaviors accountBehaviors) {
    sputObj(ServiceIdentifier.Session, "accountBehaviors", accountBehaviors);
  }

  /**
   * @return identifier for the session's device
   */
  public final String getDeviceId() {
    return deviceId;
  }

  /**
   * @param deviceId identifier for session's device
   */
  public final void setDeviceId(String deviceId) {
    this.deviceId = deviceId;
  }

  /**
   * @return the deviceHeight
   */
  public final Integer getDeviceHeight() {
    return deviceHeight;
  }

  /**
   * @param deviceHeight the deviceHeight to set
   */
  public final void setDeviceHeight(Integer deviceHeight) {
    this.deviceHeight = deviceHeight;
  }

  /**
   * @return the deviceLatitude
   */
  public final Double getDeviceLatitude() {
    return deviceLatitude;
  }

  /**
   * @param deviceLatitude the deviceLatitude to set
   */
  public final void setDeviceLatitude(Double deviceLatitude) {
    this.deviceLatitude = deviceLatitude;
  }

  /**
   * @return the deviceLongitude
   */
  public final Double getDeviceLongitude() {
    return deviceLongitude;
  }

  /**
   * @param deviceLongitude the deviceLongitude to set
   */
  public final void setDeviceLongitude(Double deviceLongitude) {
    this.deviceLongitude = deviceLongitude;
  }

  /**
   * @return the deviceMake
   */
  public final String getDeviceMake() {
    return deviceMake;
  }

  /**
   * @param deviceMake the deviceMake to set
   */
  public final void setDeviceMake(String deviceMake) {
    this.deviceMake = deviceMake;
  }

  /**
   * @return the deviceModel
   */
  public final String getDeviceModel() {
    return deviceModel;
  }

  /**
   * @param deviceModel the deviceModel to set
   */
  public final void setDeviceModel(String deviceModel) {
    this.deviceModel = deviceModel;
  }

  /**
   * @return the deviceOperatingSystem
   */
  public final String getDeviceOperatingSystem() {
    return deviceOperatingSystem;
  }

  /**
   * @param deviceOperatingSystem the deviceOperatingSystem to set
   */
  public final void setDeviceOperatingSystem(String deviceOperatingSystem) {
    this.deviceOperatingSystem = deviceOperatingSystem;
  }

  /**
   * @return the deviceOperatingSystemVersion
   */
  public final String getDeviceOperatingSystemVersion() {
    return deviceOperatingSystemVersion;
  }

  /**
   * @param deviceOperatingSystemVersion the deviceOperatingSystemVersion to set
   */
  public final void setDeviceOperatingSystemVersion(String deviceOperatingSystemVersion) {
    this.deviceOperatingSystemVersion = deviceOperatingSystemVersion;
  }

  /**
   * @return the deviceWidth
   */
  public final Integer getDeviceWidth() {
    return deviceWidth;
  }

  /**
   * @param deviceWidth the deviceWidth to set
   */
  public final void setDeviceWidth(Integer deviceWidth) {
    this.deviceWidth = deviceWidth;
  }

  /**
   * @return the email
   */
  public final String getEmail() {
    return decryptValue(email);
  }

  /**
   * @param email the email to set
   */
  public final void setEmail(String email) {
    this.email = encryptValue(email);
  }

  /**
   * @param newId this session's id
   */
  public final void setId(String newId) {
    this.id = newId;
  }

  /**
   * @return this session's id
   */
  public final String getId() {
    return id;
  }

  @Override
  public final String getSessionId() {
    return getId();
  }

  /**
   * @param newExpiresAt hard expiration time for this session
   */
  public final void setExpiresAt(Long newExpiresAt) {
    this.expiresAt = newExpiresAt;
  }

  /**
   * @return expiresAt hard expiration time for this session
   */
  public final Long getExpiresAt() {
    return expiresAt;
  }

  /**
   * @return the user's first name
   */
  public final String getFirstName() {
    return decryptValue(firstName);
  }

  /**
   * @param firstName the user's first name
   */
  public final void setFirstName(String firstName) {
    this.firstName = encryptValue(firstName);
  }

  /**
   * @return the user's last name
   */
  public final String getLastName() {
    return decryptValue(lastName);
  }

  /**
   * @param lastName the user's last name
   */
  public final void setLastName(String lastName) {
    this.lastName = encryptValue(lastName);
  }

  /**
   * @return a decrypted copy of jointOwners
   */
  public final List<SessionAccountOwner> getJointOwners() {
    return jointOwners;
  }

  /**
   * @param jointOwners the jointOwners to set
   */
  public final void setJointOwners(List<SessionAccountOwner> jointOwners) {
    this.jointOwners = jointOwners;
  }

  /**
   * @return the sessionState
   */
  public final SessionState getSessionState() {
    return sessionState;
  }

  /**
   * @param sessionState the sessionState to set
   */
  public final void setSessionState(SessionState sessionState) {
    this.sessionState = sessionState;
  }

  /**
   * @param newStartedAt start timestamp for this session
   */
  public final void setStartedAt(LocalDateTime newStartedAt) {
    this.startedAt = newStartedAt;
  }

  /**
   * @return start timestamp for this session
   */
  public final LocalDateTime getStartedAt() {
    return startedAt;
  }

  /**
   * @param newUserId user id to whom this session belongs
   */
  public final void setUserId(String newUserId) {
    this.userId = newUserId;
  }

  /**
   * @return user id to whom this session belongs
   */
  public final String getUserId() {
    return userId;
  }

  // Public methods

  /**
   * Clears session from repository and memory
   */
  public final void delete() {
    getRepository().delete(this);
    clearSession();
  }

  /**
   * Clears value from session
   * @param key
   *
   * @deprecated Use {@link Session#delete(ScopeKeyGenerator, String)}
   */
  @Deprecated
  public final void delete(String key) {
    getRepository().deleteValue(this, key);
  }

  /**
   * Clears value from session
   * @param key
   */
  public final void delete(ScopeKeyGenerator scope, String key) {
    getRepository().deleteValue(this, buildScopeKey(scope, key));
  }

  /**
   * Get the value for given key
   * @param key
   * @return
   *
   * @deprecated Use {@link Session#get(ScopeKeyGenerator, String)}
   */
  @Deprecated
  public final String get(String key) {
    String value = getRepository().getValue(this, key);
    return decryptValue(value);
  }

  /**
   * Get the value for given key
   * @param scope
   * @param key
   * @return
   */
  public final String get(ScopeKeyGenerator scope, String key) {
    String value = getRepository().getValue(this, buildScopeKey(scope, key));
    return decryptValue(value);
  }

  /**
   * @return seconds to expiration
   */
  @Override
  public final long getExpiresIn() {
    long now = LocalDateTime.now(ZoneId.of("UTC")).toEpochSecond(ZoneOffset.UTC);
    return expiresAt - now;
  }

  /**
   * Get the value for given key, scoped to a service where value is an Object
   * @param scope
   * @param key
   * @param klass
   */
  public final <T> T getObj(ScopeKeyGenerator scope, String key, Class<T> klass) {
    return gson.fromJson(get(scope, key), klass);
  }

  public final SessionMutex lock(String key) {
    SessionRepositoryMutex lock = new SessionRepositoryMutex(getRepository(), key);
    lock.request();

    return lock;
  }

  /**
   * Put a key/value pair
   * @param key
   * @param value
   *
   * @deprecated Use {@link Session#put(ScopeKeyGenerator, String, String)}
   */
  @Deprecated
  public final void put(String key, String value) {
    getRepository().saveValue(this, key, value);
  }

  /**
   * Put a key/value pair
   * @param scope
   * @param key
   * @param value
   *
   */
  public final void put(ScopeKeyGenerator scope, String key, String value) {
    getRepository().saveValue(this, buildScopeKey(scope, key), value);
  }

  /**
   * Put a key/value pair, scoped to a service where value is an Object
   * @param scope
   * @param key
   * @param obj
   */
  public final void putObj(ScopeKeyGenerator scope, String key, Object obj) {
    put(scope, key, gson.toJson(obj));
  }

  /**
   * Secure put.
   *
   * Encrypts the value before writing to value hash
   *
   * @param scope
   * @param key
   * @param value
   */
  public final void sput(ScopeKeyGenerator scope, String key, String value) {
    put(scope, key, encryptValue(value));
  }

  /**
   * Secure putObj.
   *
   * Encrypts the value before writing to value hash
   *
   * @param scope
   * @pram key
   * @param value
   */
  public final void sputObj(ScopeKeyGenerator scope, String key, Object value) {
    sput(scope, key, gson.toJson(value));
  }

  /**
   * Saves this session to repository
   */
  public final void save() {
    notifyBeforeSave();

    getRepository().save(this);
  }

  // Private

  private String buildScopeKey(ScopeKeyGenerator scope, String key) {
    return scope.generate() + "." + key;
  }

  private String decryptValue(String value) {
    if (Objects.isNull(value) || Strings.isBlank(value)) {
      return value;
    }

    EncryptionService encryptionService = getEncryptionService();
    if (Objects.nonNull(encryptionService) && encryptionService.isEncrypted(value)) {
      try {
        return encryptionService.decrypt(value);
      } catch (Exception e) {
        throw new GatewayContextException("Unable to decrypt. Assuming key got rotated prematurely. Killing session.", e);
      }
    }

    return value;
  }

  private String encryptValue(String value) {
    EncryptionService encryptionService = getEncryptionService();

    if (Objects.isNull(encryptionService)) {
      throw new GatewayContextException("Encryption service not configured");
    }

    return encryptionService.encrypt(value);
  }

  /**
   * Invokes beforeSave on all registered listeners
   */
  private void notifyBeforeSave() {
    getSessionEventListeners().stream().forEach((listener) -> {
      listener.beforeSave(this);
    });
  }
}
