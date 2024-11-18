package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum LapisOre {

    SHAPE_SELECTED(new NamespacedKey(TARDIS.plugin, "gui/particle/shape_selected"));

    private final NamespacedKey key;

    LapisOre(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
