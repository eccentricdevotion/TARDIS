package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum CyanConcrete {

    CONSOLE_CYAN(new NamespacedKey(TARDIS.plugin, "item/tardis/console_cyan")),
    SIEGE_CUBE(new NamespacedKey(TARDIS.plugin, "block/siege_cube"));

    private final NamespacedKey key;

    CyanConcrete(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}

