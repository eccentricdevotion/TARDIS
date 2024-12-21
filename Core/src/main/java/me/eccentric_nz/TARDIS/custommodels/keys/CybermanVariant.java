package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum CybermanVariant {

    BUTTON_CYBERMAN(new NamespacedKey(TARDIS.plugin, "button_cyberman")),
    BUTTON_CYBERSHADE(new NamespacedKey(TARDIS.plugin, "button_cybershade")),
    CYBERMAN_HEAD(new NamespacedKey(TARDIS.plugin, "cyberman_head")),
    CYBERMAN_FEATURES(new NamespacedKey(TARDIS.plugin, "cyberman_features")),
    CYBER_WEAPON(new NamespacedKey(TARDIS.plugin, "cyber_weapon")),
    CYBERMAN_STATIC(new NamespacedKey(TARDIS.plugin, "cyberman_static")),
    CYBERSHADE_STATIC(new NamespacedKey(TARDIS.plugin, "cybershade_static")),
    CYBERSHADE_HEAD(new NamespacedKey(TARDIS.plugin, "cybershade_head"));

    private final NamespacedKey key;

    CybermanVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}

