package com.mx.models.ondemand

import java.time.LocalDate

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.dataformat.xml.XmlFactory
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator
import com.mx.models.account.Account
import com.mx.models.account.Transaction
import com.mx.models.ondemand.mixins.AccountXmlMixin
import com.mx.models.ondemand.mixins.MixinDefinition
import com.mx.testing.WithMockery

import spock.lang.Specification

class MdxOnDemandSerializerTest extends Specification implements WithMockery {
  MdxOnDemandSerializer subject
  ToXmlGenerator generator
  StringWriter stringWriter

  void setup() {
    subject = new MdxOnDemandSerializer()
    stringWriter = new StringWriter()
    generator = new XmlFactory().createGenerator(stringWriter)
  }

  def "unwrapped, interacts with generator"() {
    given:
    def account = new Account().tap {
      id = "A-123"
      name = "Checking"
      balance = 0.09
      fullAccountNumber = "DONT_RENDER_ME"
    }

    when:
    subject.serialize(account, (JsonGenerator) generator, (SerializerProvider) null)
    generator.flush()

    then:
    stringWriter.toString() == "<Account>\n" +
        "  <wrapped>false</wrapped>\n" +
        "  <balance>0.09</balance>\n" +
        "  <id>A-123</id>\n" +
        "  <name>Checking</name>\n" +
        "</Account>\n"
  }

  def "wrapped, interacts with generator"() {
    given:
    def account = new Account().tap {
      id = "A-123"
      name = "Checking"
      balance = 0.09
      fullAccountNumber = "DONT_RENDER_ME"
    }

    when:
    subject.serialize(account.wrapped(), (JsonGenerator) generator, (SerializerProvider) null)
    generator.flush()

    then:
    stringWriter.toString() == "<mdx version=\"5.0\">\n" +
        "<Account>\n" +
        "  <wrapped>true</wrapped>\n" +
        "  <balance>0.09</balance>\n" +
        "  <id>A-123</id>\n" +
        "  <name>Checking</name>\n" +
        "</Account>\n" +
        "</mdx>\n"
  }

  def "applies mixins"() {
    given:
    subject = new MdxOnDemandSerializer(new MixinDefinition(Account, AccountXmlMixin))

    def account = new Account().tap {
      id = "A-123"
      name = "Checking"
      balance = 0.09
      fullAccountNumber = "DONT_RENDER_ME"
      paymentDueOn = LocalDate.of(2020, 12, 5)
    }

    when:
    subject.serialize(account.wrapped(), (JsonGenerator) generator, (SerializerProvider) null)
    generator.flush()

    then:
    stringWriter.toString() == "<mdx version=\"5.0\">\n" +
        "<account>\n" +
        "  <balance>0.09</balance>\n" +
        "  <id>A-123</id>\n" +
        "  <name>Checking</name>\n" +
        "  <payment_due_on>2020-12-05</payment_due_on>\n" +
        "</account>\n" +
        "</mdx>\n"
  }

  def "serializes LocalDate to string"() {
    given:
    def transaction = new Transaction().tap {
      postedOn = LocalDate.of(2020, 01, 12)
    }

    when:
    subject.serialize(transaction, (JsonGenerator) generator, (SerializerProvider) null)
    generator.flush()

    then:
    stringWriter.toString() == "<Transaction>\n" +
        "  <wrapped>false</wrapped>\n" +
        "  <posted_on>2020-01-12</posted_on>\n" +
        "</Transaction>\n"
  }

  def "serializes large amounts"() {
    given:
    def account = new Account().tap {
      id = "A-123"
      name = "Checking"
      balance = 30000000.00
      fullAccountNumber = "DONT_RENDER_ME"
    }

    when:
    print("BALANCE: "+account.balance)
    subject.serialize(account.wrapped(), (JsonGenerator) generator, (SerializerProvider) null)
    generator.flush()

    then:
    stringWriter.toString() == "<mdx version=\"5.0\">\n" +
        "<Account>\n" +
        "  <wrapped>true</wrapped>\n" +
        "  <balance>30000000.00</balance>\n" +
        "  <id>A-123</id>\n" +
        "  <name>Checking</name>\n" +
        "</Account>\n" +
        "</mdx>\n"
  }
}
