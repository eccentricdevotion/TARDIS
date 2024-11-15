package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum CarvedPumpkin {

    EMPTY_CHILD_MASK(new NamespacedKey(TARDIS.plugin, "item/monster/empty_child/empty_child_mask"));

    private final NamespacedKey key;

    CarvedPumpkin(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
