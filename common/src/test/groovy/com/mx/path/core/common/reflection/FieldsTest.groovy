package com.mx.path.core.common.reflection

import java.lang.reflect.Field
import java.time.Duration
import java.time.ZoneId
import java.util.regex.Pattern
import java.util.regex.PatternSyntaxException

import com.mx.path.core.common.configuration.ConfigurationException
import com.mx.testing.ConstructorsTestClass

import spock.lang.Specification

class FieldsTest extends Specification {

  class TestDataClass {

    private int id
    private Integer cid

    private float pFloat
    private Float cFloat

    private double pDouble
    private Double cDouble

    private long pLong
    private Long cLong

    private String string

    private Duration duration

    private com.mx.path.core.common.request.Feature enumeration

    private Pattern regex

    private Byte cByte;

    private Short cShort;

    private Character cCharacter;

    private ZoneId zoneId;

    private Class<?> klass;

    def getId() {
      return this.id
    }
  }

  Field field

  def setup() {
    field = TestDataClass.class.getDeclaredField("id")
  }

  def "getFieldValue"() {
    given:
    def obj = new TestDataClass().tap {id = 12 }

    when: "using Field"
    def val = Fields.getFieldValue(field, obj)

    then:
    val == 12

    when: "using field name"
    val = Fields.getFieldValue("id", obj)

    then:
    val == 12
  }

  def "getFieldValue throws exception if field does not exist"() {
    given:
    def obj = new TestDataClass().tap { id = 12 }

    when:
    Fields.getFieldValue("garbage", obj)

    then:
    def ex = thrown(RuntimeException)
    ex.getMessage() == "Can't find field garbage on com.mx.path.core.common.reflection.FieldsTest.TestDataClass"
  }

  def "setFieldValue"() {
    given:
    def obj = new TestDataClass().tap { id = 12 }

    when: "using Field"
    Fields.setFieldValue(field, obj, 13)

    then:
    obj.getId() == 13

    when: "using field name"
    Fields.setFieldValue("id", obj, 14)

    then:
    obj.getId() == 14
  }

  def "setFieldValue type coercion"() {
    given:
    def obj = new TestDataClass()

    when: "using Field"
    Fields.setFieldValue(fieldName, obj, val)

    then:
    Fields.getFieldValue(fieldName, obj) == expected

    where:
    fieldName  | val                   | expected
    "id"      | 1                     | 1
    "id"      | " 2 "                 | 2

    "cid"     | 3                     | 3
    "cid"     | " 4 "                 | 4

    "pFloat"  | " 5 "                 | Float.valueOf(5.0)
    "pFloat"  | " 6.1 "               | Float.valueOf(6.1)
    "pFloat"  | Float.valueOf(6.2)    | Float.valueOf(6.2)

    "cFloat"  | " 7 "                 | Float.valueOf(7.0)
    "cFloat"  | " 7.1 "               | Float.valueOf(7.1)
    "cFloat"  | Float.valueOf(7.2)    | Float.valueOf(7.2)

    "pDouble" | " 8 "                 | Double.valueOf(8.0)
    "pDouble" | " 8.1 "               | Double.valueOf(8.1)
    "pDouble" | Double.valueOf(8.2)   | Double.valueOf(8.2)

    "cDouble" | " 9 "                 | Double.valueOf(9.0)
    "cDouble" | " 9.1 "               | Double.valueOf(9.1)
    "cDouble" | Double.valueOf(9.2)   | Double.valueOf(9.2)

    "pLong"   | " 10 "                | Long.valueOf(10)
    "pLong"   | Long.valueOf(10)      | Long.valueOf(10)

    "cLong"   | " 11 "                | Long.valueOf(11)
    "cLong"   | Long.valueOf(11)      | Long.valueOf(11)

    "string"  | " strValue "          | " strValue "
    "string"  | 12                    | "12"
    "string"  | Long.valueOf(12)      | "12"
    "string"  | Float.valueOf(12.1)   | "12.1"
    "string"  | Double.valueOf(12.2)  | "12.2"

    "enumeration" | "ACCOUNTS"        | com.mx.path.core.common.request.Feature.ACCOUNTS
    "enumeration" | " TRANSFERS "     | com.mx.path.core.common.request.Feature.TRANSFERS
    "enumeration" | " Transfers "     | com.mx.path.core.common.request.Feature.TRANSFERS
    "enumeration" | " ach_transfers " | com.mx.path.core.common.request.Feature.ACH_TRANSFERS

    "zoneId"  | "MST"                 | ZoneId.of("MST", ZoneId.SHORT_IDS)
    "zoneId"  | "-02:00"              | ZoneId.of("-02:00")
    "zoneId"  | "America/Los_Angeles" | ZoneId.of("America/Los_Angeles")

    "klass"   | "com.mx.testing.ConstructorsTestClass" | ConstructorsTestClass.class
  }

