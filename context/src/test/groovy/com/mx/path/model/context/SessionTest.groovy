package com.mx.path.model.context

import static org.mockito.Mockito.spy

import com.mx.common.collections.ObjectMap
import com.mx.common.security.EncryptionService
import com.mx.path.model.context.store.SessionRepository
import com.mx.testing.EncryptionServiceImpl
import com.mx.testing.HashSessionRepository
import com.mx.testing.TestPayee
import com.mx.testing.TestScope

import spock.lang.Specification

class SessionTest extends Specification {

  Session subject
  SessionRepository repository
  EncryptionService encryptionService

  def setup() {
    repository = Mock()
    encryptionService = Mock()
    Session.setRepositorySupplier({ -> repository })
    Session.setEncryptionServiceSupplier({ -> encryptionService })
    subject = new Session()
  }

  def cleanup() {
    Session.setRepositorySupplier(null)
    Session.setEncryptionServiceSupplier(null)
    Session.clearSession()
  }

  def "createsWithInitializedJoinOwnersArray"() {
    expect:
    subject.getJointOwners() != null
  }

  def "setCurrent"() {
    given:
    Session session = new Session()

    when:
    Session.setCurrent(session)

    then:
    Session.current() == session
  }

  def "startCreatesSessionWithExpirationAndId"() {
    given:
    subject = Session.start("session-1234")

    expect:
    subject.getExpiresAt() != null
    "session-1234" == subject.getId()
  }

  def "startCreatesSessionWithIdAndUser"() {
    given:
    subject = Session.start("session-1234", "user-1234")

    expect:
    "session-1234" == subject.getId()
    "user-1234" == subject.getUserId()
  }

  def "loadLoadsFromRepository"() {
    given:
    subject.setFirstName("first")
    subject.setLastName("last")
    subject.setClientId("clientId")
    subject.setDeviceId("deviceId")
    subject.setDeviceHeight(100)
    subject.setDeviceLatitude(101)
    subject.setDeviceMake("deviceMake")
    subject.setDeviceModel("deviceModel")
    subject.setDeviceOperatingSystem("operatingSystem")
    repository.load("session-1234") >> subject

    when:
    Session.loadSession("session-1234")

    then:
    subject == Session.current()
    subject.getFirstName() == Session.current().getFirstName()
    subject.getLastName() == Session.current().getLastName()
    subject.getClientId() == Session.current().getClientId()
    subject.getDeviceId() == Session.current().getDeviceId()
    subject.getDeviceHeight() == Session.current().getDeviceHeight()
    subject.getDeviceLatitude() == Session.current().getDeviceLatitude()
    subject.getDeviceMake() == Session.current().getDeviceMake()
    subject.getDeviceModel() == Session.current().getDeviceModel()
    subject.getDeviceOperatingSystem() == Session.current().getDeviceOperatingSystem()
  }

  def "createSessionStartsAndPersistSessionWithNewId"() {
    given:
    Session.createSession()

    expect:
    Session.current().getId() != null
    Session.SessionState.UNAUTHENTICATED == Session.current().getSessionState()
  }

  def "saveSavesToRepository"() {
    when:
    subject.save()

    then:
    1 * repository.save(subject)
  }

  def "getRetrievesFromRepository"() {
    when:
    subject.get("key1")
    subject.get(TestScope.Key, "key1")

    then:
    1 * repository.getValue(subject, "key1")
    1 * repository.getValue(subject, "scope.key1")
  }

  def "putStoresInRepository"() {
    when:
    subject.put("key1", "value1")
    subject.put(TestScope.Key, "key1", "value1")

    then:
    1 * repository.saveValue(subject, "key1", "value1")
    1 * repository.saveValue(subject, "scope.key1", "value1")
  }

  def "deleteRemovesFromRepository"() {
    when:
    subject.delete("key1")
    subject.delete(TestScope.Key, "key1")

    then:
    1 * repository.deleteValue(subject, "key1")
    1 * repository.deleteValue(subject, "scope.key1")
  }

  def "deleteCurrentDeletesTheCurrentSession"() {
    given:
    Session session = Mock()
    repository.load("session-1234") >> session
    Session.loadSession("session-1234")

    when:
    Session.deleteCurrent()

    then:
    1 * repository.delete(session)
  }

  def "deleteCurrentDeletesDeletesNothing"() {
    Session.clearSession()

    Session.deleteCurrent()
  }

