package com.mx.path.core.context.util

import javax.xml.datatype.XMLGregorianCalendar

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.mx.path.core.context.util.XMLGregorianCalendarConverter

import spock.lang.Specification

class XMLGregorianCalendarConverterTest extends Specification {
  Gson gson

  def setup() {
    gson = new GsonBuilder()
        .registerTypeAdapter(XMLGregorianCalendar, new XMLGregorianCalendarConverter.Serializer())
        .registerTypeAdapter(XMLGregorianCalendar, new XMLGregorianCalendarConverter.Deserializer())
        .create()
  }

  def "serialize produces XML format string"() {
    given:
    def cal = javax.xml.datatype.DatatypeFactory.newInstance().newXMLGregorianCalendar("2024-01-15")

    when:
    def json = gson.toJson(cal, XMLGregorianCalendar)

    then:
    json == '"2024-01-15"'
  }

  def "deserialize parses XML format string back to calendar"() {
    given:
    def json = '"2024-01-15"'

    when:
    def cal = gson.fromJson(json, XMLGregorianCalendar)

    then:
    cal != null
    cal.year == 2024
    cal.month == 1
    cal.day == 15
  }

  def "deserialize returns null for invalid format"() {
    given:
    def json = '"not-a-date"'

    when:
    def cal = gson.fromJson(json, XMLGregorianCalendar)

    then:
    cal == null
  }
}
