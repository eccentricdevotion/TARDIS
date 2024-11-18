package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum PackedIce {

    IGLOO(new NamespacedKey(TARDIS.plugin, "gui/room/igloo"));

    private final NamespacedKey key;

    PackedIce(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