  def "beforeSaveInvokesListeners"() {
    given:
    SessionEventListener listener = Mock()
    SessionEventListener listener2 = Mock()
    Session.registerSessionEventListener(listener)
    Session.registerSessionEventListener(listener2)
    Session session = Session.start("session-1234")

    when:
    session.save()

    then:
    1 * listener.beforeSave(session)
    1 * listener2.beforeSave(session)
  }


  def "clearSessionRemovesListeners"() {
    given:
    SessionEventListener listener = Mock()
    Session.registerSessionEventListener(listener)
    assert 1 == Session.getSessionEventListeners().size()
    Session.clearSession()

    expect:
    0 == Session.getSessionEventListeners().size()
  }

  def "putWithObjSerializesCorrectly"() {
    given:
    HashSessionRepository.register()
    TestPayee payee = new TestPayee()
    payee.setAccountNumber("someAccountNumber")
    payee.setAddress("address")
    payee.setCity("River City")
    payee.setId("0")
    payee.setLogoUrl("http://google.com")
    payee.setMerchantId("0")
    payee.setName("Test payee")
    payee.setNextPaymentDeliverableOn("2020-06-19")
    payee.setNickname("Payee")
    payee.setPayeeType("OTHER")
    payee.setPhoneNumber("555-555-5555")
    payee.setPostalCode("84045")
    payee.setState("UT")

    when:
    subject.putObj(Session.ServiceIdentifier.Checkfree, "payee", payee)
    TestPayee cachedPayee = subject.getObj(Session.ServiceIdentifier.Checkfree, "payee", TestPayee.class)

    then:
    payee.getAccountNumber() == cachedPayee.getAccountNumber()
    payee.getAddress() == cachedPayee.getAddress()
    payee.getCity() == cachedPayee.getCity()
    payee.getId() == cachedPayee.getId()
    payee.getLogoUrl() == cachedPayee.getLogoUrl()
    payee.getMerchantId() == cachedPayee.getMerchantId()
    payee.getName() == cachedPayee.getName()
    payee.getNextPaymentDeliverableOn() == cachedPayee.getNextPaymentDeliverableOn()
    payee.getNickname() == cachedPayee.getNickname()
    payee.getPayeeType() == cachedPayee.getPayeeType()
    payee.getPhoneNumber() == cachedPayee.getPhoneNumber()
    payee.getPostalCode() == cachedPayee.getPostalCode()
    payee.getState() == cachedPayee.getState()

    when:
    subject.putObj(TestScope.Key, "payee", payee)
    cachedPayee = subject.getObj(TestScope.Key, "payee", TestPayee.class)

    then:
    payee.getAccountNumber() == cachedPayee.getAccountNumber()
    payee.getAddress() == cachedPayee.getAddress()
    payee.getCity() == cachedPayee.getCity()
    payee.getId() == cachedPayee.getId()
    payee.getLogoUrl() == cachedPayee.getLogoUrl()
    payee.getMerchantId() == cachedPayee.getMerchantId()
    payee.getName() == cachedPayee.getName()
    payee.getNextPaymentDeliverableOn() == cachedPayee.getNextPaymentDeliverableOn()
    payee.getNickname() == cachedPayee.getNickname()
    payee.getPayeeType() == cachedPayee.getPayeeType()
    payee.getPhoneNumber() == cachedPayee.getPhoneNumber()
    payee.getPostalCode() == cachedPayee.getPostalCode()
    payee.getState() == cachedPayee.getState()
  }

  def "getObjWithNull"() {
    when:
    def nullPayee = subject.getObj(Session.ServiceIdentifier.Checkfree, "junk", TestPayee.class)

    then:
    null == nullPayee

    when:
    nullPayee = subject.getObj(TestScope.Key, "junk", TestPayee.class)

    then:
    null == nullPayee
  }

  def "putWritesToValueMap"() {
    given:
    def repository = new HashSessionRepository()
    Session.setRepositorySupplier({ -> repository })
    subject.put(Session.ServiceIdentifier.RSA, "key1", "value")
    subject.put(TestScope.Key, "key1", "value")

    expect:
    "value" == repository.getValue(subject, "RSA.key1")
    "value" == repository.getValue(subject, "scope.key1")
  }

  def "getReadsFromSessionRepository"() {
    given:
    def rep = new HashSessionRepository()
    Session.setRepositorySupplier({ -> rep })
    rep.saveValue(subject, "RSA.key1", "valueInRedis")
    rep.saveValue(subject, "scope.key1", "valueInRedis")

    expect:
    "valueInRedis" == subject.get(Session.ServiceIdentifier.RSA, "key1")
    "valueInRedis" == subject.get(TestScope.Key, "key1")
  }

