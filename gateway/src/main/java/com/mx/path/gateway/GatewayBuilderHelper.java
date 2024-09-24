package com.mx.path.gateway;

import com.mx.path.core.utility.reflection.ClassHelper;
import com.mx.path.gateway.accessor.Accessor;
import com.mx.path.gateway.behavior.GatewayBehavior;
import com.mx.path.gateway.service.GatewayService;

/**
 * This is to work around the fact that Lombok {@link lombok.experimental.SuperBuilder} does not allow
 * use a common builder class. This uses reflection to invoke builder methods that are
 * available on all descendents and BaseGateway.
 */
public class GatewayBuilderHelper {
  /**
   * ClassHelper instance with "build" method white listed.
   * It requires access to a volatile member which, by default, cannot be invoked via reflection.
   */
  private static final ClassHelper H = new ClassHelper("build");

  /**
   * Add behavior to object.
   *
   * @param builder builder
   * @param behavior behavior
   */
  public static void addBehavior(Object builder, GatewayBehavior behavior) {
    H.invokeMethod(builder, "behavior", behavior);
  }

  /**
   * Add service to object.
   *
   * @param builder builder
   * @param service service
   */
  public static void addService(Object builder, GatewayService service) {
    H.invokeMethod(builder, "service", service);
  }

  /**
   * Build object.
   *
   * @param builder builder
   * @param returnType object type
   * @return object
   * @param <T> object type
   */
  public static <T> T build(Object builder, Class<T> returnType) {
    return H.invokeMethod(returnType, builder, "build");
  }

  /**
   * Set object root accessor.
   *
   * @param builder builder
   * @param accessor accessor
   */
  public static void setRootAccessor(Object builder, Accessor accessor) {
    H.invokeMethod(builder, "baseAccessor", accessor);
  }

  /**
   * Set object client id.
   *
   * @param builder builder
   * @param clientId client id
   */
  public static void setClientId(Object builder, String clientId) {
    H.invokeMethod(builder, "clientId", clientId);
  }

  /**
   * Return builder.
   *
   * @param klass klass
   * @return object
   */
  public static Object getBuilder(Class<?> klass) {
    return H.invokeStaticMethod(Object.class, klass, "builder");
  }
}
