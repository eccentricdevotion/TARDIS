package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum CarvedPumpkin {

    EMPTY_CHILD_MASK(new NamespacedKey(TARDIS.plugin, "monster/empty_child/empty_child_mask")),
    EMPTY_CHILD_OVERLAY(new NamespacedKey(TARDIS.plugin, "/monster/empty_child/overlay"));

    private final NamespacedKey key;

    CarvedPumpkin(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
