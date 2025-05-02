package svenhjol.charmony.api;

import java.util.List;

/**
 * Provider for secret chest definitions.
 */
public interface SecretChestDefinitionProvider {
    List<SecretChestDefinition> getSecretChestDefinitions();
}
