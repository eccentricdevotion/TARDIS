package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum Hopper {

    RAIL(new NamespacedKey(TARDIS.plugin, "item/gui/room/rail")),
    BUTTON_AGE(new NamespacedKey(TARDIS.plugin, "item/genetic/button_age"));

    private final NamespacedKey key;

    Hopper(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
