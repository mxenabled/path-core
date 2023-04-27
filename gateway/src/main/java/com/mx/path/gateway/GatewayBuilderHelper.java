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

  public static void addBehavior(Object builder, GatewayBehavior behavior) {
    H.invokeMethod(builder, "behavior", behavior);
  }

  public static void addService(Object builder, GatewayService service) {
    H.invokeMethod(builder, "service", service);
  }

  public static <T> T build(Object builder, Class<T> returnType) {
    return H.invokeMethod(returnType, builder, "build");
  }

  public static void setRootAccessor(Object builder, Accessor accessor) {
    H.invokeMethod(builder, "baseAccessor", accessor);
  }

  public static void setClientId(Object builder, String clientId) {
    H.invokeMethod(builder, "clientId", clientId);
  }

  public static Object getBuilder(Class<?> klass) {
    return H.invokeStaticMethod(Object.class, klass, "builder");
  }
}
