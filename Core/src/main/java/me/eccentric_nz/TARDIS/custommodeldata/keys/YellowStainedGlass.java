package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum YellowStainedGlass {


    TINT_YELLOW(new NamespacedKey(TARDIS.plugin, "block/lights/tint_yellow"));

    private final NamespacedKey key;

    YellowStainedGlass(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
