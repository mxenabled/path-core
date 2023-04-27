package com.mx.path.gateway.event;

import com.mx.path.core.common.accessor.Accessor;

/**
 * Interface for all Accessor-emitted events
 */
public interface AccessorEvent {

  /**
   * @return The accessor emitting the event
   */
  Accessor getCurrentAccessor();

}
