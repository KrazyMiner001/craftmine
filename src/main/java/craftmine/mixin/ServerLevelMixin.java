package craftmine.mixin;

import craftmine.config.Config;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.WorldModifiers;
import net.minecraft.world.level.mines.WorldEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(ServerLevel.class)
public abstract class ServerLevelMixin {
    @Inject(method = "cleanInventoryAndReward", at = @At("HEAD"), cancellable = true)
    private void cleanInventoryAndReward(ServerPlayer serverPlayer, float immediateXpPortion, CallbackInfo ci) {
        if (!Config.HANDLER.instance().enableExpCalcFix) return;

        double xpReward = 10.0;
        List<ItemStack> list = new ArrayList<>();

        for (ItemStack itemStack : serverPlayer.getInventory()) {
            WorldModifiers worldModifiers = itemStack.get(DataComponents.WORLD_MODIFIERS);
            if (worldModifiers != null) {
                for (WorldEffect worldEffect : worldModifiers.effects()) {
                    if (itemStack.has(DataComponents.WORLD_EFFECT_UNLOCK)) {
                        ((ServerLevel) (Object) this).unlockEffect(worldEffect);
                    }
                }
            } else if (itemStack.is(ItemTags.CARRY_OVER)) {
                list.add(itemStack.copy());
            } else {
                xpReward += itemStack.getOrDefault(DataComponents.EXCHANGE_VALUE, Item.NO_EXCHANGE).getValue(serverPlayer, itemStack);
            }

            CriteriaTriggers.INVENTORY_CASHED_IN.trigger(serverPlayer, serverPlayer.blockPosition(), itemStack);
        }

        serverPlayer.getInventory().clearContent();
        float worldEffectsMultiplier = 1.0F;

        for (WorldEffect worldEffect2 : ((ServerLevel) (Object) this).getActiveEffects()) {
            worldEffectsMultiplier *= worldEffect2.experienceModifier();
        }

        xpReward *= worldEffectsMultiplier;
        xpReward *= serverPlayer.getAttributeValue(Attributes.EXPERIENCE_GAIN_MODIFIER);
        xpReward *= Config.HANDLER.instance().bonusMult;
        int immediateXp = (int)(xpReward * immediateXpPortion);
        int xpAsOrbs = (int)(xpReward * (1.0F - immediateXpPortion));
        serverPlayer.giveExperiencePoints(immediateXp);

        for (ItemStack itemStack2 : list) {
            serverPlayer.addHubReward(itemStack2);
        }

        ((ServerLevelAccessor) serverPlayer.serverLevel()).getMineData().addExperienceToDrop(xpAsOrbs);
        ((ServerLevelAccessor) this).getTheGame().playerList().broadcastSystemMessage(Component.translatable("mine.won.rewards", serverPlayer.getName(), xpAsOrbs), false);

        ci.cancel();
    }
}
