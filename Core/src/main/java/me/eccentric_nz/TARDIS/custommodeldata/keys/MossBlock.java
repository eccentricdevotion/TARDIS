package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum MossBlock {


    PEN(new NamespacedKey(TARDIS.plugin, "item/gui/room/pen"));

    private final NamespacedKey key;

    MossBlock(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
