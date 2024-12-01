package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum Weather {

    CLEAR(new NamespacedKey(TARDIS.plugin, "gui/clear")),
    RAIN(new NamespacedKey(TARDIS.plugin, "gui/rain")),
    THUNDER(new NamespacedKey(TARDIS.plugin, "gui/thunder")),
    EXCITE(new NamespacedKey(TARDIS.plugin, "gui/excite"));

    private final NamespacedKey key;

    Weather(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
