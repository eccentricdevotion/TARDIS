package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum OakLeaves {

    ARBORETUM(new NamespacedKey(TARDIS.plugin, "gui/room/arboretum"));

    private final NamespacedKey key;

    OakLeaves(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
