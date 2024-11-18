package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum GrayConcrete {

    CONSOLE_GRAY(new NamespacedKey(TARDIS.plugin, "tardis/console_gray")),
    FACTORY(new NamespacedKey(TARDIS.plugin, "block/seed/factory"));

    private final NamespacedKey key;

    GrayConcrete(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}

