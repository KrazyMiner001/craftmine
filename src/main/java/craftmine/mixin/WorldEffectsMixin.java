package craftmine.mixin;

import craftmine.config.Config;
import craftmine.util.MinesRegistryEntriesHelper;
import net.minecraft.aprilfools.WorldEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(WorldEffects.class)
public class WorldEffectsMixin {
    @Inject(method = "method_70146", at = @At("HEAD"), cancellable = true)
    private static void lambda(ServerWorld serverLevel, ServerPlayerEntity serverPlayer, ItemStack itemStack, CallbackInfoReturnable<Boolean> cir) {
        if (!Config.HANDLER.instance().enableDryLandsFix) return;
        cir.cancel();
        cir.setReturnValue(false);
        Optional<RegistryEntry.Reference<Biome>> biome = MinesRegistryEntriesHelper.removeLevelPrefix(serverLevel.getBiome(serverPlayer.getBlockPos()), serverLevel.createCommandRegistryWrapper(RegistryKeys.BIOME), RegistryKeys.BIOME);
        if (biome.isEmpty()) return;
        cir.setReturnValue(biome.get().isIn(BiomeTags.IS_BADLANDS) && itemStack.isOf(Items.LAVA_BUCKET));
    }
}
