package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum Barrier {

    WEEPING_ANGEL_WING(new NamespacedKey(TARDIS.plugin, "monster/weeping_angel/weeping_angel_wing"));

    private final NamespacedKey key;

    Barrier(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
