package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum Tnt {

    JETTISON(new NamespacedKey(TARDIS.plugin, "item/gui/room/jettison"));

    private final NamespacedKey key;

    Tnt(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
