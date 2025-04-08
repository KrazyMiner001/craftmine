package craftmine.mixin;

import craftmine.interfaces.BetterWorldEffectRegister;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.mines.WorldEffect;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.function.Supplier;

@Mixin(WorldEffect.Builder.class)
public abstract class WorldEffectBuilderMixin implements BetterWorldEffectRegister {

    @Shadow @Final private String key;

    @Shadow protected abstract WorldEffect build();

    @Override
    public WorldEffect craftmine$register(String namespace, Supplier<Boolean> shouldRegister) {
        if (shouldRegister.get()) {
            return Registry.register(BuiltInRegistries.WORLD_EFFECT, new ResourceLocation(namespace, this.key), this.build());
        }
        return this.build();
    }
}
