package craftmine.worldeffects;

import craftmine.Craftmine;
import craftmine.config.Config;
import net.minecraft.aprilfools.WorldEffect;
import net.minecraft.aprilfools.WorldEffects;
import net.minecraft.item.Items;
import net.minecraft.world.GameRules;

public class CraftmineWorldEffects {
    public static final WorldEffect ETERNAL_DAY = WorldEffect.builder("eternal_day")
            .itemModel(Items.LIGHT_BLUE_DYE)
            .nameStyle(WorldEffects.field_59197)
            .onMineEnter(serverLevel -> {
                serverLevel.getGameInstance().getOverworld().setTimeOfDay(8000L);
                serverLevel.getGameInstance().getGameRules().get(GameRules.DO_DAYLIGHT_CYCLE).set(false, serverLevel.getGameInstance());
            })
            .onMineLeave(serverLevel -> serverLevel.getGameInstance().getGameRules().get(GameRules.DO_DAYLIGHT_CYCLE).set(true, serverLevel.getGameInstance()))
            .incompatibleWith(WorldEffects.ETERNAL_NIGHT)
            .experienceModifier(0.5F)
            .craftmine$register(Craftmine.MOD_ID, () -> Config.HANDLER.instance().enableExtraWorldEffects);

    public static void init() {}
}
