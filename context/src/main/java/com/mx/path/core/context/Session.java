package com.mx.path.core.context;

import java.time.Duration;
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
import com.mx.path.core.common.compression.CompressionService;
import com.mx.path.core.common.lang.Strings;
import com.mx.path.core.common.security.EncryptionService;
import com.mx.path.core.common.serialization.LocalDateDeserializer;
import com.mx.path.core.common.serialization.LocalDateTimeDeserializer;
import com.mx.path.core.common.session.SessionInfo;
import com.mx.path.core.context.store.SessionRepository;
import com.mx.path.core.context.util.XMLGregorianCalendarConverter;

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

  // Static
  private static Supplier<SessionRepository> repositorySupplier = new SessionRepositorySupplier();

  private static Supplier<EncryptionService> encryptionServiceSupplier = new EncryptionServiceSupplier();

  private static Supplier<CompressionService> compressionServiceSupplier = null;

  private static final int DEFAULT_SESSION_EXPIRATION_MINUTES = 30;

  private static Duration defaultSessionExpiration = Duration.ofMinutes(DEFAULT_SESSION_EXPIRATION_MINUTES);

  private static Gson gson = new GsonBuilder()
      .registerTypeAdapter(XMLGregorianCalendar.class, new XMLGregorianCalendarConverter.Deserializer())
      .registerTypeAdapter(XMLGregorianCalendar.class, new XMLGregorianCalendarConverter.Serializer())
      .registerTypeAdapter(LocalDateTime.class, LocalDateTimeDeserializer.builder().build())
      .registerTypeAdapter(LocalDate.class, LocalDateDeserializer.builder().build())
      .create();

  /**
   * Return session repository.
   *
   * @return session repository
   */
  public static SessionRepository getRepository() {
    if (Objects.isNull(repositorySupplier)) {
      throw new GatewayContextException("No repositorySupplier registered for Session");
    }

    return repositorySupplier.get();
  }

  /**
   * Return session encryption service.
   *
   * @return encryption service
   */
  public static EncryptionService getEncryptionService() {
    if (Objects.isNull(encryptionServiceSupplier)) {
      throw new GatewayContextException("No encryptionServiceSupplier registered for Session");
    }

    return encryptionServiceSupplier.get();
  }

  /**
   * Return session compression service.
   *
   * @return compression service
   */
  public static CompressionService getCompressionService() {
    if (compressionServiceSupplier == null) {
      return null;
    }

    return compressionServiceSupplier.get();
  }

  /**
   * Set session service supplier.
   *
   * @param supplier supplier to set
   */
  public static void setCompressionServiceSupplier(Supplier<CompressionService> supplier) {
    compressionServiceSupplier = supplier;
  }

  /**
   * Remove session compression service.
   */
  static void resetCompressionService() {
    compressionServiceSupplier = null;
  }

  /**
   * Set default session expiry duration.
   *
   * @param sessionExpiry session expiry duration
   */
  public static void setDefaultSessionExpiration(Duration sessionExpiry) {
    defaultSessionExpiration = sessionExpiry;
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
   * Creates a new session object and sets it as the current session.
   */
  public static void createSession() {
    UUID uuid = UUID.randomUUID();
    String sessionId = uuid.toString();

    Session session = Session.start(sessionId);
    sessionThreadLocal.set(session);
  }

  /**
   * Current, active session for {@link ThreadLocal}.
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

  /**
   * Return session event listener.
   *
   * @return session event listener
   */
  public static List<SessionEventListener> getSessionEventListeners() {
    List<SessionEventListener> listeners = eventListenersThreadLocal.get();

    if (listeners == null) {
      listeners = new ArrayList<SessionEventListener>();
      eventListenersThreadLocal.set(listeners);
    }

    return listeners;
  }

  /**
   * Add new event listener to session.
   *
   * @param listener listener to add
   */
  public static void registerSessionEventListener(SessionEventListener listener) {
    getSessionEventListeners().add(listener);
  }

  /**
   * Loads new session by given sessionId and sets it as the current session.
   *
   * @param sessionId session ID
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
   * @param session session to set
   */
  public static void setCurrent(Session session) {
    sessionThreadLocal.set(session);
  }

  // Static methods

  /**
   * Return session repository supplier.
   *
   * @return repository supplier
   */
  public static Supplier<SessionRepository> getRepositorySupplier() {
    return repositorySupplier;
  }

  /**
   * Return session encryption service supplier.
   *
   * @return encryption service supplier
   */
  public static Supplier<EncryptionService> getEncryptionServiceSupplier() {
    return encryptionServiceSupplier;
  }

  /**
   * Set session repository supplier.
   *
   * @param supplier repository supplier to set
   */
  public static void setRepositorySupplier(Supplier<SessionRepository> supplier) {
    repositorySupplier = supplier;
  }

  /**
   * Set session encryption service supplier
   *
   * @param supplier encryption service supplier to set
   */
  public static void setEncryptionServiceSupplier(Supplier<EncryptionService> supplier) {
    encryptionServiceSupplier = supplier;
  }

  /**
   * Generate a new session. Does not persist.
   *
   * @param sessionId new session's ID
   * @return new Session
   */
  protected static Session start(String sessionId) {
    Session newSession = new Session();
    newSession.id = sessionId;
    newSession.startedAt = LocalDateTime.now(ZoneId.of("UTC"));
    newSession.expiresAt = newSession.startedAt.plusSeconds(defaultSessionExpiration.getSeconds()).toEpochSecond(ZoneOffset.UTC);
    return newSession;
  }

  /**
   * Generate a new session. Does not persist.
   *
   * @param sessionId new session's ID
   * @param userId the userId for this session
   * @return new Session
   */
  protected static Session start(String sessionId, String userId) {
    Session newSession = start(sessionId);
    newSession.userId = userId;

    return newSession;
  }

  /**
   * Enum for session states.
   */
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
  private SessionState sessionState = SessionState.UNAUTHENTICATED;
  private LocalDateTime startedAt;
  private String userId;

  // Getter/setters

  /**
   * Return client ID.
   *
   * @return the clientId - replaced by Request object encapsulating clientId
   */
  @Deprecated
  public final String getClientId() {
    return clientId;
  }

  /**
   * Set client ID.
   *
   * @param clientId the clientId to set
   */
  public final void setClientId(String clientId) {
    this.clientId = clientId;
  }

  /**
   * Return session device ID.
   *
   * @return identifier for the session's device
   */
  public final String getDeviceId() {
    return deviceId;
  }

  /**
   * Set session device ID.
   *
   * @param deviceId identifier for session's device
   */
  public final void setDeviceId(String deviceId) {
    this.deviceId = deviceId;
  }

  /**
   * Return device height.
   *
   * @return the deviceHeight
   */
  public final Integer getDeviceHeight() {
    return deviceHeight;
  }

  /**
   * Set device height.
   *
   * @param deviceHeight the deviceHeight to set
   */
  public final void setDeviceHeight(Integer deviceHeight) {
    this.deviceHeight = deviceHeight;
  }

  /**
   * Return device latitude.
   *
   * @return the deviceLatitude
   */
  public final Double getDeviceLatitude() {
    return deviceLatitude;
  }

  /**
   * Set device latitude.
   *
   * @param deviceLatitude the deviceLatitude to set
   */
  public final void setDeviceLatitude(Double deviceLatitude) {
    this.deviceLatitude = deviceLatitude;
  }

  /**
   * Return device longitude.
   *
   * @return the deviceLongitude
   */
  public final Double getDeviceLongitude() {
    return deviceLongitude;
  }

  /**
   * Set device longitude.
   *
   * @param deviceLongitude the deviceLongitude to set
   */
  public final void setDeviceLongitude(Double deviceLongitude) {
    this.deviceLongitude = deviceLongitude;
  }

  /**
   * Return device make.
   *
   * @return the deviceMake
   */
  public final String getDeviceMake() {
    return deviceMake;
  }

  /**
   * Set device make.
   *
   * @param deviceMake the deviceMake to set
   */
  public final void setDeviceMake(String deviceMake) {
    this.deviceMake = deviceMake;
  }

  /**
   * Return device model.
   *
   * @return the deviceModel
   */
  public final String getDeviceModel() {
    return deviceModel;
  }

  /**
   * Set device model.
   *
   * @param deviceModel the deviceModel to set
   */
  public final void setDeviceModel(String deviceModel) {
    this.deviceModel = deviceModel;
  }

  /**
   * Return device OS.
   *
   * @return the deviceOperatingSystem
   */
  public final String getDeviceOperatingSystem() {
    return deviceOperatingSystem;
  }

  /**
   * Set device OS.
   *
   * @param deviceOperatingSystem the deviceOperatingSystem to set
   */
  public final void setDeviceOperatingSystem(String deviceOperatingSystem) {
    this.deviceOperatingSystem = deviceOperatingSystem;
  }

  /**
   * Return device OS version.
   *
   * @return the deviceOperatingSystemVersion
   */
  public final String getDeviceOperatingSystemVersion() {
    return deviceOperatingSystemVersion;
  }

  /**
   * Set device OS version.
   *
   * @param deviceOperatingSystemVersion the deviceOperatingSystemVersion to set
   */
  public final void setDeviceOperatingSystemVersion(String deviceOperatingSystemVersion) {
    this.deviceOperatingSystemVersion = deviceOperatingSystemVersion;
  }

  /**
   * Return device width.
   *
   * @return the deviceWidth
   */
  public final Integer getDeviceWidth() {
    return deviceWidth;
  }

  /**
   * Set device width.
   *
   * @param deviceWidth the deviceWidth to set
   */
  public final void setDeviceWidth(Integer deviceWidth) {
    this.deviceWidth = deviceWidth;
  }

  /**
   * Return email.
   *
   * @return the email
   */
  public final String getEmail() {
    return decryptValue(email);
  }

  /**
   * Set email.
   *
   * @param email the email to set
   */
  public final void setEmail(String email) {
    this.email = encryptValue(email);
  }

  /**
   * Set session ID.
   *
   * @param newId this session's id
   */
  public final void setId(String newId) {
    this.id = newId;
  }

  /**
   * Return session ID.
   *
   * @return this session's id
   */
  public final String getId() {
    return id;
  }

  /**
   * Alias for {@link #getId()}.
   *
   * @return id
   */
  @Override
  public final String getSessionId() {
    return getId();
  }

  /**
   * Set session expiration time.
   *
   * @param newExpiresAt hard expiration time for this session
   */
  public final void setExpiresAt(Long newExpiresAt) {
    this.expiresAt = newExpiresAt;
  }

  /**
   * Return session expiration time.
   *
   * @return expiresAt hard expiration time for this session
   */
  public final Long getExpiresAt() {
    return expiresAt;
  }

  /**
   * Return user first name.
   *
   * @return the user's first name
   */
  public final String getFirstName() {
    return decryptValue(firstName);
  }

  /**
   * Set user first name.
   *
   * @param firstName the user's first name
   */
  public final void setFirstName(String firstName) {
    this.firstName = encryptValue(firstName);
  }

  /**
   * Return user last name.
   *
   * @return the user's last name
   */
  public final String getLastName() {
    return decryptValue(lastName);
  }

  /**
   * Set user last name.
   *
   * @param lastName the user's last name
   */
  public final void setLastName(String lastName) {
    this.lastName = encryptValue(lastName);
  }

  /**
   * Return session state.
   *
   * @return the sessionState
   */
  public final SessionState getSessionState() {
    return sessionState;
  }

  /**
   * Set session state.
   *
   * @param sessionState the sessionState to set
   */
  public final void setSessionState(SessionState sessionState) {
    this.sessionState = sessionState;
  }

  /**
   * Set session start timestamp.
   *
   * @param newStartedAt start timestamp for this session
   */
  public final void setStartedAt(LocalDateTime newStartedAt) {
    this.startedAt = newStartedAt;
  }

  /**
   * Return session start timestamp.
   *
   * @return start timestamp for this session
   */
  public final LocalDateTime getStartedAt() {
    return startedAt;
  }

  /**
   * Set session user ID.
   *
   * @param newUserId user id to whom this session belongs
   */
  public final void setUserId(String newUserId) {
    this.userId = newUserId;
  }

  /**
   * Return session user ID.
   *
   * @return user id to whom this session belongs
   */
  public final String getUserId() {
    return userId;
  }

  // Public methods

  /**
   * Clears session from repository and memory.
   */
  public final void delete() {
    getRepository().delete(this);
    clearSession();
  }

  /**
   * Clears value from session.
   *
   * @param key key associated with value
   *
   * @deprecated Use {@link Session#delete(ScopeKeyGenerator, String)}
   */
  @Deprecated
  public final void delete(String key) {
    getRepository().deleteValue(this, key);
  }

  /**
   * Clears value from session.
   *
   * @param key key associated with value
   * @param scope session scope
   */
  public final void delete(ScopeKeyGenerator scope, String key) {
    getRepository().deleteValue(this, buildScopeKey(scope, key));
  }

  /**
   * Get the value for given key.
   *
   * @param key key associated with value
   * @return value
   *
   * @deprecated Use {@link Session#get(ScopeKeyGenerator, String)}
   */
  @Deprecated
  public final String get(String key) {
    String value = getRepository().getValue(this, key);
    return decompressValue(decryptValue(value));
  }

  /**
   * Get the value for given key.
   *
   * @param scope scope
   * @param key key
   * @return value
   */
  public final String get(ScopeKeyGenerator scope, String key) {
    String value = getRepository().getValue(this, buildScopeKey(scope, key));
    return decompressValue(decryptValue(value));
  }

  /**
   * Return seconds to session expiration.
   *
   * @return seconds to expiration
   */
  @Override
  public final long getExpiresIn() {
    long now = LocalDateTime.now(ZoneId.of("UTC")).toEpochSecond(ZoneOffset.UTC);
    return expiresAt - now;
  }

  /**
   * Get the value for given key, scoped to a service where value is an Object.
   *
   * @param scope scope
   * @param key key
   * @param klass klass
   * @param <T> object type
   * @return object
   */
  public final <T> T getObj(ScopeKeyGenerator scope, String key, Class<T> klass) {
    return gson.fromJson(get(scope, key), klass);
  }

  /**
   * Put a key-value pair on session.
   *
   * @param key key
   * @param value value
   *
   * @deprecated Use {@link Session#put(ScopeKeyGenerator, String, String)}
   */
  @Deprecated
  public final void put(String key, String value) {
    getRepository().saveValue(this, key, compressValue(value));
  }

  /**
   * Put a key/value pair.
   *
   * @param scope scope
   * @param key key
   * @param value value
   */
  public final void put(ScopeKeyGenerator scope, String key, String value) {
    getRepository().saveValue(this, buildScopeKey(scope, key), compressValue(value));
  }

  /**
   * Put a key/value pair, scoped to a service where value is an Object.
   *
   * @param scope scope
   * @param key key
   * @param obj object
   */
  public final void putObj(ScopeKeyGenerator scope, String key, Object obj) {
    getRepository().saveValue(this, buildScopeKey(scope, key), compressValue(gson.toJson(obj)));
  }

  /**
   * Secure put.
   *
   * Encrypts the value before writing to value hash
   *
   * @param scope scope
   * @param key key
   * @param value value
   */
  public final void sput(ScopeKeyGenerator scope, String key, String value) {
    getRepository().saveValue(this, buildScopeKey(scope, key), encryptValue(compressValue(value)));
  }

  /**
   * Secure putObj.
   *
   * Encrypts the value before writing to value hash
   *
   * @param scope scope
   * @param key key
   * @param value value
   */
  public final void sputObj(ScopeKeyGenerator scope, String key, Object value) {
    getRepository().saveValue(this, buildScopeKey(scope, key), encryptValue(compressValue(gson.toJson(value))));
  }

  /**
   * Saves this session to repository.
   */
  public final void save() {
    notifyBeforeSave();

    getRepository().save(this);
  }

  // Private

  private String buildScopeKey(ScopeKeyGenerator scope, String key) {
    return scope.generate() + "." + key;
  }

  private String compressValue(String value) {
    CompressionService compressionService = getCompressionService();
    if (compressionService == null) {
      return value;
    }

    return compressionService.compress(value);
  }

  private String decompressValue(String value) {
    CompressionService compressionService = getCompressionService();
    if (compressionService == null) {
      return value;
    }

    return compressionService.decompress(value);
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
   * Invokes beforeSave on all registered listeners.
   */
  private void notifyBeforeSave() {
    getSessionEventListeners().stream().forEach((listener) -> {
      listener.beforeSave(this);
    });
  }
}
