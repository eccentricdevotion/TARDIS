package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum Terracotta {

    RENDERER(new NamespacedKey(TARDIS.plugin, "gui/room/renderer"));

    private final NamespacedKey key;

    Terracotta(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
