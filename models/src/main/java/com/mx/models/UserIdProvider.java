package com.mx.models;

import java.util.function.Supplier;

/**
 * User ID Provider
 *
 * This static class contains a user id supplier that can be provided by application.
 * This provider is called in the constructor of Mdx Models that need a user id.
 *
 * Example:
 * <pre>
 * UserIdProvider.setUserIdProvider(() -> {
 *   if (Session.current() != null) {
 *     return Session.current().getUserId();
 *   }
 *   return null;
 * });
 * </pre>
 */
public class UserIdProvider {
  private static Supplier<String> userIdProvider = null;

  public static void setUserId(MdxBase<?> obj) {
    if (userIdProvider != null) {
      obj.setUserId(userIdProvider.get());
    }
  }

  public static void setUserIdProvider(Supplier<String> newUserIdProvider) {
    userIdProvider = newUserIdProvider;
  }
}
