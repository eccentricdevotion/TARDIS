package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum GrassBlock {

    ZERO(new NamespacedKey(TARDIS.plugin, "item/gui/room/zero"));

    private final NamespacedKey key;

    GrassBlock(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
