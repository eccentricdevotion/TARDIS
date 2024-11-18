package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum OrangeConcrete {

    THIRTEENTH(new NamespacedKey(TARDIS.plugin, "block/seed/thirteenth")),
    CONSOLE_ORANGE(new NamespacedKey(TARDIS.plugin, "tardis/console_orange")),
    COMPOUND(new NamespacedKey(TARDIS.plugin, "block/chemistry/compound"));

    private final NamespacedKey key;

    OrangeConcrete(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}

