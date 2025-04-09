package craftmine.mixin;

import net.minecraft.aprilfools.MineData;
import net.minecraft.server.GameInstance;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ServerWorld.class)
public interface ServerWorldAccessor {
    @Accessor("field_58298")
    MineData getMineData();

    @Accessor("gameInstance")
    GameInstance getGameInstance();
}
