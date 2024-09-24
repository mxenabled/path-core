package com.mx.path.core.common.serialization;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import com.mx.path.core.common.collection.ObjectArray;
import com.mx.path.core.common.collection.ObjectMap;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.representer.Representer;

/**
 * This class serializes and deserializes YAML documents for version 1.1 of the YAML spec
 * into an ObjectMap/ObjectArray data structure.
 *
 * Additional information: https://yaml.org/spec/1.1/
 */
public class ObjectMapYamlDeserializer {
  private static final int DEFAULT_MAX_YAML_ALIASES = 50;

  @Data
  @Builder
  public static class Parameters {
    /**
     * Protects against https://en.wikipedia.org/wiki/Billion_laughs_attack.
     *
     * -- GETTER --
     * Return maximum number of aliases.
     *
     * @return maximum number of aliases
     * -- SETTER --
     * Set maximum number of aliases.
     *
     * @param maxYamlAliases maximum number of aliases to set
     */
    @Builder.Default
    private int maxYamlAliases = DEFAULT_MAX_YAML_ALIASES; //
  }

  /**
   * -- GETTER --
   * Return parameters.
   *
   * @return parameters
   * -- SETTER --
   * Set parameters.
   *
   * @param parameters parameters to set
   */
  @Setter
  @Getter
  private Parameters parameters;

  /**
   * Default constructor.
   */
  public ObjectMapYamlDeserializer() {
    parameters = Parameters.builder().build();
  }

  /**
   * Build new {@link ObjectMapYamlDeserializer} instance with specified parameters.
   *
   * @param parameters parameters
   */
  public ObjectMapYamlDeserializer(Parameters parameters) {
    this.parameters = parameters;
  }

  /**
   * Build new object from YAML.
   *
   * @param document string obtained from yaml
   * @return deserialized object
   */
  public final Object fromYaml(String document) {
    LoaderOptions options = new LoaderOptions();
    options.setMaxAliasesForCollections(getParameters().getMaxYamlAliases());
    Yaml yaml = new Yaml(new ObjectMapConstructor(options), new Representer(), new DumperOptions(), options);
    Object root = yaml.load(document);

    // If the root node is a Map we need to convert it to an ObjectMap
    // before we convert any nested Maps into ObjectMaps.
    if (root instanceof Map) {
      ObjectMap map = new ObjectMap();
      map.putAll((Map) root);
      root = map;
    }
    populateObjectMaps(root);
    return root;
  }

  /**
   * Create string representing YAML object.
   *
   * @param yaml object
   * @return string representation
   */
  public final String toYaml(Object yaml) {
    return new Yaml().dump(yaml);
  }

  /**
   * There is no easy way for us to instruct org.yaml.snakeyaml.Yaml to use ObjectMaps as our default map,
   * so we have to recursively traverse the YAML document and convert LinkedHashMaps into ObjectMaps.
   *
   * @param root Object
   */
  private void populateObjectMaps(Object root) {
    // If we are looking at an ObjectMap we need to iterate over its members and convert any LinkedHashMaps
    // to ObjectMaps and recursively repeat this step for any nested LinkedHashMaps.
    if (root instanceof ObjectMap) {
      ObjectMap map = (ObjectMap) root;
      for (Map.Entry<String, Object> entry : map.entrySet()) {
        Object converted = tryToConvertToObjectMap(entry.getValue());
        map.put(entry.getKey(), converted);
        populateObjectMaps(converted);
      }
    } else if (root instanceof ObjectArray) {
      // If we are looking at an ObjectArray we need to do the same thing: iterate over its members, convert
      // LinkedHashMaps to ObjectMaps, and then recursively repeat the process for nested items.
      ObjectArray array = (ObjectArray) root;
      for (int i = 0; i < array.size(); ++i) {
        Object converted = tryToConvertToObjectMap(array.get(i));
        array.set(i, converted);
        populateObjectMaps(converted);
      }
    }
  }

  /**
   * If an object is a Map we create a new ObjectMap and copy the contents of the Map into the ObjectMap and return it.
   * Otherwise, we simply return the Object.
   *
   * @param object Object
   * @return Object
   */
  private Object tryToConvertToObjectMap(Object object) {
    if (object instanceof Map) {
      Map<String, Object> map = (Map<String, Object>) object;
      ObjectMap objectMap = new ObjectMap();
      objectMap.putAll(map);
      return objectMap;
    }
    return object;
  }

  /**
   * This class allows us to overwrite the default List and Map that org.yaml.snakeyaml.Yaml uses.
   * Unfortunately there is no way to specify an ObjectMap as the default Map; however, we can at least
   * set it to a LinkedHashMap (which ObjectMap extends).
   */
  private static class ObjectMapConstructor extends Constructor {

    /**
     * Build new {@link ObjectMapConstructor} instance with specified options.
     *
     * @param loaderOptions options
     */
    ObjectMapConstructor(LoaderOptions loaderOptions) {
      super(loaderOptions);
    }

    @Override
    protected List<Object> createDefaultList(int initSize) {
      return new ObjectArray();
    }

    @Override
    protected Map<Object, Object> createDefaultMap(int initSize) {
      return new LinkedHashMap<>(initSize);
    }
  }
}
