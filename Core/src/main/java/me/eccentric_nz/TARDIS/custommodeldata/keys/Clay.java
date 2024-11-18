package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum Clay {

    PASSAGE(new NamespacedKey(TARDIS.plugin, "gui/room/passage"));

    private final NamespacedKey key;

    Clay(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
