package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum Pumpkin {

    KITCHEN(new NamespacedKey(TARDIS.plugin, "gui/room/kitchen"));

    private final NamespacedKey key;

    Pumpkin(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