  def "setFieldValue Duration coercion"() {
    given:
    def obj = new TestDataClass()

    when:
    Fields.setFieldValue(fieldName, obj, val)

    then:
    Fields.getFieldValue(fieldName, obj) == expected

    where:
    fieldName    | val                | expected
    "duration"  | " 10 s "           | Duration.ofSeconds(10)
    "duration"  | " 10sec "          | Duration.ofSeconds(10)
    "duration"  | " 10 m "           | Duration.ofMillis(10)
    "duration"  | " 10ms "           | Duration.ofMillis(10)
    "duration"  | " 10milliseconds " | Duration.ofMillis(10)
    "duration"  | " 10min "          | Duration.ofMinutes(10)
    "duration"  | " 10nanos "        | Duration.ofNanos(10)
    "duration"  | " 10h "            | Duration.ofHours(10)
  }

  def "setFieldValue coerce failures"() {
    given:
    def obj = new TestDataClass()

    when:
    Fields.setFieldValue(fieldName, obj, val)

    Duration.ofMinutes(1)

    then:
    def err = thrown(com.mx.path.core.common.configuration.ConfigurationException)
    err.getMessage() == expected

    where:
    fieldName    | val                | expected
    "duration"  | " 10 s1 "          | "Invalid duration string:  10 s1 "
    "duration"  | " 10 "             | "Invalid duration string:  10 "
    "duration"  | " 10 g"            | "Invalid duration unit:  10 g"
    "duration"  | 10                 | "Duration value must be a string"
    "enumeration" | "JUNK"           | "Invalid value JUNK for enumeration com.mx.path.core.common.request.Feature"
    "klass"       | "com.mx.InvalidClass" | "Invalid Class: com.mx.InvalidClass"
    "zoneId"      | "JNK"            | "Invalid zoneId value: JNK"
  }

  def "setFieldValue throws exception if field does not exist"() {
    given:
    def obj = new TestDataClass().tap { id = 12 }

    when:
    Fields.setFieldValue("garbage", obj, 31)

    then:
    def ex = thrown(RuntimeException)
    ex.getMessage() == "Can't find field garbage on com.mx.path.core.common.reflection.FieldsTest.TestDataClass"
  }

  def "coerces Pattern"() {
    given:
    def subject = new TestDataClass()

    when:
    Fields.setFieldValue("regex", subject, "(?i)^[a-z]+\$")

    then:
    subject.regex.matcher("AbCdEfG").matches()

    when:
    Fields.setFieldValue("regex", subject, "^[a-z]+\$")

    then:
    subject.regex.matcher("abcdefg").matches()
    !subject.regex.matcher("AbCdEfG").matches()

    when:
    Fields.setFieldValue("regex", subject, "[a-z")

    then:
    def ex = thrown(ConfigurationException)
    ex.message == "Invalid regular expression - [a-z"
    ex.cause.getClass() == PatternSyntaxException
  }

  def "coerces Byte"() {
    given:
    def subject = new TestDataClass()

    when:
    Fields.setFieldValue("cByte", subject, 12)

    then:
    subject.cByte == 12

    when:
    Fields.setFieldValue("cByte", subject, null)

    then:
    subject.cByte == null

    when:
    Fields.setFieldValue("cByte", subject, Byte.MAX_VALUE + 1)

    then:
    def ex = thrown(ConfigurationException)
    ex.message == "Invalid Byte value - 128"
    ex.cause.getClass() == NumberFormatException
  }

  def "coerces Character"() {
    given:
    def subject = new TestDataClass()

    when:
    Fields.setFieldValue("cCharacter", subject, "p")

    then:
    subject.cCharacter == 'p'

    when:
    Fields.setFieldValue("cCharacter", subject, null)

    then:
    subject.cCharacter == null

    when:
    Fields.setFieldValue("cCharacter", subject, "p?")

    then:
    def ex = thrown(ConfigurationException)
    ex.message == "Invalid char length - p?"
    ex.cause == null
  }

  def "coerces Short"() {
    given:
    def subject = new TestDataClass()

    when:
    Fields.setFieldValue("cShort", subject, 12)

    then:
    subject.cShort == 12

    when:
    Fields.setFieldValue("cShort", subject, null)

    then:
    subject.cShort == null

    when:
    Fields.setFieldValue("cShort", subject, Short.MAX_VALUE + 1)

    then:
    def ex = thrown(ConfigurationException)
    ex.message == "Invalid Short value - 32768"
    ex.cause.getClass() == NumberFormatException
  }
}
