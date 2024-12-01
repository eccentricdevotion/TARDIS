package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum ColouredVariant {

    TINTED(new NamespacedKey(TARDIS.plugin, "police_box/tinted")),
    TINTED_OPEN(new NamespacedKey(TARDIS.plugin, "police_box/tinted_open")),
    TINTED_STAINED(new NamespacedKey(TARDIS.plugin, "police_box/tinted_stained")),
    TINTED_GLASS(new NamespacedKey(TARDIS.plugin, "police_box/tardis_glass")),
    TINTED_CAMERA(new NamespacedKey(TARDIS.plugin, "police_box/tinted_camera")),
    TINT(new NamespacedKey(TARDIS.plugin, "police_box/tint"));

    private final NamespacedKey key;

    ColouredVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
