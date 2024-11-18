package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum WaxedOxidizedCopper {

    CONSOLE_RUSTIC(new NamespacedKey(TARDIS.plugin, "tardis/console_rustic"));

    private final NamespacedKey key;

    WaxedOxidizedCopper(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
