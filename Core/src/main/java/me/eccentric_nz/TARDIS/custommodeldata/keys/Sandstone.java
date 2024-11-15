package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum Sandstone {

    ANTIGRAVITY(new NamespacedKey(TARDIS.plugin, "item/gui/room/antigravity")),
    FACTORY(new NamespacedKey(TARDIS.plugin, "block/seed/factory"));

    private final NamespacedKey key;

    Sandstone(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}

