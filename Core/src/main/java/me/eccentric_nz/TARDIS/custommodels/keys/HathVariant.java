package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum HathVariant {

    BUTTON_HATH(new NamespacedKey(TARDIS.plugin, "button_hath")),
    HATH_ARM(new NamespacedKey(TARDIS.plugin, "hath_arm")),
    HATH_WEAPON_ARM(new NamespacedKey(TARDIS.plugin, "hath_weapon_arm")),
    HATH_HEAD(new NamespacedKey(TARDIS.plugin, "hath_head")),
    HATH_DISGUISE(new NamespacedKey(TARDIS.plugin, "hath_disguise")),
    HATH_FEATURES(new NamespacedKey(TARDIS.plugin, "hath_features")),
    HATH_0(new NamespacedKey(TARDIS.plugin, "hath_0")),
    HATH_1(new NamespacedKey(TARDIS.plugin, "hath_1")),
    HATH_2(new NamespacedKey(TARDIS.plugin, "hath_2")),
    HATH_3(new NamespacedKey(TARDIS.plugin, "hath_3")),
    HATH_4(new NamespacedKey(TARDIS.plugin, "hath_4")),
    HATH_STATIC(new NamespacedKey(TARDIS.plugin, "hath_static")),
    HATH_ATTACKING_0(new NamespacedKey(TARDIS.plugin, "hath_attacking_0")),
    HATH_ATTACKING_1(new NamespacedKey(TARDIS.plugin, "hath_attacking_1")),
    HATH_ATTACKING_2(new NamespacedKey(TARDIS.plugin, "hath_attacking_2")),
    HATH_ATTACKING_3(new NamespacedKey(TARDIS.plugin, "hath_attacking_3")),
    HATH_ATTACKING_4(new NamespacedKey(TARDIS.plugin, "hath_attacking_4"));

    private final NamespacedKey key;

    HathVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
