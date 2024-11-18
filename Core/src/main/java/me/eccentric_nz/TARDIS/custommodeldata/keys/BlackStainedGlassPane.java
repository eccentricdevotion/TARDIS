package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum BlackStainedGlassPane {

    XRAY(new NamespacedKey(TARDIS.plugin, "tardis/xray"));

    private final NamespacedKey key;

    BlackStainedGlassPane(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
