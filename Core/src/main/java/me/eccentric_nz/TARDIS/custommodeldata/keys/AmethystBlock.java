package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum AmethystBlock {

    GEODE(new NamespacedKey(TARDIS.plugin, "gui/room/geode"));

    private final NamespacedKey key;

    AmethystBlock(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
