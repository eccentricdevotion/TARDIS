package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum Melon {

    GREENHOUSE(new NamespacedKey(TARDIS.plugin, "item/gui/room/greenhouse"));

    private final NamespacedKey key;

    Melon(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
