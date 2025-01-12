package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum SmilerVariant {

    BUTTON_SMILER(new NamespacedKey(TARDIS.plugin, "button_smiler")),
    SMILER_STATIC(new NamespacedKey(TARDIS.plugin, "smiler_static")),
    SMILER_HEAD(new NamespacedKey(TARDIS.plugin, "smiler_head")),
    SMILER_ANGRY_HEAD(new NamespacedKey(TARDIS.plugin, "smiler_angry_head"));

    private final NamespacedKey key;

    SmilerVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
