package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum EmeraldBlock {

    ELEVENTH(new NamespacedKey(TARDIS.plugin, "block/seed/eleventh")),
    COLOUR_INFO(new NamespacedKey(TARDIS.plugin, "item/gui/particle/colour_info"));

    private final NamespacedKey key;

    EmeraldBlock(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
