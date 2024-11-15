package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum BrownStainedGlass {

    TV(new NamespacedKey(TARDIS.plugin, "item/lazarus/tv")),

    TINT_BROWN(new NamespacedKey(TARDIS.plugin, "block/lights/tint_brown"));

    private final NamespacedKey key;

    BrownStainedGlass(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
