package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum BrownConcrete {

    CONSOLE_BROWN(new NamespacedKey(TARDIS.plugin, "item/tardis/console_brown"));

    private final NamespacedKey key;

    BrownConcrete(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
