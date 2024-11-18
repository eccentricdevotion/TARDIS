package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum Elytra {

    FLIGHT_MODE(new NamespacedKey(TARDIS.plugin, "gui/flight_mode"));

    private final NamespacedKey key;

    Elytra(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
