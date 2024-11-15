package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum EnchantingTable {
    LIBRARY(new NamespacedKey(TARDIS.plugin, "item/gui/room/library"));

    private final NamespacedKey key;

    EnchantingTable(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
