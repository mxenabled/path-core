package com.mx.path.testing.util

import static DeepReflectionEquals.deepRefEq
import static org.mockito.Mockito.mock
import static org.mockito.Mockito.verify

import com.mx.testing.data.Address
import com.mx.testing.data.User
import com.mx.testing.data.UserService

import org.mockito.exceptions.verification.opentest4j.ArgumentsAreDifferent

import spock.lang.Specification

class DeepReflectionEqualsTest extends Specification {

  UserService mockUserService

  def setup() {
    mockUserService = mock(UserService)
  }

  def "should successfully verify invocation when actual and expected objects are deeply equal"() {
    given:
    def actualUser = new User(id: 1, name: "Bob", address: new Address(street: "123 Main St", zipCode: "11111"))
    def expectedUser = new User(id: 1, name: "Bob", address: new Address(street: "123 Main St", zipCode: "11111"))

    when:
    mockUserService.save(actualUser)

    then:
    verify(mockUserService).save(deepRefEq(expectedUser))
  }

  def "should successfully verify invocation when ignoring a mismatched field"() {
    given:
    def actualUser = new User(id: 1, name: "Bob", address: new Address(street: "123 Main St", zipCode: "11111"))
    def expectedUser = new User(id: 2, name: "Bob", address: new Address(street: "123 Main St", zipCode: "11111")) // ID mismatch

    when:
    mockUserService.save(actualUser)

    then:
    verify(mockUserService).save(deepRefEq(expectedUser, "id"))
  }

  def "should fail verification when ignoring the wrong field"() {
    given:
    def actualUser = new User(id: 1, name: "Bob", address: new Address(street: "123 Main St", zipCode: "11111"))
    def expectedUser = new User(id: 2, name: "Bob", address: new Address(street: "123 Main St", zipCode: "11111")) // ID mismatch
    mockUserService.save(actualUser)

    when:
    verify(mockUserService).save(deepRefEq(expectedUser, "name"))

    then:
    thrown(ArgumentsAreDifferent)
  }

  def "should fail verification when a nested field value does not match"() {
    given:
    def actualUser = new User(id: 1, name: "Bob", address: new Address(street: "123 Main St", zipCode: "11111"))
    def expectedUser = new User(id: 1, name: "Bob", address: new Address(street: "123 Main St", zipCode: "22222")) // zip code mismatch
    mockUserService.save(actualUser)

    when:
    verify(mockUserService).save(deepRefEq(expectedUser))

    then:
    thrown(ArgumentsAreDifferent)
  }
}
