package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum Chest {

    SMELTER(new NamespacedKey(TARDIS.plugin, "item/gui/room/smelter"));

    private final NamespacedKey key;

    Chest(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
