// ---------------------------------------------------------------------------------------------------------------------
//   GENERATED FILE - ** Do not edit **
//   Wrapped Class: com.mx.accessors.account.AccountBaseAccessor
// ---------------------------------------------------------------------------------------------------------------------
package com.mx.testing.accessors.proxy.account;

import lombok.Getter;

import com.mx.path.core.common.model.ModelList;
import com.mx.path.gateway.accessor.AccessorConfiguration;
import com.mx.path.gateway.accessor.AccessorResponse;
import com.mx.path.gateway.configuration.AccessorConstructionContext;
import com.mx.path.gateway.configuration.AccessorProxy;
import com.mx.testing.accessors.AccountBaseAccessor;
import com.mx.testing.accessors.TransactionBaseAccessor;
import com.mx.testing.model.Account;

/**
 * Base class for wrapping com.mx.accessors.account.AccountBaseAccessor.
 *
 * <p>Used to provide scoped construction strategies.
 */
public abstract class AccountBaseAccessorProxy extends AccountBaseAccessor implements AccessorProxy {
  @Getter
  private final AccessorConstructionContext<? extends AccountBaseAccessor> accessorConstructionContext;

  public AccountBaseAccessorProxy(AccessorConfiguration configuration,
      Class<? extends AccountBaseAccessor> accessorClass) {
    super(configuration);
    this.accessorConstructionContext = new AccessorConstructionContext<AccountBaseAccessor>(accessorClass, configuration);
  }

  @Override
  public Class<? extends AccountBaseAccessor> getAccessorClass() {
    return accessorConstructionContext.getAccessorClass();
  }

  /**
   * @return get accessor
   */
  @Override
  public AccessorResponse<Account> get(String id) {
    return build().get(id);
  }

  /**
   * @return list accessor
   */
  @Override
  public AccessorResponse<ModelList<Account>> list() {
    return build().list();
  }

  /**
   * @return transactions accessor
   */
  @Override
  public TransactionBaseAccessor transactions() {
    if (getTransactions() != null) {
      return getTransactions();
    }
    return build().transactions();
  }

  /**
   * Create an instance of Accessor type klass
   *
   * <p>Override to change how the accessor is constructed.
   *
   * @param klass
   * @return Accessor
   */
  protected AccountBaseAccessor buildAccessor() {
    return accessorConstructionContext.build();
  }

  public abstract AccountBaseAccessor build();
}
