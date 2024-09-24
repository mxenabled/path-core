package com.mx.path.connect.messaging;

import java.util.function.Supplier;

import com.mx.path.core.common.messaging.MessageBroker;
import com.mx.path.core.common.messaging.MessageError;
import com.mx.path.core.common.messaging.MessageStatus;
import com.mx.path.core.context.facility.Facilities;

/**
 * A {@link Supplier} implementation that provides instances of {@link MessageBroker}.
 */
public final class FacilityMessageBrokerSupplier implements Supplier<MessageBroker> {
  private final String clientId;

  /**
   * Build new {@link FacilityMessageBrokerSupplier} with specified client ID.
   *
   * @param clientId client ID used to identify the client
   */
  public FacilityMessageBrokerSupplier(String clientId) {
    this.clientId = clientId;
  }

  @Override
  public MessageBroker get() {
    if (clientId == null) {
      throw new MessageError("clientId required to provide MessageBroker from Facilities", MessageStatus.DISABLED, null);
    }
    return Facilities.getMessageBroker(clientId);
  }
}
