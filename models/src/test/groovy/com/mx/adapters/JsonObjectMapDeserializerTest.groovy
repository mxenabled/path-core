package com.mx.adapters

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.mx.common.collections.ObjectArray
import com.mx.common.collections.ObjectMap

import spock.lang.Specification

class JsonObjectMapDeserializerTest extends Specification {
  Gson subject

  def setup() {
    subject = new GsonBuilder().registerTypeAdapter(ObjectMap.class, new JsonObjectMapDeserializer()).create()
  }

  def "deserializes JSON into an ObjectMap"() {
    given:
    def json = '{"fictionalLawyers":["Atticus Finch","Saul Goodman","Phoenix Wright",{"randomObject":true}]}'

    when:
    def result = subject.fromJson(json, ObjectMap.class)

    then:
    result instanceof ObjectMap
    result['fictionalLawyers'] instanceof ObjectArray

    def array = (ObjectArray) result['fictionalLawyers']
    array.size() == 4
    array[0] == "Atticus Finch"
    array[1] == "Saul Goodman"
    array[2] == "Phoenix Wright"
    array[3] instanceof ObjectMap

    def subObject = (ObjectMap) array[3]
    subObject["randomObject"] == true
  }
}
