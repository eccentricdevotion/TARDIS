package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum RedNetherBricks {

    WORKSHOP(new NamespacedKey(TARDIS.plugin, "gui/room/workshop"));

    private final NamespacedKey key;

    RedNetherBricks(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
