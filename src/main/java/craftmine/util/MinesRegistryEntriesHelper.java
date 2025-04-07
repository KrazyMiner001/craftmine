package craftmine.util;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;

public class MinesRegistryEntriesHelper {
    public static <T> Optional<Holder.Reference<T>> removeLevelPrefix(Holder<T> holder, HolderGetter<T> holderGetter, ResourceKey<Registry<T>> registry) {
        Optional<ResourceKey<T>> resourceKey = holder.unwrapKey();
        if (resourceKey.isEmpty()) return Optional.empty();

        ResourceKey<T> key = resourceKey.get();

        ResourceLocation resourceLocation = key.location();

        String[] splitResourceLocation = resourceLocation.getPath().split("/");
        if (splitResourceLocation.length != 2) return Optional.empty();
        if (!splitResourceLocation[0].matches("level[0-9]+")) return Optional.empty();

        ResourceKey<T> remappedKey = ResourceKey.create(
                registry,
                new ResourceLocation(
                        resourceLocation.getNamespace(),
                        splitResourceLocation[1]
                )
        );

        return holderGetter.get(remappedKey);
    }
}
