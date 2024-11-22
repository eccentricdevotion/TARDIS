package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum EmeraldBlock {

    COLOUR_INFO(new NamespacedKey(TARDIS.plugin, "gui/particle/colour_info"));

    private final NamespacedKey key;

    EmeraldBlock(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
