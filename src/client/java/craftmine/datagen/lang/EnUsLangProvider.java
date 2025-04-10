package craftmine.datagen.lang;

import craftmine.worldeffects.CraftmineWorldEffects;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class EnUsLangProvider extends FabricLanguageProvider {
    public EnUsLangProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generateTranslations(RegistryWrapper.WrapperLookup wrapperLookup, TranslationBuilder translationBuilder) {
        translationBuilder.add(CraftmineWorldEffects.ETERNAL_DAY.name().getString(), "Eternal Day");
        translationBuilder.add(CraftmineWorldEffects.ETERNAL_DAY.description().getString(), "Makes it always day");
        translationBuilder.add(CraftmineWorldEffects.ETERNAL_DAY.unlockHint().getString(), "Unlocked after a while, I guess?");
    }
}
