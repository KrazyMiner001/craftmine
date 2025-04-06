package craftmine;

import craftmine.config.Config;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.Commands;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.WorldModifiers;
import net.minecraft.world.level.mines.WorldEffect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Craftmine implements ModInitializer {
	public static final String MOD_ID = "craftmine";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Hello Fabric world!");

		Config.HANDLER.load();

		CommandRegistrationCallback.EVENT.register(
				((commandDispatcher, commandBuildContext, commandSelection) -> {
					commandDispatcher.register(
							Commands.literal("text_exp")
									.executes(context -> {
										ServerPlayer serverPlayer = context.getSource().getPlayer();

										double baseExp = 10.0;

										for (ItemStack itemStack : serverPlayer.getInventory()) {
											WorldModifiers worldModifiers = itemStack.get(DataComponents.WORLD_MODIFIERS);
											if (worldModifiers == null && !itemStack.is(ItemTags.CARRY_OVER))  {
												baseExp += itemStack.getCount() * itemStack.getOrDefault(DataComponents.EXCHANGE_VALUE, Item.NO_EXCHANGE).getValue(serverPlayer, itemStack);
											}
										}

										float wordEffectMult = 1.0F;

										for (WorldEffect worldEffect2 : serverPlayer.serverLevel().getActiveEffects()) {
											wordEffectMult += worldEffect2.experienceModifier();
										}

										int finalExp = (int) (baseExp * wordEffectMult * serverPlayer.getAttributeValue(Attributes.EXPERIENCE_GAIN_MODIFIER));

										context.getSource().sendSuccess(() -> Component.literal(String.valueOf(finalExp)), true);

										return 1;
									})
					);

				})
		);
	}
}