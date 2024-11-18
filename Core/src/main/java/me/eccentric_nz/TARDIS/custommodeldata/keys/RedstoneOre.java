package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum RedstoneOre {

    EFFECT_SELECTED(new NamespacedKey(TARDIS.plugin, "gui/particle/effect_selected"));

    private final NamespacedKey key;

    RedstoneOre(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
