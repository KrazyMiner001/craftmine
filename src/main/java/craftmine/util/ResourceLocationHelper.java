package craftmine.util;

import craftmine.Craftmine;
import net.minecraft.resources.ResourceLocation;

public class ResourceLocationHelper {
    public static ResourceLocation of(String id) {
        return new ResourceLocation(Craftmine.MOD_ID, id);
    }
}
