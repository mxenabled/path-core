package com.mx.path.gateway.configuration;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

import com.mx.path.core.common.collection.ObjectArray;
import com.mx.path.core.common.collection.ObjectMap;
import com.mx.path.gateway.behavior.GatewayBehavior;

public class BehaviorStackConfigurator {

  private final ConfigurationState state;
  private final GatewayObjectConfigurator gatewayObjectConfigurator;

  @Getter
  @Setter
  private ObjectArray rootBehaviors;

  public BehaviorStackConfigurator(ConfigurationState state) {
    this.state = state;
    this.gatewayObjectConfigurator = new GatewayObjectConfigurator(state);
  }

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

      // todo: Add behavior priority sorting
      addBehaviors(behaviors, behaviorsMap, clientId);

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
