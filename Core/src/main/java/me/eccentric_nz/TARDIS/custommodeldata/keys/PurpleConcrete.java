package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum PurpleConcrete {

    CONSOLE_PURPLE(new NamespacedKey(TARDIS.plugin, "tardis/console_purple"));

    private final NamespacedKey key;

    PurpleConcrete(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
