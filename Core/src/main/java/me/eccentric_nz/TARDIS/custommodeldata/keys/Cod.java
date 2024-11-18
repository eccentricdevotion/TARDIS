package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum Cod {

    VAMPIRE_OF_VENICE_FAN(new NamespacedKey(TARDIS.plugin, "lazarus/vampire_of_venice_fan"));

    private final NamespacedKey key;

    Cod(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
