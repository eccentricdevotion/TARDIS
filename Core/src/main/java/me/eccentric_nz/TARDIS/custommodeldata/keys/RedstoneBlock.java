package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum RedstoneBlock {

    EFFECT_INFO(new NamespacedKey(TARDIS.plugin, "gui/particle/effect_info"));

    private final NamespacedKey key;

    RedstoneBlock(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}

