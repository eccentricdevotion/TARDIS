package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum Lead {

    BUTTON_OPTS(new NamespacedKey(TARDIS.plugin, "genetic/button_opts"));

    private final NamespacedKey key;

    Lead(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
