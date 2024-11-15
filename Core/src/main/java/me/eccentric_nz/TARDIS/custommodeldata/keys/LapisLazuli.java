package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum LapisLazuli {

    SHAPE(new NamespacedKey(TARDIS.plugin, "item/gui/particle/shape"));

    private final NamespacedKey key;

    LapisLazuli(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
