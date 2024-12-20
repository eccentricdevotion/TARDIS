package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum SilurianVariant {

    BUTTON_SILURIAN(new NamespacedKey(TARDIS.plugin, "button_silurian")),
    SILURIAN_HEAD(new NamespacedKey(TARDIS.plugin, "silurian_head")),
    SILURIAN_CREST(new NamespacedKey(TARDIS.plugin, "silurian_crest")),
    SILURIAN_STATIC(new NamespacedKey(TARDIS.plugin, "silurian_static")),
    SILURIAN_GUN(new NamespacedKey(TARDIS.plugin, "silurian_bow"));

    private final NamespacedKey key;

    SilurianVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}

