package craftmine.interfaces;

import net.minecraft.world.level.mines.WorldEffect;

import java.util.function.Supplier;

public interface BetterWorldEffectRegister {
    default WorldEffect craftmine$register(String namespace, Supplier<Boolean> shouldRegister) {
        return null;
    }
}
