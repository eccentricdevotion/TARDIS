package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum Emerald {

    COLOUR(new NamespacedKey(TARDIS.plugin, "item/gui/particle/colour"));

    private final NamespacedKey key;

    Emerald(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
