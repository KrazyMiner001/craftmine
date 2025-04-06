package craftmine.mixin;

import net.minecraft.server.TheGame;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.MineData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ServerLevel.class)
public interface ServerLevelAccessor {
    @Accessor("mineData")
    public MineData getMineData();

    @Accessor("theGame")
    public TheGame getTheGame();
}
