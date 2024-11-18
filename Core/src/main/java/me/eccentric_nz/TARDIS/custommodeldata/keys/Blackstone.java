package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum Blackstone {

    NETHER(new NamespacedKey(TARDIS.plugin, "gui/room/nether"));

    private final NamespacedKey key;

    Blackstone(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
