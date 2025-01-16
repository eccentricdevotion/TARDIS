package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum OmegaVariant {

    BUTTON_OMEGA(new NamespacedKey(TARDIS.plugin, "button_omega")),
    OMEGA_STATIC(new NamespacedKey(TARDIS.plugin, "omega_static")),
    OMEGA_HEAD(new NamespacedKey(TARDIS.plugin, "omega_head"));

    private final NamespacedKey key;

    OmegaVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
