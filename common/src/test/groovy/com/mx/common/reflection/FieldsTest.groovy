package com.mx.common.reflection

import java.lang.reflect.Field

import spock.lang.Specification

class FieldsTest extends Specification {

  class TestDataClass {

    private int id

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
    ex.getMessage() == "Can't find field garbage on com.mx.common.reflection.FieldsTest.TestDataClass"
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

  def "setFieldValue throws exception if field does not exist"() {
    given:
    def obj = new TestDataClass().tap { id = 12 }

    when:
    Fields.setFieldValue("garbage", obj, 31)

    then:
    def ex = thrown(RuntimeException)
    ex.getMessage() == "Can't find field garbage on com.mx.common.reflection.FieldsTest.TestDataClass"
  }
}
