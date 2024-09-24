package com.mx.path.gateway.configuration;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

import com.mx.path.core.common.collection.ObjectArray;
import com.mx.path.core.common.collection.ObjectMap;
import com.mx.path.core.common.collection.OrderComparator;
import com.mx.path.gateway.behavior.GatewayBehavior;

/**
 * Configurator for building a stack of {@link GatewayBehavior} instances.
 */
public class BehaviorStackConfigurator {

  private final ConfigurationState state;
  private final GatewayObjectConfigurator gatewayObjectConfigurator;

  private final Comparator<Object> comparator = new OrderComparator();

  /**
   * -- GETTER --
   * Return root behaviors array.
   *
   * @return root behaviors array
   * -- SETTER --
   * Set root behaviors array.
   *
   * @param rootBehaviors root behaviors array to set
   */
  @Getter
  @Setter
  private ObjectArray rootBehaviors;

  /**
   * Build new {@link BehaviorStackConfigurator} instance with specified configuration state.
   *
   * @param state state
   */
  public BehaviorStackConfigurator(ConfigurationState state) {
    this.state = state;
    this.gatewayObjectConfigurator = new GatewayObjectConfigurator(state);
  }

  /**
   * Builds a list of {@link GatewayBehavior} instances from the given configuration map.
   *
   * @param map the configuration map containing behavior definitions.
   * @param clientId the client ID associated with this configuration, passed to each behavior for context.
   * @return a list of {@link GatewayBehavior} instances constructed from the configuration.
   */
  public final List<GatewayBehavior> buildFromNode(ObjectMap map, String clientId) {
    return state.withLevel("behaviors", () -> {
      List<GatewayBehavior> behaviors = new ArrayList<>();
      ObjectArray behaviorsMap = new ObjectArray();

      //always first the root behaviors to maintain the sequence order
      if (getRootBehaviors() != null) {
        behaviorsMap.addAll(getRootBehaviors());
      }

      if (map.getArray("behaviors") != null) {
        behaviorsMap.addAll(map.getArray("behaviors"));
      }

      addBehaviors(behaviors, behaviorsMap, clientId);
      behaviors.sort(comparator);

      return behaviors;
    });
  }

  private void addBehaviors(List<GatewayBehavior> behaviors, ObjectArray behaviorsMap, String clientId) {
    if (behaviorsMap == null) {
      return;
    }

    behaviorsMap.forEach((node) -> {
      behaviors.add(gatewayObjectConfigurator.buildFromNode((ObjectMap) node, clientId, GatewayBehavior.class));
    });
  }
}
