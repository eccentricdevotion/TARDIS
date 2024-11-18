package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum LightGrayConcrete {

    CONSOLE_LIGHT_GRAY(new NamespacedKey(TARDIS.plugin, "tardis/console_light_gray")),
    CREATIVE(new NamespacedKey(TARDIS.plugin, "block/chemistry/creative"));

    private final NamespacedKey key;

    LightGrayConcrete(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}

