package com.mx.path.core.common.collection

import com.mx.testing.collection.OrderedFirst
import com.mx.testing.collection.OrderedIndifferent1
import com.mx.testing.collection.OrderedIndifferent2
import com.mx.testing.collection.OrderedLast
import com.mx.testing.collection.OrderedUnspecified

import spock.lang.Specification

class OrderComparatorTest extends Specification {

  def "first and last"() {
    given:
    def input = [
      new OrderedLast(),
      new OrderedFirst()
    ]

    when:
    input.sort(new OrderComparator())

    then:
    input[0].class == OrderedFirst
    input[1].class == OrderedLast
  }

  def "multiple first does not affect order"() {
    given:
    def first1 = new OrderedFirst()
    def first2 = new OrderedFirst()
    def first3 = new OrderedFirst()
    def input = [first2, first3, first1]

    when:
    input.sort(new OrderComparator())

    then:
    input[0] == first2
    input[1] == first3
    input[2] == first1
  }

  def "multiple last does not affect order"() {
    given:
    def last1 = new OrderedLast()
    def last2 = new OrderedLast()
    def last3 = new OrderedLast()
    def input = [last2, last3, last1]

    when:
    input.sort(new OrderComparator())

    then:
    input[0] == last2
    input[1] == last3
    input[2] == last1
  }

  def "indifferent does not affect order"() {
    given:
    def indifferent1 = new OrderedIndifferent1()
    def indifferent2 = new OrderedIndifferent2()
    def indifferent3 = new OrderedIndifferent1()
    def input = [
      indifferent2,
      null,
      indifferent3,
      null,
      indifferent1
    ]

    when:
    input.sort(new OrderComparator())

    then:
    input[0] == indifferent2
    input[1] == null
    input[2] == indifferent3
    input[3] == null
    input[4] == indifferent1
  }

  def "simple sort"() {
    given:
    def first1 = new OrderedFirst()
    def indifferent1 = new OrderedIndifferent1()
    def last1 = new OrderedLast()

    def input = [last1, indifferent1, first1]

    when:
    input.sort(new OrderComparator())

    then:
    verifyAll {
      input[0] == first1
      input[1] == indifferent1
      input[2] == last1
    }
  }

  def "sorts"() {
    given:
    def first1 = new OrderedFirst()
    def first2 = new OrderedFirst()
    def indifferent1 = new OrderedIndifferent1()
    def indifferent2 = new OrderedIndifferent2()
    def unspecified = new OrderedUnspecified()
    def last1 = new OrderedLast()
    def last2 = new OrderedLast()

    def input = [
      indifferent2,
      first1,
      last2,
      unspecified,
      last1,
      indifferent1,
      null,
      first2
    ]

    when:
    input.sort(new OrderComparator())

    then:
    verifyAll {
      input[0] == first1
      input[1] == first2
      input[2] == indifferent2
      input[3] == unspecified
      input[4] == indifferent1
      input[5] == null
      input[6] == last2
      input[7] == last1
    }
  }
}
