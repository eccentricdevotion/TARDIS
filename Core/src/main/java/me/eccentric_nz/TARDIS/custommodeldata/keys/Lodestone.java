package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum Lodestone {

    MAZE(new NamespacedKey(TARDIS.plugin, "item/gui/room/maze"));

    private final NamespacedKey key;

    Lodestone(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
