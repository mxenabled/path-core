package com.mx.path.gateway.security;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.mx.accessors.ConnectionSettings;
import com.mx.common.lang.Strings;

public class MutualAuthProviderFactory {
  private static Map<ConnectionSettings, MutualAuthProvider> mutualAuthProviderInstances = new HashMap<>();

  public static MutualAuthProvider build(ConnectionSettings settings) {
    if (settings == null || !isMutualAuthEnabled(settings)) {
      return null;
    }

    if (!mutualAuthProviderInstances.containsKey(settings)) {
      synchronized (MutualAuthProviderFactory.class) {
        if (!mutualAuthProviderInstances.containsKey(settings)) {
          mutualAuthProviderInstances.put(settings, buildProvider(settings));
        }
      }
    }

    return mutualAuthProviderInstances.get(settings);
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
