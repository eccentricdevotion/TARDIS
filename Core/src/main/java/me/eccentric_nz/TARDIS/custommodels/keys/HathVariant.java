package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum HathVariant {

    BUTTON_HATH(new NamespacedKey(TARDIS.plugin, "button_hath")),
    HATH_WEAPON(new NamespacedKey(TARDIS.plugin, "hath_weapon")),
    HATH_HEAD(new NamespacedKey(TARDIS.plugin, "hath_head")),
    HATH_FEATURES(new NamespacedKey(TARDIS.plugin, "hath_features")),
    HATH_STATIC(new NamespacedKey(TARDIS.plugin, "hath_static"));

    private final NamespacedKey key;

    HathVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
