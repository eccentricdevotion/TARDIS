package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum Bamboo {

    BAMBOO(new NamespacedKey(TARDIS.plugin, "gui/room/bamboo"));

    private final NamespacedKey key;

    Bamboo(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
