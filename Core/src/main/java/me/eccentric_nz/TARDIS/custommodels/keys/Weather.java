package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum Weather {

    CLEAR(new NamespacedKey(TARDIS.plugin, "button_clear")),
    RAIN(new NamespacedKey(TARDIS.plugin, "button_rain")),
    THUNDER(new NamespacedKey(TARDIS.plugin, "button_thunder")),
    EXCITE(new NamespacedKey(TARDIS.plugin, "button_excite"));

    private final NamespacedKey key;

    Weather(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
