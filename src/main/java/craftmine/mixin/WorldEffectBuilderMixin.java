package craftmine.mixin;

import craftmine.interfaces.BetterWorldEffectRegister;
import net.minecraft.aprilfools.WorldEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.function.Supplier;

@Mixin(WorldEffect.Builder.class)
public abstract class WorldEffectBuilderMixin implements BetterWorldEffectRegister {

    @Shadow @Final private String id;

    @Shadow protected abstract WorldEffect build();

    @Override
    public WorldEffect craftmine$register(String namespace, Supplier<Boolean> shouldRegister) {
        if (shouldRegister.get()) {
            return Registry.register(Registries.WORLD_EFFECT, new Identifier(namespace, this.id), this.build());
        }
        return this.build();
    }
}
