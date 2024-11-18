package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum MagentaConcrete {

    CONSOLE_MAGENTA(new NamespacedKey(TARDIS.plugin, "tardis/console_magenta")),
    REDUCER(new NamespacedKey(TARDIS.plugin, "block/chemistry/reducer"));

    private final NamespacedKey key;

    MagentaConcrete(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}

