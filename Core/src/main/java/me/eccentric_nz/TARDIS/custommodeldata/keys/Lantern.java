package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum Lantern {

    INTERIOR(new NamespacedKey(TARDIS.plugin, "item/gui/lights/interior"));

    private final NamespacedKey key;

    Lantern(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}

