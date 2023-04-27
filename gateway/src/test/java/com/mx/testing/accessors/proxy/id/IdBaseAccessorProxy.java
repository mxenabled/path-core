package com.mx.testing.accessors.proxy.id;

import lombok.Getter;

import com.mx.path.gateway.accessor.AccessorConfiguration;
import com.mx.path.gateway.accessor.AccessorResponse;
import com.mx.path.gateway.configuration.AccessorConstructionContext;
import com.mx.path.gateway.configuration.AccessorProxy;
import com.mx.testing.accessors.IdBaseAccessor;
import com.mx.testing.model.Authentication;

public abstract class IdBaseAccessorProxy extends IdBaseAccessor implements AccessorProxy {
  @Getter
  private final AccessorConstructionContext<? extends IdBaseAccessor> accessorConstructionContext;

  public IdBaseAccessorProxy(AccessorConfiguration configuration,
      Class<? extends IdBaseAccessor> accessorClass) {
    super(configuration);
    this.accessorConstructionContext = new AccessorConstructionContext<IdBaseAccessor>(accessorClass, configuration);
  }

  @Override
  public Class<? extends IdBaseAccessor> getAccessorClass() {
    return accessorConstructionContext.getAccessorClass();
  }

  @Override
  public AccessorResponse<Authentication> authenticate(Authentication authentication) {
    return build().authenticate(authentication);
  }

  /**
   * Create an instance of Accessor type klass
   *
   * <p>Override to change how the accessor is constructed.
   *
   * @return Accessor
   */
  protected IdBaseAccessor buildAccessor() {
    return accessorConstructionContext.build();
  }

  public abstract IdBaseAccessor build();
}
