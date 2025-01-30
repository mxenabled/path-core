package com.mx.path.core.common.security

import com.google.gson.GsonBuilder
import com.google.gson.JsonElement

import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class LogValueRegexTest extends Specification {
  def logValueMasker = new LogValueMasker()
  def gson = new GsonBuilder().create()
  def sampleJson = ""

  @Shared
  def maskedString = "**MASKED**"

  void setup() {
    sampleJson = "{\n" +
        "  \"test_string_field_1\": \"test string field 1 value\",\n" +
        "  \"test_number_field_1\": 100,\n" +
        "  \"test_number_field_2\": 100.0,\n" +
        "  \"test_number_field_3\": 1.0E+2,\n" +
        "  \"test_number_field_4\": 1E+2,\n" +
        "  \"test_number_field_5\": 3.1415926,\n" +
        "  \"test_number_field_6\": -466.22,\n" +
        "  \"test_array_field_1\": [],\n" +
        "  \"test_array_field_2\": [ ],\n" +
        "  \"test_array_field_3\": [\"item1\", \"item2\", \"item3\"],\n" +
        "  \"test_array_field_4\": [1, 2, 3],\n" +
        "  \"test_array_field_5\": [{ \"id\": \"object1\" }, { \"id\": \"object2\" }, { \"id\": \"object3\" }],\n" +
        "  \"test_array_field_6\": [\n" +
        "    {\n" +
        "      \"id\": \"object1\"\n" +
        "    },\n" +
        "    {\n" +
        "      \"id\": \"object2\"\n" +
        "    },\n" +
        "    {\n" +
        "      \"id\": \"object3\"\n" +
        "    }\n" +
        "  ]\n" +
        "}"
  }

  void cleanup() {
    LogValueMasker.clearPatterns()
  }

  def "jsonString"() {
    given:
    def regex = LogValueRegex.jsonString("test_string_field_1")

    when:
    LogValueMasker.registerPayloadPattern(regex)
    def result = logValueMasker.maskPayload(sampleJson)
    def jsonObject = gson.fromJson(result, JsonElement.class)
    def jsonElement = jsonObject.asJsonObject.get("test_string_field_1")

    then:
    jsonElement.asString == maskedString
  }

  @Unroll
  def "jsonNumber"() {
    when:
    def regex = LogValueRegex.jsonNumber(fieldName)
    LogValueMasker.registerPayloadPattern(regex)
    def result = logValueMasker.maskPayload(sampleJson)
    def jsonObject = gson.fromJson(result, JsonElement.class)
    def jsonElement = jsonObject.asJsonObject.get(fieldName)

    then:
    jsonElement.asString == expectedResult

    where:
    fieldName             || expectedResult
    "test_number_field_1" || maskedString
    "test_number_field_2" || maskedString
    "test_number_field_3" || maskedString
    "test_number_field_4" || maskedString
    "test_number_field_5" || maskedString
    "test_number_field_6" || maskedString
  }

  @Unroll
  def "jsonArray"() {
    when:
    def regex = LogValueRegex.jsonArray(fieldName)
    LogValueMasker.registerPayloadPattern(regex)
    def result = logValueMasker.maskPayload(sampleJson)
    def jsonObject = gson.fromJson(result, JsonElement.class)
    def jsonElement = jsonObject.asJsonObject.get(fieldName)
    def jsonArray = jsonElement.asJsonArray

    then:
    def arraySize = jsonArray.size()
    arraySize == expectedArraySize
    if (jsonArray.size() == 1) {
      jsonArray.get(0).asString == expectedResult
    }

    where:
    fieldName            || expectedArraySize || expectedResult
    "test_array_field_1" || 0                 || ""
    "test_array_field_2" || 0                 || ""
    "test_array_field_3" || 1                 || maskedString
    "test_array_field_4" || 1                 || maskedString
    "test_array_field_5" || 1                 || maskedString
    "test_array_field_6" || 1                 || maskedString
  }
}
