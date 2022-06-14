package com.mx.path.api.connect.messaging;

import java.util.function.Supplier;

import com.mx.common.messaging.MessageBroker;
import com.mx.common.messaging.MessageError;
import com.mx.common.messaging.MessageStatus;
import com.mx.path.model.context.facility.Facilities;

public final class FacilityMessageBrokerSupplier implements Supplier<MessageBroker> {
  private final String clientId;

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
