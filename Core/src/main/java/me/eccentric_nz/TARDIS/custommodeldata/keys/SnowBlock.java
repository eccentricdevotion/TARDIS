package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum SnowBlock {

    POOL(new NamespacedKey(TARDIS.plugin, "item/gui/room/pool"));

    private final NamespacedKey key;

    SnowBlock(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
