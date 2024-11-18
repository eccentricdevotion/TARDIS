package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum Redstone {

    EFFECT(new NamespacedKey(TARDIS.plugin, "gui/particle/effect"));

    private final NamespacedKey key;

    Redstone(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
