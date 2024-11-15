package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum Jukebox {

    ADVANCED_CONSOLE(new NamespacedKey(TARDIS.plugin, "block/tardis/advanced_console"));

    private final NamespacedKey key;

    Jukebox(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}

