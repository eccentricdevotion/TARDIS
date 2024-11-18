package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum StoneBrickStairs {

    HARMONY(new NamespacedKey(TARDIS.plugin, "gui/room/harmony"));

    private final NamespacedKey key;

    StoneBrickStairs(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
