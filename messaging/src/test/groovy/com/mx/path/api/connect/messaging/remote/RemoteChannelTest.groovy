package com.mx.path.api.connect.messaging.remote

import com.mx.path.api.connect.messaging.MessageEvent
import com.mx.path.api.connect.messaging.MessageRequest
import com.mx.testing.Account
import com.mx.testing.RemoteAccount

import spock.lang.Specification

class RemoteChannelTest extends Specification {
  def "builds model name from type"() {
    when:
    def model = RemoteChannel.getModel(RemoteChannelTest)

    then:
    model == "RemoteChannelTest"
  }

  def "builds request channel"() {
    when:
    def channel = RemoteChannel.buildRequestChannel("clientId", RemoteAccount, "list_some")

    then:
    channel == "path.request.clientId.RemoteAccount.list_some"

    when: "given MessageRequest"
    channel = RemoteChannel.buildRequestChannel("clientId", RemoteAccount, MessageRequest.builder().operation("list_some").build())

    then:
    channel == "path.request.clientId.RemoteAccount.list_some"
  }

  def "builds event channel"() {
    when:
    def channel = RemoteChannel.buildEventChannel("clientId", RemoteAccount, "something_happened")

    then:
    channel == "path.event.clientId.RemoteAccount.something_happened"

    when: "given MessageEvent"
    channel = RemoteChannel.buildEventChannel("clientId", RemoteAccount, MessageEvent.builder().event("something_happened").build())

    then:
    channel == "path.event.clientId.RemoteAccount.something_happened"
  }

  def "parse with valid channel"() {
    when:
    def subject = RemoteChannel.parse("path.request.client.Account.list")

    then:
    subject.getModel() == "Account"
    subject.getClientId() == "client"
    subject.getType() == RemoteChannel.ChannelType.REQUEST
    subject.getOperation() == "list"
  }

  def "parse with invalid channel"() {
    when:
    def subject = RemoteChannel.parse("junk.in.here")

    then:
    thrown(MalformedChannelException)
  }

  def "getter/setters"() {
    given:
    def subject = RemoteChannel.builder().build()

    when:
    subject.setClientId("client123")
    subject.setModel("Account")
    subject.setType(RemoteChannel.ChannelType.REQUEST)
    subject.setOperation("list")

    then:
    subject.getClientId() == "client123"
    subject.getModel() == "Account"
    subject.getType() == RemoteChannel.ChannelType.REQUEST
    subject.getOperation() == "list"
  }

  def "builder"() {
    when:
    def subject = RemoteChannel.builder()
        .clientId("client1")
        .model("Account")
        .operation("list")
        .type(RemoteChannel.ChannelType.REQUEST)
        .build();

    then:
    subject.getClientId() == "client1"
    subject.getOperation() == "list"
    subject.getModel() == "Account"
    subject.getType() == RemoteChannel.ChannelType.REQUEST
  }

  def "builder sets model"() {
    when: "from String"
    def subject = RemoteChannel.builder()
        .model("Account")
        .build();

    then:
    subject.getModel() == "Account"

    when: "from Class"
    subject = RemoteChannel.builder().model(Account.class).build()

    then:
    subject.getModel() == "Account"
  }

  def "toString builds request channel"() {
    when:
    def subject = RemoteChannel.builder()
        .model(Account)
        .clientId("client123")
        .type(RemoteChannel.ChannelType.REQUEST)
        .operation("list")
        .build();

    then:
    subject.toString() == "path.request.client123.Account.list"

    when: "invalid type"
    subject.setType(null)

    and:
    subject.toString()

    then:
    thrown(IllegalArgumentException)
  }

  def "toString builds event channel"() {
    when:
    def subject = RemoteChannel.builder()
        .model(Account)
        .clientId("client123")
        .type(RemoteChannel.ChannelType.EVENT)
        .operation("changed")
        .build();

    then:
    subject.toString() == "path.event.client123.Account.changed"
  }

  def "channel type fromName"() {
    when:
    def type = RemoteChannel.ChannelType.fromName("request")

    then:
    type == RemoteChannel.ChannelType.REQUEST

    when:
    type = RemoteChannel.ChannelType.fromName("event")

    then:
    type == RemoteChannel.ChannelType.EVENT

    when:
    RemoteChannel.ChannelType.fromName("invalid")

    then:
    thrown(IllegalArgumentException)
  }
}
