package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum NetherBrick {

    ACID_BATTERY(new NamespacedKey(TARDIS.plugin, "item/tardis/acid_battery"));

    private final NamespacedKey key;

    NetherBrick(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
