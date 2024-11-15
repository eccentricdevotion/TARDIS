package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum OakPlanks {

    WOOD(new NamespacedKey(TARDIS.plugin, "item/gui/room/wood"));

    private final NamespacedKey key;

    OakPlanks(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
