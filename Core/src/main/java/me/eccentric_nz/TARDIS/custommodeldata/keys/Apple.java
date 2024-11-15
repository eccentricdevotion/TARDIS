package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum Apple {

    BUTTON_RESTORE(new NamespacedKey(TARDIS.plugin, "genetic/button_restore"));

    private final NamespacedKey key;

    Apple(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
