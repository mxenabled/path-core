package com.mx.path.connect.http.certificate;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.mx.path.core.common.connect.ConnectionSettings;
import com.mx.path.core.common.lang.Strings;

public class MutualAuthProviderFactory {
  private static Map<Integer, MutualAuthProvider> mutualAuthProviderInstances = new HashMap<>();

  public static MutualAuthProvider build(ConnectionSettings settings) {
    if (settings == null || !isMutualAuthEnabled(settings)) {
      return null;
    }

    if (!mutualAuthProviderInstances.containsKey(settings.mutualAuthProviderHashcode())) {
      synchronized (MutualAuthProviderFactory.class) {
        if (!mutualAuthProviderInstances.containsKey(settings.mutualAuthProviderHashcode())) {
          mutualAuthProviderInstances.put(settings.mutualAuthProviderHashcode(), buildProvider(settings));
        }
      }
    }

    return mutualAuthProviderInstances.get(settings.mutualAuthProviderHashcode());
  }

  public static void validateSettings(ConnectionSettings settings) throws FieldSettingsValidationError {
    if (!isMutualAuthEnabled(settings)) {
      return;
    }

    MutualAuthProvider provider = build(settings);

    if (Objects.isNull(provider)) {
      throw new FieldSettingsValidationError("Mutual auth settings do not have a matching provider");
    }

    provider.validate();
  }

  public static boolean isMutualAuthEnabled(ConnectionSettings settings) {
    return Strings.isNotBlank(settings.getKeystorePath())
        || Objects.nonNull(settings.getKeystorePassword())
        || Strings.isNotBlank(settings.getCertificateAlias());
  }

  static void reset() {
    mutualAuthProviderInstances.clear();
  }

  // Private

  private static MutualAuthProvider buildProvider(ConnectionSettings settings) {
    if (Strings.isNotBlank(settings.getKeystorePath())
        || Objects.nonNull(settings.getKeystorePassword())
        || Strings.isNotBlank(settings.getCertificateAlias())) {
      return new MutualAuthProviderKeystore(settings);
    }

    return null;
  }
}
