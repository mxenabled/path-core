package com.mx.path.gateway.context;

import com.mx.common.accessors.Accessor;
import com.mx.common.gateway.GatewayException;
import com.mx.common.session.ServiceScope;
import com.mx.path.gateway.configuration.AccessorProxy;
import com.mx.path.model.context.ScopeKeyGenerator;
import com.mx.path.utilities.reflection.ClassHelper;

public enum Scope implements ScopeKeyGenerator {
  Session {
    @Override
    public String generate() {
      return "Session";
    }
  },
  Service {
    @Override
    public String generate() {
      GatewayRequestContext requestContext = GatewayRequestContext.current();
      if (requestContext == null || requestContext.getCurrentAccessor() == null && requestContext.getGateway() == null) {
        return Session.generate();
      }

      Accessor accessor = requestContext.getCurrentAccessor();

      if (accessor instanceof AccessorProxy) {
        accessor = new ClassHelper().invokeMethod(Accessor.class, accessor, "build");
      }

      if (accessor.getClass().isAnnotationPresent(ServiceScope.class)) {
        return accessor.getClass().getAnnotation(ServiceScope.class).value();
      }

      Accessor baseAccessor = requestContext
          .getGateway()
          .getBaseAccessor();

      if (AccessorProxy.class.isAssignableFrom(baseAccessor.getClass())) {
        baseAccessor = ((AccessorProxy) baseAccessor).build();
      }

      if (!baseAccessor.getClass().isAnnotationPresent(ServiceScope.class)) {
        throw new GatewayException("BaseAccessor: " + baseAccessor.getClass().getCanonicalName() + " is missing @ServiceScope annotation");
      }
      return baseAccessor.getClass().getAnnotation(ServiceScope.class).value();
    }
  };
}
