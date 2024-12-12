package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum CybermanVariant {

    CYBERMAN(new NamespacedKey(TARDIS.plugin, "button_cyberman")),
    CYBERMAN_ARM(new NamespacedKey(TARDIS.plugin, "cyberman_arm")),
    CYBERMAN_HEAD(new NamespacedKey(TARDIS.plugin, "cyberman_head")),
    CYBERMAN_DISGUISE(new NamespacedKey(TARDIS.plugin, "cyberman_disguise")),
    CYBERMAN_FEATURES(new NamespacedKey(TARDIS.plugin, "cyberman_features")),
    CYBER_WEAPON(new NamespacedKey(TARDIS.plugin, "cyber_weapon")),
    CYBERMAN_0(new NamespacedKey(TARDIS.plugin, "cyberman_0")),
    CYBERMAN_1(new NamespacedKey(TARDIS.plugin, "cyberman_1")),
    CYBERMAN_2(new NamespacedKey(TARDIS.plugin, "cyberman_2")),
    CYBERMAN_3(new NamespacedKey(TARDIS.plugin, "cyberman_3")),
    CYBERMAN_4(new NamespacedKey(TARDIS.plugin, "cyberman_4")),
    CYBERMAN_STATIC(new NamespacedKey(TARDIS.plugin, "cyberman_static")),
    CYBERMAN_ATTACKING_0(new NamespacedKey(TARDIS.plugin, "cyberman_attacking_0")),
    CYBERMAN_ATTACKING_1(new NamespacedKey(TARDIS.plugin, "cyberman_attacking_1")),
    CYBERMAN_ATTACKING_2(new NamespacedKey(TARDIS.plugin, "cyberman_attacking_2")),
    CYBERMAN_ATTACKING_3(new NamespacedKey(TARDIS.plugin, "cyberman_attacking_3")),
    CYBERMAN_ATTACKING_4(new NamespacedKey(TARDIS.plugin, "cyberman_attacking_4"));

    private final NamespacedKey key;

    CybermanVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}

