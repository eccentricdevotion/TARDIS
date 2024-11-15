package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum GreenStainedGlass {


    TINT_GREEN(new NamespacedKey(TARDIS.plugin, "block/lights/tint_green"));

    private final NamespacedKey key;

    GreenStainedGlass(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
