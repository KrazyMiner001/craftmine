package craftmine.util;

import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

import java.util.Optional;

public class MinesRegistryEntriesHelper {
    public static <T> Optional<RegistryEntry.Reference<T>> removeLevelPrefix(RegistryEntry<T> registryEntry, RegistryEntryLookup<T> registryEntryLookup, RegistryKey<Registry<T>> registry) {
        Optional<RegistryKey<T>> resourceKey = registryEntry.getKey();
        if (resourceKey.isEmpty()) return Optional.empty();

        RegistryKey<T> key = resourceKey.get();

        Identifier identifier = key.getValue();

        String[] splitIdentifier = identifier.getPath().split("/");
        if (splitIdentifier.length != 2) return Optional.empty();
        if (!splitIdentifier[0].matches("level[0-9]+")) return Optional.empty();

        RegistryKey<T> remappedKey = RegistryKey.of(
                registry,
                new Identifier(
                        identifier.getNamespace(),
                        splitIdentifier[1]
                )
        );

        return registryEntryLookup.getOptional(remappedKey);
    }
}
