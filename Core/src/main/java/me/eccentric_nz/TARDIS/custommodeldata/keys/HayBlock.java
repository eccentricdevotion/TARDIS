package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum HayBlock {

    STABLE(new NamespacedKey(TARDIS.plugin, "gui/room/stable"));

    private final NamespacedKey key;

    HayBlock(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
