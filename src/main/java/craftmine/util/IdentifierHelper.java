package craftmine.util;

import craftmine.Craftmine;
import net.minecraft.util.Identifier;

public class IdentifierHelper {
    public static Identifier of(String id) {
        return new Identifier(Craftmine.MOD_ID, id);
    }
}
