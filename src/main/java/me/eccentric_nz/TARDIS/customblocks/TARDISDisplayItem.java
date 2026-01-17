package me.eccentric_nz.TARDIS.customblocks;

import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;

import java.util.Locale;

public interface TARDISDisplayItem {
    default String getId() {
        if (this instanceof Enum<?> e) {
            return e.name();
        }
        return null;
    }

    default String getName() {
        return this.toString().toLowerCase(Locale.ROOT);
    }

    default String getDisplayName() {
        return TARDISStringUtils.capitalise(this.getName());
    }

    NamespacedKey getCustomModel();

    Material getMaterial();

    Material getCraftMaterial();

    boolean isLight();

    boolean isLit();

    boolean isVariable();

    boolean isSeed();

    boolean isDoor();

    boolean isClosedDoor();

    boolean isPipe();
}
