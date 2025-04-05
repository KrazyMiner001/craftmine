package craftmine.mixin;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.UnlockCondition;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.mines.WorldEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

@Mixin(WorldEffect.class)
public abstract class WorldEffectMixin {
    @Inject(method = "builder", at = @At("TAIL"), cancellable = true)
    private static void builder(String string, CallbackInfoReturnable<WorldEffect.Builder> cir) {
        if (!Objects.equals(string, "dry_land")) return;

        WorldEffect.Builder builder = new WorldEffect.Builder(string);
        builder.unlockedBy(
                UnlockCondition.obtainedItem(
                        (serverLevel, serverPlayer, itemStack) -> {
                            if (!itemStack.is(Items.LAVA_BUCKET)) return false;
                            Holder<Biome> biome = serverLevel.getBiome(serverPlayer.blockPosition());
                            if (biome.unwrapKey().isEmpty()) return false;

                            ResourceKey<Biome> key = biome.unwrapKey().get();
                            Optional<Holder.Reference<Biome>> remappedBiome = serverLevel.holderLookup(
                                    Registries.BIOME
                            ).get(
                                    ResourceKey.create(
                                            Registries.BIOME,
                                            new ResourceLocation(
                                                    key.location().getNamespace(),
                                                    Arrays.stream(key.location().getPath().split("/")).toList().getLast()
                                            )
                                    )
                            );

                            return remappedBiome.map(biomeReference -> biomeReference.is(BiomeTags.IS_BADLANDS)).orElse(false);
                        }
                )
        );

        cir.setReturnValue(builder);
        cir.cancel();
    }

}
