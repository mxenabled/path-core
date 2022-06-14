package com.mx.path.model.context;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class AccountBehaviors {

  // Fields

  private Map<String, AccountBehavior> accountBehaviors = new HashMap<>();

  // Static

  public static AccountBehaviors loadFromSession() {
    return Session.current().getAccountBehaviors();
  }

  // Getter/setters

  public final Map<String, AccountBehavior> getAccountBehaviors() {
    return accountBehaviors;
  }

  public final void setAccountBehaviors(Map<String, AccountBehavior> accountBehaviors) {
    this.accountBehaviors = accountBehaviors;
  }

  // Public

  public final String serviceAccountIdToAccountId(Session.ServiceIdentifier service, String serviceAccountId) {
    for (final Iterator<Entry<String, AccountBehavior>> iterator = accountBehaviors.entrySet().iterator(); iterator.hasNext();) {
      Entry<String, AccountBehavior> entry = iterator.next();
      if (serviceAccountId.equals(entry.getValue().getMappedAccountIds().get(service))) {
        return entry.getKey();
      }
    }
    return null;
  }

  public final void saveToSession() {
    Session.current().setAccountBehaviors(this);
  }

  /**
   * @param accountId - MX account external guid
   * @param accountBehavior - The associated AccountBehavior
   */
  public final void put(String accountId, AccountBehavior accountBehavior) {
    accountBehaviors.put(accountId, accountBehavior);
  }

  /**
   * @param accountId - MX account external guid
   * @return The associated AccountBehavior
   */
  public final AccountBehavior get(String accountId) {
    return accountBehaviors.get(accountId);
  }
}
