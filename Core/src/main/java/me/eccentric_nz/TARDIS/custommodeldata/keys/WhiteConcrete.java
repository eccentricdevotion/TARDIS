package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum WhiteConcrete {

    CONSOLE_WHITE(new NamespacedKey(TARDIS.plugin, "tardis/console_white"));

    private final NamespacedKey key;

    WhiteConcrete(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
