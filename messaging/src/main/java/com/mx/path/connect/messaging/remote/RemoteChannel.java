package com.mx.path.connect.messaging.remote;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.Builder;
import lombok.Data;

import com.mx.path.connect.messaging.MessageEvent;
import com.mx.path.connect.messaging.MessageRequest;

@Data
@Builder
public class RemoteChannel {
  public enum ChannelType {
    REQUEST("request"),
    EVENT("event");

    private String name;

    ChannelType(String name) {
      this.name = name;
    }

    @Override
    public String toString() {
      return name;
    }

    public static ChannelType fromName(String name) {
      ChannelType status = resolve(name);
      if (status == null) {
        throw new IllegalArgumentException("No matching constant for [" + name + "]");
      }
      return status;
    }

    public static ChannelType resolve(String name) {
      name = name.toLowerCase();
      for (ChannelType status : values()) {
        if (Objects.equals(status.name, name)) {
          return status;
        }
      }

      return null;
    }
  }

  @SuppressWarnings("checkstyle:HiddenField")
  public static class RemoteChannelBuilder {
    private String model;

    public final RemoteChannelBuilder model(String model) {
      this.model = model;
      return this;
    }

    public final RemoteChannelBuilder model(Class<?> klass) {
      this.model = getModel(klass);
      return this;
    }
  }

  private static final String REQUEST_PREFIX = "path." + ChannelType.REQUEST + ".";
  private static final String EVENT_PREFIX = "path." + ChannelType.EVENT + ".";
  private static final Pattern CHANNEL_PATTERN = Pattern.compile("path\\.(?<type>(" + ChannelType.REQUEST + "|" + ChannelType.EVENT + ")+)\\.(?<client>[^.]+)\\.(?<model>[^.]+)\\.(?<operation>[^.]+)");

  private String clientId;
  private String model;
  private String operation;
  private ChannelType type;

  public static RemoteChannel parse(String channel) {
    Matcher matcher = CHANNEL_PATTERN.matcher(channel);
    if (matcher.matches()) {

      return RemoteChannel.builder()
          .clientId(matcher.group("client"))
          .model(matcher.group("model"))
          .operation(matcher.group("operation"))
          .type(ChannelType.fromName(matcher.group("type")))
          .build();
    }

    throw new MalformedChannelException(channel);
  }

  public static String buildEventChannel(String clientId, Class<?> model, MessageEvent messageEvent) {
    return buildEventChannel(clientId, getModel(model), messageEvent.getEvent());
  }

  public static String buildEventChannel(String clientId, Class<?> model, String event) {
    return buildEventChannel(clientId, getModel(model), event);
  }

  public static String buildEventChannel(String clientId, String model, String event) {
    return EVENT_PREFIX + clientId + "." + model + "." + event;
  }

  public static String buildRequestChannel(String clientId, Class<?> model, MessageRequest messageRequest) {
    return buildRequestChannel(clientId, getModel(model), messageRequest.getOperation());
  }

  public static String buildRequestChannel(String clientId, Class<?> model, String operation) {
    return buildRequestChannel(clientId, getModel(model), operation);
  }

  public static String buildRequestChannel(String clientId, String model, String operation) {
    return REQUEST_PREFIX + clientId + "." + model + "." + operation;
  }

  /**
   * Normalized name of the class (T) this is servicing
   *
   * @return normalized class name
   */
  public static String getModel(Class<?> klass) {
    return klass.getSimpleName();
  }

  @Override
  public final String toString() {
    if (type == ChannelType.REQUEST) {
      return buildRequestChannel(clientId, model, operation);
    } else if (type == ChannelType.EVENT) {
      return buildEventChannel(clientId, model, operation);
    }

    throw new IllegalArgumentException("Invalid ChannelType");
  }
}