  def "deleteFromSessionRepository"() {
    given:
    def rep = new HashSessionRepository()
    Session.setRepositorySupplier({ -> rep })

    when:
    rep.saveValue(subject, "RSA.key1", "valueInRedis")
    rep.saveValue(subject, "scope.key1", "valueInRedis")

    then:
    "valueInRedis" == subject.get(Session.ServiceIdentifier.RSA, "key1")
    "valueInRedis" == subject.get(TestScope.Key, "key1")

    when:
    subject.delete(Session.ServiceIdentifier.RSA, "key1")
    subject.delete(TestScope.Key, "key1")

    then: "removed from repository"
    subject.get(Session.ServiceIdentifier.RSA, "key1") == null
    subject.get(TestScope.Key, "key1") == null
  }

  def "putWithServiceScopesCorrectly"() {
    given:
    HashSessionRepository.register()
    subject.put(Session.ServiceIdentifier.RSA, "key1", "value")
    subject.put(Session.ServiceIdentifier.Corillian, "key1", "anotherValue")

    expect:
    "value" == subject.get(Session.ServiceIdentifier.RSA, "key1")
    "anotherValue" == subject.get(Session.ServiceIdentifier.Corillian, "key1")
  }

  def "putSecureEncryptsValue"() {
    given:
    subject = spy(new Session())
    encryptionService.encrypt(_ as String) >> "!!CYPHERTEXT!!"

    when:
    subject.sput(Session.ServiceIdentifier.Architect, "password", "\$up3r\$up3r@3cr3t")

    then:
    1 * encryptionService.encrypt("\$up3r\$up3r@3cr3t")

    when:
    subject.sput(TestScope.Key, "password", "\$up3r\$up3r@3cr3t")

    then:
    1 * encryptionService.encrypt("\$up3r\$up3r@3cr3t")
  }

  def "getDecryptsValue"() {
    given:
    subject = spy(new Session())
    HashSessionRepository.register()

    when: "Fake putting encrypted value in session (deprecated)"
    subject.put(Session.ServiceIdentifier.Architect, "password", "encrypted:v1:(!ph3%")
    "plaintext" == subject.get(Session.ServiceIdentifier.Architect, "password")

    then:
    1 * encryptionService.isEncrypted("encrypted:v1:(!ph3%") >> true
    1 * encryptionService.decrypt("encrypted:v1:(!ph3%") >> "plaintext"

    when: "Fake putting encrypted value in session"
    subject.put(TestScope.Key, "password", "encrypted:v1:(!ph3%")
    "plaintext" == subject.get(TestScope.Key, "password")

    then:
    1 * encryptionService.isEncrypted("encrypted:v1:(!ph3%") >> true
    1 * encryptionService.decrypt("encrypted:v1:(!ph3%") >> "plaintext"
  }

  def "encryptsEmail"() {
    given:
    subject = spy(new Session())
    HashSessionRepository.register()

    when:
    subject.setEmail("test@unit.me")
    assert "test@unit.me" ==  subject.getEmail()

    then:
    1 * encryptionService.isEncrypted("encrypted:v1:(!ph3%") >> true
    1 * encryptionService.encrypt("test@unit.me") >> "encrypted:v1:(!ph3%"
    1 * encryptionService.decrypt("encrypted:v1:(!ph3%") >> "test@unit.me"
  }

  def "getAccountBehaviorsReturnsEmptyObjectIfNotSet"() {
    expect:
    subject.getAccountBehaviors() != null
    0 == subject.getAccountBehaviors().getAccountBehaviors().size()
  }

  def "getAccountReturnsSetAccountBehaviors"() {
    given:
    HashSessionRepository.register()
    EncryptionService encryptionService = new EncryptionServiceImpl(new ObjectMap())
    Session.setEncryptionServiceSupplier({ -> encryptionService })
    def setAccountBehaviors = new AccountBehaviors()
    setAccountBehaviors.put("acct1", new AccountBehavior())
    subject.setAccountBehaviors(setAccountBehaviors)

    expect:
    1 == subject.getAccountBehaviors().getAccountBehaviors().size()
  }

  def "lock returns a SessionMutex for the given key"() {
    when:
    SessionRepositoryMutex lock = subject.lock("key1")

    then:
    lock instanceof SessionRepositoryMutex
    lock.getKey() == "key1"
  }
}