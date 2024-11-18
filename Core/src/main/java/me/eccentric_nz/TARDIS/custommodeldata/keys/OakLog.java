package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum OakLog {

    VILLAGE(new NamespacedKey(TARDIS.plugin, "gui/room/village"));

    private final NamespacedKey key;

    OakLog(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
