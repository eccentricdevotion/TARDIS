package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum BlueConcrete {

    CONSOLE_BLUE(new NamespacedKey(TARDIS.plugin, "tardis/console_blue"));

    private final NamespacedKey key;

    BlueConcrete(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
