package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum PinkConcrete {

    CONSOLE_PINK(new NamespacedKey(TARDIS.plugin, "item/tardis/console_pink"));

    private final NamespacedKey key;

    PinkConcrete(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
