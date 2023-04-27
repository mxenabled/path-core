package com.mx.path.connect.messaging.remote;

import com.mx.path.connect.messaging.MessageHeaders;
import com.mx.path.connect.messaging.MessageParameters;
import com.mx.path.connect.messaging.MessageRequest;
import com.mx.path.connect.messaging.MessageResponse;
import com.mx.path.core.common.messaging.MessageError;
import com.mx.path.core.common.messaging.MessageStatus;
import com.mx.path.core.common.model.ModelBase;
import com.mx.path.core.common.model.ModelList;
import com.mx.path.core.common.model.ParameterizedTypeImpl;

/**
 * Remote model with create, update, get, list, and delete operations
 *
 * @param <T> The MdxBase Model
 */
public class RemoteCRUDModel<T extends ModelBase<?>> extends RemoteRequester<T> {
  private final String clientId;

  public RemoteCRUDModel(String clientId) {
    this.clientId = clientId;
  }

  /**
   * Delete T by id
   */
  public void delete(String id) {
    MessageRequest request = MessageRequest.builder()
        .messageHeaders(new MessageHeaders())
        .messageParameters(new MessageParameters())
        .model(RemoteChannel.getModel(getClassOfT()))
        .operation("delete")
        .messageParameters(MessageParameters.builder()
            .id(id)
            .build())
        .build();

    MessageResponse response = executeRequest(clientId, request);

    if (response.getStatus() != MessageStatus.SUCCESS) {
      throw new MessageError("Unable to list remote " + RemoteChannel.getModel(getClassOfT()) + " with status " + response.getStatus(), response.getStatus(), response.getException());
    }
  }

  /**
   * Get instance of T by id
   * @return requested T
   */
  public T get(String id) {
    MessageRequest request = MessageRequest.builder()
        .messageHeaders(new MessageHeaders())
        .messageParameters(new MessageParameters())
        .model(RemoteChannel.getModel(getClassOfT()))
        .operation("get")
        .messageParameters(MessageParameters.builder()
            .id(id)
            .build())
        .build();

    MessageResponse response = executeRequest(clientId, request);

    if (response.getStatus() != MessageStatus.SUCCESS) {
      throw new MessageError("Unable to list remote " + RemoteChannel.getModel(getClassOfT()) + " with status " + response.getStatus(), response.getStatus(), response.getException());
    }

    return response.getBodyAs(getClassOfT());
  }

  /**
   * @return list of T
   */
  public ModelList<T> list() {
    MessageRequest request = MessageRequest.builder()
        .messageHeaders(new MessageHeaders())
        .messageParameters(new MessageParameters())
        .model(RemoteChannel.getModel(getClassOfT()))
        .operation("list")
        .build();

    MessageResponse response = executeRequest(clientId, request);

    if (response.getStatus() != MessageStatus.SUCCESS) {
      throw new MessageError("Unable to update remote " + RemoteChannel.getModel(getClassOfT()) + " with status " + response.getStatus(), response.getStatus(), response.getException());
    }

    return response.getBodyAs(new ParameterizedTypeImpl<>(getClassOfT()));
  }

  /**
   * @return updated T
   */
  public T update(String id, T obj) {
    MessageRequest request = MessageRequest.builder()
        .messageHeaders(new MessageHeaders())
        .messageParameters(new MessageParameters())
        .model(RemoteChannel.getModel(getClassOfT()))
        .operation("update")
        .body(obj)
        .messageParameters(MessageParameters.builder()
            .id(id)
            .build())
        .build();

    MessageResponse response = executeRequest(clientId, request);

    if (response.getStatus() != MessageStatus.SUCCESS) {
      throw new MessageError("Unable to update remote " + RemoteChannel.getModel(getClassOfT()) + " with status " + response.getStatus(), response.getStatus(), response.getException());
    }

    return response.getBodyAs(getClassOfT());
  }

  /**
   * @return created T
   */
  public T create(T obj) {
    MessageRequest request = MessageRequest.builder()
        .messageHeaders(new MessageHeaders())
        .messageParameters(new MessageParameters())
        .model(RemoteChannel.getModel(getClassOfT()))
        .operation("create")
        .body(obj)
        .messageParameters(MessageParameters.builder()
            .build())
        .build();

    MessageResponse response = executeRequest(clientId, request);

    if (response.getStatus() != MessageStatus.SUCCESS) {
      throw new MessageError("Unable to create remote " + RemoteChannel.getModel(getClassOfT()) + " with status " + response.getStatus(), response.getStatus(), response.getException());
    }

    return response.getBodyAs(getClassOfT());
  }
}
