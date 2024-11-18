package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum Compass {

    WHERE_AM_I(new NamespacedKey(TARDIS.plugin, "gui/map/where_am_i"));

    private final NamespacedKey key;

    Compass(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
