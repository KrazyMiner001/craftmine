package craftmine.config;

import com.google.gson.GsonBuilder;
import craftmine.Craftmine;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.ResourceLocation;

public class Config {
    public static ConfigClassHandler<Config> HANDLER = ConfigClassHandler.createBuilder(Config.class)
            .id(new ResourceLocation(Craftmine.MOD_ID, "config"))
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(FabricLoader.getInstance().getConfigDir().resolve(Craftmine.MOD_ID + ".json5"))
                    .appendGsonBuilder(GsonBuilder::setPrettyPrinting)
                    .setJson5(true)
                    .build())
            .build();

    @SerialEntry(comment = "Enables Dry Lands Fix\nThe Dry Lands mine ingredient is broken in the base game and is unobtainable, this fix makes it obtainable while keeping the apparent intended way to obtain it")
    public boolean enableDryLandsFix = true;

    @SerialEntry(comment = "Enables Exp Calculation Fix\nThe exp calculation adds multipliers instead of multiplying them and multiplies exp of items by stack size twice. This fixes both of those bugs")
    public boolean enableExpCalcFix = true;

    @SerialEntry(comment = "Bonus Mult for Exp Calculation Fix\nThe broken exp calculation often gives a lot more exp than the correct one. This balences the fixed calculation a bit by adding an extra mult.")
    public int bonusMult = 5;

    @SerialEntry
    public boolean enableExtraWorldEffects = true;
}
