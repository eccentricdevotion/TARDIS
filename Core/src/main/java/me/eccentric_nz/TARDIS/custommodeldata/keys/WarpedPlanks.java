package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum WarpedPlanks {

    COPPER(new NamespacedKey(TARDIS.plugin, "block/seed/copper"));

    private final NamespacedKey key;

    WarpedPlanks(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
