package com.mx.path.core.common.reflection


import javax.annotation.Nullable

import com.mx.testing.WithAnnotations

import spock.lang.Specification

class AnnotationsTest extends Specification {

  def "methodsWithAnnotation"() {
    when:
    def methods = Annotations.methodsWithAnnotation(Nullable.class, WithAnnotations.class)

    then:
    methods.size() == 2
  }

  def "fieldsWithAnnotation"() {
    when:
    def fields = Annotations.fieldsWithAnnotation(Nullable.class, WithAnnotations.class)

    then:
    fields.size() == 3
    fields[0].field.name == "aField"
    fields[1].field.name == "baseField"
    fields[2].field.name == "baseBaseField"
  }

  def "fieldsWithAnnotations"() {
    when:
    def fields = Annotations.fieldsAndAnnotations(WithAnnotations.class)

    then:
    fields.size() == 4
    fields[0].field.name == "aField"
    fields[0].getAnnotation(Nullable.class) != null

    fields[1].field.name == "unannotatedField"
    fields[1].getAnnotation(Nullable.class) == null

    fields[2].field.name == "baseField"
    fields[2].getAnnotation(Nullable.class) != null

    fields[3].field.name == "baseBaseField"
    fields[3].getAnnotation(Nullable.class) != null
  }

  def "hasAnnotation"()  {
    when:
    true == true

    then:
    !Annotations.hasAnnotation(WithAnnotations, com.mx.path.core.common.gateway.GatewayBaseClass)
    Annotations.hasAnnotation(WithAnnotations, com.mx.path.core.common.gateway.GatewayClass)
  }

  def "parametersWithAnnotation"() {
    given:
    def constructor = WithAnnotations.getDeclaredConstructors().first()

    when:
    def parameters = Annotations.parametersWithAnnotation(Nullable.class, constructor)

    then:
    parameters.size() == 1
    parameters[0].getPosition() == 0
  }

  def "asAnnotatedField"() {
    given:
    def subject = Annotations.fieldsAndAnnotations(WithAnnotations.class)[0]

    when:
    def result = subject.asAnnotatedField(Nullable.class)

    then:
    result.field == subject.field
    result.elementType == subject.elementType
  }
}
