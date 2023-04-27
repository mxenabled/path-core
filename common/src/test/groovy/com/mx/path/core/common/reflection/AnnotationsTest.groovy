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
    fields.size() == 1
    fields[0].getField().getName() == "aField"
  }

  def "fieldsWithAnnotations"() {
    when:
    def fields = Annotations.fieldsWithAnnotations(WithAnnotations.class)

    then:
    fields.size() == 2
    fields.get(0).getField().getName() == "aField"
    fields.get(0).getAnnotation(Nullable.class) != null

    fields.get(1).getField().getName() == "unannotatedField"
    fields.get(1).getAnnotation(Nullable.class) == null
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
    def subject = Annotations.fieldsWithAnnotations(WithAnnotations.class).get(0)

    when:
    def result = subject.asAnnotatedField(Nullable.class)

    then:
    result.getField() == subject.getField()
    result.getElementType() == subject.getElementType()
  }
}
