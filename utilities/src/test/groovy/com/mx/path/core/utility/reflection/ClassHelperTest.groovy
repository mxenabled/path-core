package com.mx.path.core.utility.reflection

import java.lang.reflect.Method
import java.lang.reflect.ParameterizedType

import spock.lang.Specification

class TestClass {
  private List<String> stringList = new ArrayList<>()
  private Map<String, Double> stringToDoubleMap = new HashMap<>()
  private String nonGenericType = ""

  public static String staticMethod() {
    return "Class says \"hi!\"";
  }

  static String secretStaticMethod() {
    return "Class says \"hi!\"";
  }

  private static String safeAndSecretStaticMethod() {
    return "Class says \"hi!\"";
  }

  Map<String, Integer> getMap() {
    return new HashMap<String, Integer>()
  }

  List<List<Integer>> getListOfLists() {
    return new ArrayList<List<Integer>>()
  }

  String secretMethod() {
    throw new IllegalAccessException()
    return "Hello There!"
  }

  private String secretAndSafeMethod() {
    return "Hello There!"
  }
}

class ClassHelperTest extends Specification {
  static class TestParent {
    private String member;

    TestParent(String member) {
      this.member = member;
    }

    String getMember() {
      return member
    }

    void withParameters(String bar) {}
  }

  static class TestChild extends TestParent {
    TestChild(String member) {
      super(member)
    }
  }

  static class TestOther {}

  def "buildInstance"() {
    given:
    def subject = new ClassHelper()

    when: "valid instance"
    def created = subject.buildInstance(TestParent.class, TestChild.class, "clientId")

    then:
    created != null

    when: "incompatible types"
    subject.buildInstance(TestOther.class, TestChild.class, "clientId")

    then:
    thrown(RuntimeException)

    when: "parameter mismatch"
    subject.buildInstance(TestParent.class, TestChild.class, 1, "garbage")

    then:
    thrown(RuntimeException)
  }

  def "resolveGenericFieldTypes"() {
    given:
    def classHelper = new ClassHelper()

    TestClass testClass = new TestClass()
    def stringListField = testClass.getClass().getDeclaredField("stringList")
    def stringToDoubleMapField = testClass.getClass().getDeclaredField("stringToDoubleMap")
    def nonGenericTypeField = testClass.getClass().getDeclaredField("nonGenericType")

    when:
    def stringListTypes = classHelper.resolveParameterizedFieldTypes(stringListField)
    def stringToDoubleMapTypes = classHelper.resolveParameterizedFieldTypes(stringToDoubleMapField)
    def nonGenericTypes = classHelper.resolveParameterizedFieldTypes(nonGenericTypeField)

    then:
    stringListTypes.size() == 1
    ((Class<?>) stringListTypes.get(0)) == String

    stringToDoubleMapTypes.size() == 2
    ((Class<?>) stringToDoubleMapTypes.get(0)) == String
    ((Class<?>) stringToDoubleMapTypes.get(1)) == Double
    nonGenericTypes.size() == 0
  }

  def "resolveGenericMethodReturnTypes"() {
    given:
    def subject = new ClassHelper()
    Method getMap = TestClass.getMethod("getMap")
    Method getListOfLists = TestClass.getMethod("getListOfLists")

    when:
    def getMapGenerics = subject.resolveParameterizedMethodReturnTypes(getMap)
    def getListOfListsGenerics = subject.resolveParameterizedMethodReturnTypes(getListOfLists)
    def listOfListGeneric = subject.resolveParameterizedTypes(getListOfListsGenerics.get(0))

    then:
    getMapGenerics.size() == 2
    ((Class<?>) getMapGenerics.get(0)) == String
    ((Class<?>) getMapGenerics.get(1)) == Integer

    getListOfListsGenerics.size() == 1
    getListOfListsGenerics.get(0) instanceof ParameterizedType

    listOfListGeneric.size() == 1
    ((Class<?>) listOfListGeneric.get(0)) == Integer
  }

  def "getClass"() {
    given:
    def subject = new ClassHelper()

    when: "valid class"
    def created = subject.getClass("com.mx.path.core.utility.reflection.TestClass")

    then:
    created != null

    when: "empty class name"
    created = subject.getClass("")

    then:
    thrown(RuntimeException)

    when: "invalid class name"
    created = subject.getClass("com.Nothing")

    then:
    thrown(RuntimeException)
  }

  def "getMethod"() {
    given:
    def subject = new ClassHelper()

    when: "valid method"
    def method = subject.getMethod(TestParent.class, "getMember")

    then:
    method != null

    when: "valid method with parameters"
    method = subject.getMethod(TestParent.class, "withParameters", String.class)

    then:
    method != null

    when: "invalid parameter count"
    method = subject.getMethod(TestParent.class, "withParameters", String.class, Integer.class)

    then:
    thrown(RuntimeException)

    when: "invalid parameter type"
    method = subject.getMethod(TestParent.class, "withParameters", Integer.class)

    then:
    thrown(RuntimeException)
  }

  def "getTypes"() {
    given:
    def subject = new ClassHelper()

    when:
    def types = subject.getTypes("Hi", new TestParent("clientId"))

    then:
    types == [
      String.class,
      TestParent.class
    ]
  }

