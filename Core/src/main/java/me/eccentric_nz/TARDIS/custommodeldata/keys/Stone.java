package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum Stone {

    SLOT(new NamespacedKey(TARDIS.plugin, "gui/room/slot"));

    private final NamespacedKey key;

    Stone(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
