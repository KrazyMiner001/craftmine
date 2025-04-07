package craftmine.mixin;

import craftmine.config.Config;
import craftmine.util.MinesRegistryEntriesHelper;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.mines.WorldEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(WorldEffects.class)
public class WorldEffectsMixin {
    @Inject(method = "method_70146", at = @At("HEAD"), cancellable = true)
    private static void lambda(ServerLevel serverLevel, ServerPlayer serverPlayer, ItemStack itemStack, CallbackInfoReturnable<Boolean> cir) {
        if (!Config.HANDLER.instance().enableDryLandsFix) return;
        cir.cancel();
        cir.setReturnValue(false);
        Optional<Holder.Reference<Biome>> biome = MinesRegistryEntriesHelper.removeLevelPrefix(serverLevel.getBiome(serverPlayer.blockPosition()), serverLevel.holderLookup(Registries.BIOME), Registries.BIOME);
        if (biome.isEmpty()) return;
        cir.setReturnValue(biome.get().is(BiomeTags.IS_BADLANDS) && itemStack.is(Items.LAVA_BUCKET));
    }
}
