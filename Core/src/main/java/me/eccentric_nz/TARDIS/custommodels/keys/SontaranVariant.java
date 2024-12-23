package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum SontaranVariant {

    BUTTON_SONTARAN(new NamespacedKey(TARDIS.plugin, "button_sontaran")),
    SONTARAN_WEAPON(new NamespacedKey(TARDIS.plugin, "sontaran_weapon")),
    SONTARAN_HEAD(new NamespacedKey(TARDIS.plugin, "sontaran_head")),
    SONTARAN_STATIC(new NamespacedKey(TARDIS.plugin, "sontaran_static"));

    private final NamespacedKey key;

    SontaranVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}

