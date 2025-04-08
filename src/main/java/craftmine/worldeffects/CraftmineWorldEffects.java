package craftmine.worldeffects;

import craftmine.Craftmine;
import craftmine.config.Config;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.mines.WorldEffect;
import net.minecraft.world.level.mines.WorldEffects;

public class CraftmineWorldEffects {
    public static final WorldEffect ETERNAL_DAY = WorldEffect.builder("eternal_day")
            .withItemModelOf(Items.LIGHT_BLUE_DYE)
            .withNameStyle(WorldEffects.CHALLENGE_STYLE)
            .onMineEnter(serverLevel -> {
                serverLevel.theGame().overworld().setDayTime(8000L);
                serverLevel.theGame().getGameRules().getRule(GameRules.RULE_DAYLIGHT).set(false, serverLevel.theGame());
            })
            .onMineLeave(serverLevel -> serverLevel.theGame().getGameRules().getRule(GameRules.RULE_DAYLIGHT).set(true, serverLevel.theGame()))
            .incompatibleWith(WorldEffects.ETERNAL_NIGHT)
            .xpModifier(0.5F)
            .craftmine$register(Craftmine.MOD_ID, () -> Config.HANDLER.instance().enableExtraWorldEffects);

    public static void init() {}
}
