package craftmine.mixin;

import craftmine.config.Config;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.impl.launch.FabricLauncherBase;
import net.fabricmc.loader.impl.lib.mappingio.tree.MappingTree;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.aprilfools.WorldEffect;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.WorldModifiersComponent;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin {
    @Inject(method = "method_69076", at = @At("HEAD"), cancellable = true)
    private void cleanInventoryAndReward(ServerPlayerEntity serverPlayer, float immediateXpPortion, CallbackInfo ci) {
        if (!Config.HANDLER.instance().enableExpCalcFix) return;

        String namespace = Objects.equals(FabricLoader.getInstance().getMappingResolver().getCurrentRuntimeNamespace(), "intermediary") ? "official" : "named";
        MappingTree mappings = FabricLauncherBase.getLauncher().getMappingConfiguration().getMappings();

        double xpReward = 10.0;
        List<ItemStack> list = new ArrayList<>();

        Method worldEffectsMethod;
        try {
            worldEffectsMethod = WorldModifiersComponent.class.getMethod(Objects.requireNonNull(Objects.requireNonNull(mappings.getMethod("net/minecraft/class_11056", "comp_3946", null, 0)).getName(namespace)));
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        for (ItemStack itemStack : serverPlayer.getInventory()) {
            WorldModifiersComponent worldModifiers = itemStack.get(DataComponentTypes.WORLD_MODIFIERS);
            if (worldModifiers != null) {
                try {
                    @SuppressWarnings("unchecked") List<WorldEffect> worldEffects = (List<WorldEffect>) worldEffectsMethod.invoke(worldModifiers);
                    for (WorldEffect worldEffect : worldEffects) {
                        if (itemStack.contains(DataComponentTypes.WORLD_EFFECT_UNLOCK)) {
                            //noinspection DataFlowIssue
                            ((ServerWorld) (Object) this).method_69083(worldEffect);
                        }
                    }
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            } else if (itemStack.isIn(ItemTags.CARRY_OVER)) {
                list.add(itemStack.copy());
            } else {
                xpReward += itemStack.getOrDefault(DataComponentTypes.EXCHANGE_VALUE, Item.DEFAULT_EXCHANGE_VALUE).getValue(serverPlayer, itemStack);
            }

            Criteria.INVENTORY_CASHED_IN.trigger(serverPlayer, serverPlayer.getBlockPos(), itemStack);
        }

        serverPlayer.getInventory().clear();
        float worldEffectsMultiplier = 1.0F;

        MappingTree.MethodMapping experienceModifierMethodMapping = mappings.getMethod("net/minecraft/class_11109", "comp_4004", null, 0);
        Method experienceModifierMethod;
        try {
            assert experienceModifierMethodMapping != null;
            experienceModifierMethod = WorldEffect.class.getMethod(Objects.requireNonNull(experienceModifierMethodMapping.getName(namespace)));
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        for (WorldEffect worldEffect2 : ((ServerWorld) (Object) this).method_69125()) {
            try {
                worldEffectsMultiplier *= (float) experienceModifierMethod.invoke(worldEffect2);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }

        xpReward *= worldEffectsMultiplier;
        xpReward *= serverPlayer.getAttributeValue(EntityAttributes.EXPERIENCE_GAIN_MODIFIER);
        xpReward *= Config.HANDLER.instance().bonusMult;
        int immediateXp = (int)(xpReward * immediateXpPortion);
        int xpAsOrbs = (int)(xpReward * (1.0F - immediateXpPortion));
        serverPlayer.addExperience(immediateXp);

        for (ItemStack itemStack2 : list) {
            serverPlayer.method_69131(itemStack2);
        }

        ((ServerWorldAccessor) serverPlayer.getServerWorld()).getMineData().addExperienceToDrop(xpAsOrbs);
        ((ServerWorldAccessor) this).getGameInstance().getPlayerManager().broadcast(Text.translatable("mine.won.rewards", serverPlayer.getName(), xpAsOrbs), false);

        ci.cancel();
    }
}
