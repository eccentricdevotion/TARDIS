package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum CherryLeaves {

    GARDEN(new NamespacedKey(TARDIS.plugin, "gui/room/garden"));

    private final NamespacedKey key;

    CherryLeaves(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
