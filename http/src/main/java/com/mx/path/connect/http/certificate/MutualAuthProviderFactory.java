package com.mx.path.connect.http.certificate;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.mx.path.core.common.connect.ConnectionSettings;
import com.mx.path.core.common.lang.Strings;

/**
 * Factory class for creating and managing instances of {@link MutualAuthProvider}.
 */
public class MutualAuthProviderFactory {
  private static Map<Integer, MutualAuthProvider> mutualAuthProviderInstances = new HashMap<>();

  /**
   * Build and return {@link MutualAuthProvider} based on provided {@link ConnectionSettings}.
   *
   * @param settings {@link ConnectionSettings} used to build the {@link MutualAuthProvider}
   * @return {@link MutualAuthProvider} instance if mutual authentication is enabled and settings are valid;
   * null if mutual authentication is not enabled or settings are invalid
   */
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

  /**
   * Validate provided {@link ConnectionSettings} to ensure they are properly configured.
   *
   * @param settings {@link ConnectionSettings} to be validated
   * @throws FieldSettingsValidationError if mutual authentication settings do not have a matching provider
   * or if the provider fails validation
   */
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

  /**
   * Determine whether mutual authentication is enabled based on the provided {@link ConnectionSettings}.
   *
   * @param settings {@link ConnectionSettings} used to check if mutual authentication is enabled
   * @return true if mutual authentication is enabled; false otherwise
   */
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
