package com.mx.path.gateway.context;

import java.lang.reflect.Type;
import java.util.Objects;
import java.util.function.Supplier;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.mx.common.lang.Strings;
import com.mx.models.MdxList;
import com.mx.models.account.Account;
import com.mx.path.model.context.Session;

@Deprecated
public class SessionAccountCache<T extends Account> {

  // Statics

  private static final String ACCOUNT_CACHE_KEY = "sessionAccountCache";
  private static final String ACCOUNT_CACHE_STATE_KEY = "sessionAccountCacheState";
  private static final Gson GSON = new GsonBuilder().create();

  // Fields

  private Session session;
  private Supplier<MdxList<T>> accountSupplier;

  // Constructors

  public SessionAccountCache(Session session) {
    this.accountSupplier = null;
    this.session = session;
  }

  // Getter/Setter

  /**
   * @param accountSupplier to be called if the account cache is null or invalid
   */
  public final void setAccountSupplier(Supplier<MdxList<T>> accountSupplier) {
    this.accountSupplier = accountSupplier;
  }

  /**
   * @param newAccountSupplier to be called if the account cache is null or invalid
   * @return this
   */
  public final SessionAccountCache<T> withAccountSupplier(Supplier<MdxList<T>> newAccountSupplier) {
    setAccountSupplier(newAccountSupplier);
    return this;
  }

  // Public

  public final MdxList<T> getAccounts() {
    return getAccounts(new TypeToken<MdxList<Account>>() {
    }.getType());
  }

  /**
   * Because java generics are impossible to work with....
   * If you have a specialized account stored in the account cache you need to provide the type to deserialize it.
   *
   * Example:
   *
   * { @code
   * cache.getAccounts(new TypeToken<MdxList<SpecialAccount>>() {}.getType());
   * }
   *
   * @param accountListType type of cached array
   * @return Account cache
   */
  public final MdxList<T> getAccounts(Type accountListType) {
    if (isValid()) {
      String accountsJson = session.get(Session.ServiceIdentifier.Session, ACCOUNT_CACHE_KEY);

      if (Strings.isNotBlank(accountsJson)) {
        MdxList<T> result = GSON.fromJson(accountsJson, accountListType);
        if (Objects.nonNull(result)) {
          return result;
        }
      }

      // If we get here, then the cache is not valid.
      invalidate();
    }

    MdxList<T> accounts = getFromSupplier();

    if (Objects.nonNull(accounts)) {
      setAccounts(accounts);
    }

    return accounts;
  }

  private MdxList<T> getFromSupplier() {
    if (Objects.nonNull(accountSupplier)) {
      return accountSupplier.get();
    }

    return null;
  }

  public final void invalidate() {
    session.put(Session.ServiceIdentifier.Session, ACCOUNT_CACHE_STATE_KEY, "INVALID");
  }

  /**
   * Save account list to cache and set cache as valid
   *
   * @param accounts
   */
  public final void setAccounts(MdxList<T> accounts) {
    session.sput(Session.ServiceIdentifier.Session, ACCOUNT_CACHE_KEY, GSON.toJson(accounts));
    setValid(true);
  }

  public final boolean isValid() {
    return Objects.equals("VALID", session.get(Session.ServiceIdentifier.Session, ACCOUNT_CACHE_STATE_KEY));
  }

  public final void setValid(boolean valid) {
    if (valid) {
      session.put(Session.ServiceIdentifier.Session, ACCOUNT_CACHE_STATE_KEY, "VALID");
    } else {
      session.put(Session.ServiceIdentifier.Session, ACCOUNT_CACHE_STATE_KEY, "INVALID");
    }
  }
}
