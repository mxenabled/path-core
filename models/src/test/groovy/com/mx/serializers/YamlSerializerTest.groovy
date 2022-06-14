package com.mx.serializers

import com.mx.common.collections.ObjectArray
import com.mx.common.collections.ObjectMap

import org.yaml.snakeyaml.error.YAMLException

import spock.lang.Specification

class YamlSerializerTest extends Specification {
  YamlSerializer subject

  def setup() {
    subject = new YamlSerializer()
  }

  def "deserializes and serializes YAML"() {
    given:
    def yaml =
        "fictionalLawyers:\n" +
        "- Atticus Finch\n" +
        "- Saul Goodman\n" +
        "- Phoenix Wright\n" +
        "- randomObject: true"

    when: "deserializing"
    def deserialized = subject.fromYaml(yaml)

    then:
    deserialized instanceof ObjectMap
    deserialized['fictionalLawyers'] instanceof ObjectArray

    def array = (ObjectArray) deserialized['fictionalLawyers']
    array.size() == 4
    array[0] == "Atticus Finch"
    array[1] == "Saul Goodman"
    array[2] == "Phoenix Wright"
    array[3] instanceof ObjectMap

    def subObject = (ObjectMap) array[3]
    subObject["randomObject"] == true

    when: "serializing"
    def result = subject.toYaml(deserialized)

    then:
    deserialized == subject.fromYaml(result)
  }

  def "supports Yaml 1.1 features"() {
    when: "loading simple.yaml"
    def file = new File("src/test/resources/simple.yaml")
    def yaml = String.join("\n", file.readLines("UTF-8"))
    def result = (ObjectMap) subject.fromYaml(yaml)

    then:
    verifySimpleYaml(result)

    when: "loading list.yaml"
    file = new File("src/test/resources/list.yaml")
    yaml = String.join("\n", file.readLines("UTF-8"))
    result = (ObjectArray) subject.fromYaml(yaml)

    then:
    verifyListYaml(result)

    when: "loading from simple.yaml toYaml"
    file = new File("src/test/resources/simple.yaml")
    yaml = String.join("\n", file.readLines("UTF-8"))
    def resultFromFile = (ObjectMap) subject.fromYaml(yaml)
    def dumped = subject.toYaml(resultFromFile)
    def resultFromDumped = (ObjectMap) subject.fromYaml(dumped)

    then:
    verifySimpleYaml(resultFromDumped)

    when: "loading from list.yaml toYaml"
    file = new File("src/test/resources/list.yaml")
    yaml = String.join("\n", file.readLines("UTF-8"))
    resultFromFile = (ObjectArray) subject.fromYaml(yaml)
    dumped = subject.toYaml(resultFromFile)
    resultFromDumped = (ObjectArray) subject.fromYaml(dumped)

    then:
    verifyListYaml(resultFromDumped)
  }

  def "1.1 max aliases"() {
    given:
    def file = new File("src/test/resources/aliasList.yaml")
    def yaml = String.join("\n", file.readLines("UTF-8"))

    when: "document contains too many aliases"
    subject = new YamlSerializer(YamlSerializer.Parameters.builder().maxYamlAliases(2).build())
    subject.fromYaml(yaml)

    then:
    thrown(YAMLException)

    when: "document contains less aliases than limit"
    subject = new YamlSerializer(YamlSerializer.Parameters.builder().maxYamlAliases(10).build())
    subject.fromYaml(yaml)

    then:
    noExceptionThrown()
  }

  def verifySimpleYaml(ObjectMap map) {
    def reference = map.getMap("simpleReference")
    assert reference.get("x") == 1
    assert reference.get("y") == 2

    // 1.1 supports the merge key so we should see "y" overwritten with 3.
    def merge = map.getMap("simpleMerge")
    assert merge.get("x") == 1
    assert merge.get("y") == 3

    return true
  }

  def verifyListYaml(ObjectArray array) {
    def item1 = array.getMap(1).getMap("item1")
    assert item1.get("key1") == "value1"
    assert item1.get("key2") == "value2"

    def item2 = array.getMap(2).getMap("item2")
    assert item2.get("key1") == "value3"
    assert item2.get("key2") == "value2"

    return true
  }
}
