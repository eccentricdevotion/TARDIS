package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum ColouredVariant {

    TINTED_CLOSED(new NamespacedKey(TARDIS.plugin, "chameleon_tinted_closed")),
    TINTED_OPEN(new NamespacedKey(TARDIS.plugin, "chameleon_tinted_open")),
    TINTED_STAINED(new NamespacedKey(TARDIS.plugin, "chameleon_tinted_stained")),
    TINTED_GLASS(new NamespacedKey(TARDIS.plugin, "chameleon_glass")),
    TINTED_CAMERA(new NamespacedKey(TARDIS.plugin, "chameleon_tinted_camera")),
    TINT(new NamespacedKey(TARDIS.plugin, "chameleon_tint"));

    private final NamespacedKey key;

    ColouredVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
