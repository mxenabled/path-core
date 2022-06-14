package com.mx.models.ondemand

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.mx.models.account.Account

import spock.lang.Specification

class MdxOnDemandDeserializerTest extends Specification {
  MdxOnDemandDeserializer<Account> subject
  void setup() {
    subject = new MdxOnDemandDeserializer<>(Account, "/account")
  }

  def "deserializes"() {
    given:
    XmlMapper mapper = new XmlMapper()
    JsonParser parser = mapper.getFactory().createParser("<mdx><account><balance>0.09</balance><id>A-123</id><name>Checking</name></account></mdx>")

    when:
    def account = subject.deserialize(parser, null)

    then:
    account.getId() == "A-123"
    account.getBalance() == 0.09
  }
}
