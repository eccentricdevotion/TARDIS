package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum GreenConcrete {

    CONSOLE_GREEN(new NamespacedKey(TARDIS.plugin, "tardis/console_green"));

    private final NamespacedKey key;

    GreenConcrete(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
