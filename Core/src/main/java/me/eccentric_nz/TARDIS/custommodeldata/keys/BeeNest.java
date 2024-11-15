package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum BeeNest {

    APIARY(new NamespacedKey(TARDIS.plugin, "gui/room/apiary"));

    private final NamespacedKey key;

    BeeNest(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
