package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum Dirt {

    FARM(new NamespacedKey(TARDIS.plugin, "item/gui/room/farm"));

    private final NamespacedKey key;

    Dirt(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
