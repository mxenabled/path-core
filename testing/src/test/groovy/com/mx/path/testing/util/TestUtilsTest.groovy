package com.mx.path.testing.util

import com.mx.testing.data.Address
import com.mx.testing.data.Person
import com.mx.testing.data.User

import spock.lang.Specification

class TestUtilsTest extends Specification {

  def "deepEquals should return true for two deeply equal but different object instances"() {
    given:
    def user1 = new User(id: 1, name: "Alice", address: new Address(street: "123 Main St", zipCode: "11111"))
    def user2 = new User(id: 1, name: "Alice", address: new Address(street: "123 Main St", zipCode: "11111"))

    when:
    TestUtils.deepEquals(user1, user2)

    then:
    noExceptionThrown()
  }

  def "deepEquals should return false when a nested field value does not match"() {
    given:
    def user1 = new User(id: 1, name: "Bob", address: new Address(street: "123 Main St", zipCode: "11111"))
    def user2 = new User(id: 1, name: "Bob", address: new Address(street: "123 Main St", zipCode: "22222")) // zip code mismatch

    when:
    TestUtils.deepEquals(user1, user2)

    then:
    thrown(AssertionError)
  }

  def "deepEquals should return false for objects with different types"() {
    given:
    def obj1 = new User(id: 1, name: "Charlie", address: new Address(street: "456 Side Ave", zipCode: "33333"))
    def obj2 = new Person(id: 1, name: "Charlie", address: new Address(street: "456 Side Ave", zipCode: "33333")) // type mismatch

    when:
    TestUtils.deepEquals(obj1, obj2)

    then:
    thrown(AssertionError)
  }

  def "deepEquals should return false if either object is null"() {
    given:
    def user = new User(id: 1, name: "Alice", address: new Address(street: "123 Main St", zipCode: "11111"))

    when: "obj1 is null"
    TestUtils.deepEquals(null, user)

    then:
    thrown(AssertionError)

    when: "obj2 is null"
    TestUtils.deepEquals(user, null)

    then:
    thrown(AssertionError)
  }
}
