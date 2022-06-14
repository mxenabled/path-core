package com.mx.path.api.connect.messaging.remote.models;

import java.lang.reflect.InvocationTargetException;

import lombok.Data;

import com.mx.models.account.Account;

import org.apache.commons.beanutils.BeanUtils;

/**
 * @deprecated Use {@link com.mx.models.account.Account}
 */
@Data
@Deprecated
public class RemoteAccount extends Account {
  public RemoteAccount() {
    super();
  }

  public RemoteAccount(Account account) {
    this();

    try {
      BeanUtils.copyProperties(account, this);
    } catch (IllegalAccessException | InvocationTargetException e) {
      throw new RuntimeException("Error creating RemoteAccount from Account", e);
    }
  }
}
