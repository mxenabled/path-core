/**
 * Path Core - Configuration
 *
 * <p>Path Core provides a rich configuration layer to streamline the definition and discovery of configuration bindings
 * along with definable validations
 *
 * <p>Configuration binding draws values in the gateway.yml "configurations" nodes into a configuration POJO that can then
 * be injected into an Accessor, Connection, Behavior, or Facility.
 *
 *  <p>Basic Example:
 *
 *  <p><i>Configuration Definition</i>
 *  <pre>{@code
 *    @Data
 *    public class WidgetConfiguration {
 *      @ConfigurationField
 *      private String apiToken;
 *    }
 *  }</pre>
 *
 *  <p><i>Configuration Injection</i>
 *  <pre>{@code
 *    @Data
 *    public class Widget extends GatewayBehavior {
 *      private WidgetConfiguration configuration;
 *
 *      public Widget(@Configuration WidgetConfiguration configuration) {
 *        this.configuration = configuration;
 *      }
 *
 *      // ...
 *
 *      private void doSomething() {
 *        callTheApi(configuration.getApiToken()); // using the apiToken value
 *      }
 *    }
 *  }</pre>
 *
 *  <p><i>Configuration Values (in gateway.yaml)</i>
 *  <pre>{@code
 *  clientA:
 *    rootBehaviors:
 *      - class: Widget
 *        configurations:
 *          apiToken: 8e7fd8c77e90-55r7f9
 *  }</pre>
 *
 *  <p><strong>Configuration Value Types</strong>
 *
 *  <p>The configuration POJO can have the following types. The configuration binding code will make a best-effort
 *  attempt to coerce the value into the POJO's field.
 *
 *  <ul>
 *    <li>String
 *    <li>Integer
 *    <li>Long
 *    <li>Double
 *    <li>Enumerations (see notes)
 *    <li>Regex Pattern (see notes)
 *    <li>Duration (see notes)
 *    <li>ZoneId (see notes)
 *  </ul>
 *
 *  <p><strong>Enumerations</strong>
 *  <p>Enumerations are matched using a case-insensitive comparison with name() and toString() of the enumerations
 *  values.
 *
 *  <p><strong>Patterns</strong>
 *  <p>Regular expression strings can be coerced into a {@link java.util.regex.Pattern}. Use Embedded Flag Expressions
 *  to specify flags. See https://docs.oracle.com/javase/tutorial/essential/regex/pattern.html and javadocs for
 *  {@link java.util.regex.Pattern} for details.
 *
 *  <p><strong>Durations</strong>
 *  <p>When dealing with time lengths, the use of Durations is preferred rather than using simple integer values. This
 *  avoids ambiguity in the unit of the duration needed and provided. To help facilitate this, {@link java.time.Duration}
 *  types can be used in the configuration POJO and a String value can be provided in the yaml that includes the duration's
 *  value and unit.
 *
 *  <p>For details about supported duration strings and units see {@link com.mx.path.core.common.lang.Durations}.
 *
 *  <p>Duration Configuration Example:
 *
 *  <p><i>Configuration Definition</i>
 *  <pre>{@code
 *    @Data
 *    public class WidgetFacilityConfiguration {
 *      @ConfigurationField
 *      private Duration connectionTimeout;
 *    }
 *  }</pre>
 *
 *  <p><i>Configuration Injection</i>
 *  <pre>{@code
 *    @Data
 *    public class WidgetFacility {
 *      private WidgetFacilityConfiguration configuration;
 *
 *      public WidgetFacility(@Configuration WidgetFacilityConfiguration configuration) {
 *        this.configuration = configuration;
 *      }
 *
 *      public void doSomething() {
 *        // The implementation needs the duration in milliseconds
 *        callWithConnectionTimeout(configuration.getConnectionTimeout().toMillis());
 *      }
 *    }
 *  }</pre>
 *
 *  <p><i>Configuration Values (in gateway.yaml)</i>
 *  <pre>{@code
 *  facilities:
 *    widget:
 *      class: WidgetConfiguration
 *      configurations:
 *        # Providing the timeout in seconds
 *        # Unit doesn't need to match the underlying implementation needs
 *        connectionTimeout: 5s
 *  }</pre>
 *
 *  <p><strong>ZoneId</strong>
 *  <p>Parses provided string using {@link java.time.ZoneId}. Can use offsets (e.g. -08:00),
 *  time-zone name (America/Los_Angelos) or time-zone short id (PST).
 */
package com.mx.path.core.common.configuration;