  def "invokeMethod with resultType"() {
    given:
    def subject = new ClassHelper()
    def target = "Hi"

    when:
    subject.invokeMethod(String.class, target, "concat", " There")

    then:
    notThrown(Exception)

    when:
    def result = subject.invokeMethod(String.class, target, "concat", " There")

    then:
    result == "Hi There"

    when: "invalid method"
    result = subject.invokeMethod(String.class, target, "junk", " There")

    then:
    def e = thrown(RuntimeException)
    e.message == "No method junk on class java.lang.String with argTypes java.lang.String"

    when: "invalid parameters"
    result = subject.invokeMethod(String.class, target, "concat", 1)

    then:
    e = thrown(RuntimeException)
    e.message == "No method concat on class java.lang.String with argTypes java.lang.Integer"

    when: "method call exception"
    subject.invokeMethod(String.class, new TestClass(), "secretMethod")

    then:
    e = thrown(RuntimeException)
    e.message == "No method secretMethod on class com.mx.path.core.utility.reflection.TestClass"

    when: "method is not whitelisted"
    result = subject.invokeMethod(String.class, new TestClass(), "secretAndSafeMethod")

    then:
    e = thrown(RuntimeException)
    e.message == "No method secretAndSafeMethod on class com.mx.path.core.utility.reflection.TestClass"

    when: "method is whitelisted"
    subject = new ClassHelper("secretAndSafeMethod")
    result = subject.invokeMethod(String.class, new TestClass(), "secretAndSafeMethod")

    then:
    result == "Hello There!"
  }

  def "invokeMethod without resultType"() {
    given:
    def subject = new ClassHelper()
    def target = "Hi"

    when:
    subject.invokeMethod(target, "concat", " There")

    then:
    notThrown(Exception)

    when: "invalid method"
    subject.invokeMethod(target, "junk", " There")

    then:
    def e = thrown(RuntimeException)
    e.message == "No method junk on class java.lang.String with argTypes java.lang.String"

    when: "invalid parameters"
    subject.invokeMethod(new TestClass(), "secretMethod")

    then:
    e = thrown(RuntimeException)
    e.message == "No method secretMethod on class com.mx.path.core.utility.reflection.TestClass"

    when: "method is not whitelisted"
    subject.invokeMethod(new TestClass(), "secretAndSafeMethod")

    then:
    e = thrown(RuntimeException)
    e.message == "No method secretAndSafeMethod on class com.mx.path.core.utility.reflection.TestClass"

    when: "method is whitelisted"
    subject = new ClassHelper("secretAndSafeMethod")
    subject.invokeMethod(new TestClass(), "secretAndSafeMethod")

    then:
    noExceptionThrown()
  }

  def "invokeStaticMethod with resultType"() {
    given:
    def subject = new ClassHelper()
    def target = "Hi"

    when:
    def result = subject.invokeStaticMethod(String.class, TestClass.class, "staticMethod")

    then:
    result == "Class says \"hi!\""

    when: "invalid method"
    subject.invokeStaticMethod(String.class, TestClass.class, "nonExistentMethod", "junk", " There")

    then:
    def e = thrown(RuntimeException)
    e.message == "No method nonExistentMethod on class com.mx.path.core.utility.reflection.TestClass with argTypes java.lang.String, java.lang.String"

    when: "invalid parameters"
    subject.invokeStaticMethod(String.class, String.class, "valueOf", "concat", 1)

    then:
    e = thrown(RuntimeException)
    e.message == "No method valueOf on class java.lang.String with argTypes java.lang.String, java.lang.Integer"

    when: "method is not whitelisted"
    subject.invokeStaticMethod(String.class, TestClass.class, "safeAndSecretStaticMethod")

    then:
    e = thrown(RuntimeException)
    e.message == "No method safeAndSecretStaticMethod on class com.mx.path.core.utility.reflection.TestClass"

    when: "method is whitelisted"
    subject = new ClassHelper("safeAndSecretStaticMethod")
    result = subject.invokeStaticMethod(String.class, TestClass.class, "safeAndSecretStaticMethod")

    then:
    result == "Class says \"hi!\""
  }

  def "invokeStaticMethod without resultType"() {
    given:
    def subject = new ClassHelper()
    def target = String.class

    when:
    subject.invokeStaticMethod(target, "valueOf", 1)

    then:
    notThrown(Exception)

    when: "invalid method"
    subject.invokeStaticMethod(target, "junk", "Hello, there")

    then:
    def e = thrown(RuntimeException)
    e.message == "No method junk on class java.lang.String with argTypes java.lang.String"

    when: "invalid parameters"
    subject.invokeStaticMethod(TestClass.class, "staticMethod", 1)

    then:
    e = thrown(RuntimeException)
    e.message == "No method staticMethod on class com.mx.path.core.utility.reflection.TestClass with argTypes java.lang.Integer"

    when: "method is not whitelisted"
    subject.invokeStaticMethod(TestClass.class, "safeAndSecretStaticMethod")

    then:
    e = thrown(RuntimeException)
    e.message == "No method safeAndSecretStaticMethod on class com.mx.path.core.utility.reflection.TestClass"

    when: "method is whitelisted"
    subject = new ClassHelper("safeAndSecretStaticMethod")
    subject.invokeStaticMethod(TestClass.class, "safeAndSecretStaticMethod")

    then:
    noExceptionThrown()
  }
}
