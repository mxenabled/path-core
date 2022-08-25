package com.mx.common.request

import spock.lang.Specification

class FeatureTest extends Specification {
  def "valueOf(lower_case_mx_feature) results in IllegalArgumentException"() {
    when:
    Feature.valueOf("managed_cards") == Feature.MANAGED_CARDS

    then:
    def e = thrown(IllegalArgumentException)
    e.message == "No enum constant com.mx.common.request.Feature.managed_cards"
  }

  def "valueOf(UPPER_CASE_MX_FEATURE) results in successful Feature enum lookup"() {
    expect:
    Feature.valueOf("MANAGED_CARDS") == Feature.MANAGED_CARDS
  }
}