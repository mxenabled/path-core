package com.mx.path.core.common.http


import spock.lang.Specification

class MediaTypeTest extends Specification {
  def "creates and toStrings a MediaType"() {
    when:
    def mediaType = new MediaType("application", "vnd.moneydesktop.mdx.v5+json")

    then:
    mediaType.toString() == "application/vnd.moneydesktop.mdx.v5+json"
  }

  def "compares MediaTypes"() {
    when:
    def mediaType = new MediaType("application", "vnd.moneydesktop.mdx.v5+json", Collections.singletonMap("key", "value"))
    def mediaTypeSame = new MediaType("application", "vnd.moneydesktop.mdx.v5+json", Collections.singletonMap("key", "value"))
    def mediaTypeDifferent = new MediaType("application", "vnd.moneydesktop.mdx.v5+xml")

    then:
    mediaType.equals(mediaTypeSame)
    mediaTypeSame.equals(mediaType)
    !mediaType.equals(mediaTypeDifferent)
  }

  def "can check inclusion and compatibility"() {
    when:
    def mediaType = new MediaType("text")
    def mediaTypeSpecific = new MediaType("text", "plain")
    def otherMediaType = new MediaType("application", "json")

    then:
    mediaType.includes(mediaTypeSpecific)
    mediaType.isCompatibleWith(mediaTypeSpecific)
    !mediaType.includes(otherMediaType)
    !mediaType.isCompatibleWith(otherMediaType)
  }

  def "sorts mediaTypes"() {
    given:
    def mediaTypeList = [
      new MediaType("application", "vnd.moneydesktop.mdx.v5+xml"),
      new MediaType("application", "vnd.moneydesktop.mdx.v5+json")
    ]

    when:
    mediaTypeList.sort()

    then:
    mediaTypeList[0].toString() == "application/vnd.moneydesktop.mdx.v5+json"
    mediaTypeList[1].toString() == "application/vnd.moneydesktop.mdx.v5+xml"
  }
}
