package com.mx.models.ondemand;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mx.models.MdxWrappable;

/**
 * Custom deserializer for MDX OnDemand Resources
 *
 * Digs out the node using the nodePath locator, then deserializes.
 * @param <T>
 */
public class MdxOnDemandDeserializer<T extends MdxWrappable<?>> extends JsonDeserializer<T> {
  private static final ObjectMapper MAPPER = new ObjectMapper();

  private final String nodePath;
  private final Class<?> target;

  @SuppressWarnings("unchecked")
  public MdxOnDemandDeserializer(Class<?> target, String nodePath) {
    super();
    this.target = target;
    this.nodePath = nodePath;
  }

  @Override
  @SuppressWarnings("unchecked")
  public final T deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
    TreeNode root = p.readValueAsTree();
    TreeNode value = root.at(JsonPointer.compile(nodePath));

    return (T) MAPPER.convertValue(value, target);
  }
}
