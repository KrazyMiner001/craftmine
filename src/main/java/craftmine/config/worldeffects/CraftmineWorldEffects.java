package craftmine.config.worldeffects;

import craftmine.config.Config;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.mines.WorldEffect;
import net.minecraft.world.level.mines.WorldEffects;

import static craftmine.util.ResourceLocationHelper.of;

public class CraftmineWorldEffects {
    public static final WorldEffect ETERNAL_DAY = register(
            WorldEffect.builder("eternal_day")
                    .withItemModelOf(Items.LIGHT_BLUE_DYE)
                    .withNameStyle(WorldEffects.CHALLENGE_STYLE)
                    .onMineEnter(serverLevel -> {
                        serverLevel.theGame().overworld().setDayTime(8000L);
                        serverLevel.theGame().getGameRules().getRule(GameRules.RULE_DAYLIGHT).set(false, serverLevel.theGame());
                    })
                    .onMineLeave(serverLevel -> serverLevel.theGame().getGameRules().getRule(GameRules.RULE_DAYLIGHT).set(true, serverLevel.theGame()))
                    .incompatibleWith(WorldEffects.ETERNAL_NIGHT)
                    .xpModifier(0.5F)
                    .build()
    );

    private static <T extends WorldEffect> T register(T worldEffect) {
        if (Config.HANDLER.instance().enableExtraWorldEffects)
            return Registry.register(BuiltInRegistries.WORLD_EFFECT, of(worldEffect.key()), worldEffect);
        return worldEffect;
    }

    public static void init() {}
}
