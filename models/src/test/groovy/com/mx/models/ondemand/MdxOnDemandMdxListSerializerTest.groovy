package com.mx.models.ondemand

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.dataformat.xml.XmlFactory
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator
import com.mx.models.MdxList
import com.mx.models.account.Account
import com.mx.models.account.Transaction
import com.mx.models.ondemand.mixins.AccountXmlMixin
import com.mx.models.ondemand.mixins.MixinDefinition
import com.mx.testing.WithMockery

import spock.lang.Specification

class MdxOnDemandMdxListSerializerTest extends Specification implements WithMockery {
  MdxOnDemandMdxListSerializer subject
  ToXmlGenerator generator
  StringWriter stringWriter

  void setup() {
    subject = new MdxOnDemandMdxListSerializer()
    stringWriter = new StringWriter()
    generator = new XmlFactory().createGenerator(stringWriter)
  }

  def "unwrapped, empty, interacts with generator"() {
    given:
    def transactions = new MdxList<Transaction>()
    def list = new MdxListWrapper(null, transactions)

    when:
    subject.serialize(list, (JsonGenerator) generator, (SerializerProvider) null)
    generator.flush()

    then:
    stringWriter.toString() == ""
  }

  def "unwrapped, interacts with generator"() {
    given:
    def transactions = new MdxList<Transaction>()
    transactions.add(new Transaction().tap {
      id = "T-123"
      description = "Fees"
      amount = 9.99
    })
    def list = new MdxListWrapper(null, transactions)

    when:
    subject.serialize(list, (JsonGenerator) generator, (SerializerProvider) null)
    generator.flush()

    then:
    stringWriter.toString() == "<Transaction>\n" +
        "  <wrapped>false</wrapped>\n" +
        "  <amount>9.99</amount>\n" +
        "  <description>Fees</description>\n" +
        "  <id>T-123</id>\n" +
        "</Transaction>\n"
  }

  def "wrapper name, empty interacts with generator"() {
    given:
    def transactions = new MdxList<Transaction>()
    def list = new MdxListWrapper("transactions", transactions)

    when:
    subject.serialize(list, (JsonGenerator) generator, (SerializerProvider) null)
    generator.flush()

    then:
    stringWriter.toString() == "<transactions>\n</transactions>\n"
  }

  def "wrapped, wrapper name, empty interacts with generator"() {
    given:
    def transactions = new MdxList<Transaction>().wrapped()
    def list = new MdxListWrapper("transactions", transactions)

    when:
    subject.serialize(list, (JsonGenerator) generator, (SerializerProvider) null)
    generator.flush()

    then:
    stringWriter.toString() == "<mdx version=\"5.0\">\n" +
        "<transactions>\n</transactions>\n" +
        "</mdx>\n"
  }

  def "wraps list interacts with generator"() {
    given:
    def transactions = new MdxList<Transaction>()
    transactions.add(new Transaction().tap {
      id = "T-123"
      description = "Fees"
      amount = 9.99
    })
    transactions.add(new Transaction().tap {
      id = "T-456"
      description = "Fees"
      amount = 9.99
    })
    def list = new MdxListWrapper("transactions", transactions.wrapped())

    when:
    subject.serialize(list, (JsonGenerator) generator, (SerializerProvider) null)
    generator.flush()

    then:
    stringWriter.toString() == "<mdx version=\"5.0\">\n" +
        "<transactions>\n" +
        "<Transaction>\n" +
        "  <wrapped>false</wrapped>\n" +
        "  <amount>9.99</amount>\n" +
        "  <description>Fees</description>\n" +
        "  <id>T-123</id>\n" +
        "</Transaction>\n" +
        "<Transaction>\n" +
        "  <wrapped>false</wrapped>\n" +
        "  <amount>9.99</amount>\n" +
        "  <description>Fees</description>\n" +
        "  <id>T-456</id>\n" +
        "</Transaction>\n" +
        "</transactions>\n" +
        "</mdx>\n"
  }

  def "wraps list interacts with generator applies mixins"() {
    given:
    subject = new MdxOnDemandMdxListSerializer(
        new MixinDefinition(Account, AccountXmlMixin)
        )

    def transactions = new MdxList<Account>()
    transactions.add(new Account().tap {
      id = "T-123"
      balance = 9.99
      accountMICRNumber = "DONT_RENDER_ME"
    })
    transactions.add(new Account().tap {
      id = "T-456"
      balance = 9.99
      accountMICRNumber = "DONT_RENDER_ME"
    })
    def list = new MdxListWrapper("accounts", transactions.wrapped())

    when:
    subject.serialize(list, (JsonGenerator) generator, (SerializerProvider) null)

    then:
    stringWriter.toString() == "<mdx version=\"5.0\">\n" +
        "<accounts>\n" +
        "<account>\n" +
        "  <balance>9.99</balance>\n" +
        "  <id>T-123</id>\n" +
        "</account>\n" +
        "<account>\n" +
        "  <balance>9.99</balance>\n" +
        "  <id>T-456</id>\n" +
        "</account>\n"
    "</accounts>\n" +
        "</mdx>\n"
  }
}
