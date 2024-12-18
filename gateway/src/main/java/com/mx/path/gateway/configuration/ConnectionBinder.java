package com.mx.path.gateway.configuration;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import com.mx.path.core.common.collection.ObjectMap;
import com.mx.path.core.common.connect.AccessorConnectionSettings;
import com.mx.path.core.common.gateway.GatewayException;
import com.mx.path.core.common.lang.Strings;
import com.mx.path.gateway.connect.filter.CallbacksFilter;
import com.mx.path.gateway.connect.filter.ErrorHandlerFilter;
import com.mx.path.gateway.connect.filter.FaultTolerantRequestFilter;
import com.mx.path.gateway.connect.filter.RequestFinishedFilter;
import com.mx.path.gateway.connect.filter.TracingFilter;
import com.mx.path.gateway.connect.filter.UpstreamRequestEventFilter;
import com.mx.path.gateway.connect.filter.UpstreamRequestProcessorFilter;

/**
 * Builds connections for gateway objects.
 */
public class ConnectionBinder {

  /**
   * Build new connection instance.
   *
   * @param klass class type of connection.
   * @param map connections configuration map.
   * @param connectionName connection name.
   * @return new instance of connection.
   * @param <T> generic class type.
   */
  public static <T> Object build(Class<T> klass, ObjectMap map, String connectionName) {
    ConfigurationState state = ConfigurationState.getCurrent();
    AtomicReference<ConnectionConstructionContext> builderRef = new AtomicReference<>();
    state.withLevel("connections." + connectionName, () -> {
      String clientId = map.getMap(connectionName).getMap("configurations").getAsString("clientId");
      if (clientId == null) {
        throw new ConfigurationError("Client ID not provided for connection " + connectionName, state);
      }

      AccessorConnectionSettings connection = buildConnection(map, connectionName);
      builderRef.set(new ConnectionConstructionContext(clientId, state, klass, connection));
    });
    ConnectionConstructionContext builder = builderRef.get();
    return builder.build();
  }

  /**
   * Helper function to build new AccessorConnectionSettings for given connection.
   *
   * @param map connection configuration object map.
   * @param connectionName connection name.
   * @return new AccessorConnectionSettings instance.
   */
  public static AccessorConnectionSettings buildConnection(ObjectMap map, String connectionName) {
    AccessorConnectionSettings.AccessorConnectionSettingsBuilder connection = AccessorConnectionSettings.builder();
    connection.baseUrl(map.getMap(connectionName).getAsString("baseUrl"));
    connection.certificateAlias(map.getMap(connectionName).getAsString("certificateAlias"));
    connection.keystorePath(map.getMap(connectionName).getAsString("keystorePath"));
    String passwordString = map.getMap(connectionName).getAsString("keystorePassword");
    if (passwordString != null) {
      connection.keystorePassword(passwordString.toCharArray());
    }
    connection.skipHostNameVerify(Boolean.parseBoolean(String.valueOf(map.getMap(connectionName).get("skipHostNameVerify"))));

    // Default request filters
    // todo: Provide way to configure the request filters in connection block
    connection.baseRequestFilter(new TracingFilter());
    connection.baseRequestFilter(new UpstreamRequestEventFilter());
    connection.baseRequestFilter(new ErrorHandlerFilter());
    connection.baseRequestFilter(new CallbacksFilter());
    connection.baseRequestFilter(new UpstreamRequestProcessorFilter());
    connection.baseRequestFilter(new RequestFinishedFilter());
    connection.baseRequestFilter(new FaultTolerantRequestFilter());

    AccessorConnectionSettings instance = connection.build();
    validate(instance);

    if (map.getMap(connectionName).getMap("configurations") != null) {
      map.getMap(connectionName).getMap("configurations").forEach(connection::configuration);
    }

    return instance;
  }

  /**
   * Validate AccessorConnectionSettings object.
   *
   * @param connection object to be validated.
   */
  private static void validate(AccessorConnectionSettings connection) {
    String keystorePassword = "";
    if (connection.getKeystorePassword() != null) {
      keystorePassword = new String(connection.getKeystorePassword());
    }

    if (Strings.isNotBlank(connection.getCertificateAlias()) || Strings.isNotBlank(connection.getKeystorePath()) || Strings.isNotBlank(keystorePassword)) {
      Map<String, String> values = new LinkedHashMap<>();
      values.put("certificateAlias", connection.getCertificateAlias());
      values.put("keystorePath", connection.getKeystorePath());
      values.put("keystorePassword", keystorePassword);

      List<String> missingKeys = values.entrySet().stream().filter((e) -> Strings.isBlank(e.getValue())).map(Map.Entry::getKey).collect(Collectors.toList());

      if (!missingKeys.isEmpty()) {
        throw new GatewayException("Invalid connection details. Missing " + String.join(", ", missingKeys));
      }
    }
  }
}
