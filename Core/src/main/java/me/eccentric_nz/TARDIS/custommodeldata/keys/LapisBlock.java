package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum LapisBlock {

    TOM(new NamespacedKey(TARDIS.plugin, "block/seed/tom")),
    SHAPE_INFO(new NamespacedKey(TARDIS.plugin, "gui/particle/shape_info"));

    private final NamespacedKey key;

    LapisBlock(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}

