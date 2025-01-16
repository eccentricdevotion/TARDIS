package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum CybermanVariant {

    BUTTON_CYBERMAN(new NamespacedKey(TARDIS.plugin, "button_cyberman")),
    BUTTON_CYBERSHADE(new NamespacedKey(TARDIS.plugin, "button_cybershade")),
    CYBER_WEAPON(new NamespacedKey(TARDIS.plugin, "cyber_weapon")),
    CYBERMAN_STATIC(new NamespacedKey(TARDIS.plugin, "cyberman_static")),
    CYBERMAN_HEAD(new NamespacedKey(TARDIS.plugin, "cyberman_head")),
    BLACK_CYBERMAN_STATIC(new NamespacedKey(TARDIS.plugin, "black_cyberman_static")),
    BLACK_CYBERMAN_HEAD(new NamespacedKey(TARDIS.plugin, "black_cyberman_head")),
    CYBERMAN_MOONBASE_STATIC(new NamespacedKey(TARDIS.plugin, "cyberman_moonbase_static")),
    CYBERMAN_MOONBASE_HEAD(new NamespacedKey(TARDIS.plugin, "cyberman_moonbase_head")),
    CYBERMAN_INVASION_ARM(new NamespacedKey(TARDIS.plugin, "cyberman_invasion_arm")),
    CYBERMAN_INVASION_STATIC(new NamespacedKey(TARDIS.plugin, "cyberman_invasion_static")),
    CYBERMAN_INVASION_HEAD(new NamespacedKey(TARDIS.plugin, "cyberman_invasion_head")),
    CYBERMAN_EARTHSHOCK_STATIC(new NamespacedKey(TARDIS.plugin, "cyberman_earthshock_static")),
    CYBERMAN_EARTHSHOCK_HEAD(new NamespacedKey(TARDIS.plugin, "cyberman_earthshock_head")),
    CYBERMAN_RISE_ARM(new NamespacedKey(TARDIS.plugin, "cyberman_rise_arm")),
    CYBERMAN_RISE_STATIC(new NamespacedKey(TARDIS.plugin, "cyberman_rise_static")),
    CYBERMAN_RISE_HEAD(new NamespacedKey(TARDIS.plugin, "cyberman_rise_head")),
    CYBERMAN_TENTH_PLANET_STATIC(new NamespacedKey(TARDIS.plugin, "cyberman_tenth_planet_static")),
    CYBERMAN_TENTH_PLANET_HEAD(new NamespacedKey(TARDIS.plugin, "cyberman_tenth_planet_head")),
    CYBER_LORD_STATIC(new NamespacedKey(TARDIS.plugin, "cyber_lord_static")),
    CYBER_LORD_HEAD(new NamespacedKey(TARDIS.plugin, "cyber_lord_head")),
    WOOD_CYBER_WEAPON(new NamespacedKey(TARDIS.plugin, "wood_cyber_weapon")),
    WOOD_CYBERMAN_STATIC(new NamespacedKey(TARDIS.plugin, "wood_cyberman_static")),
    WOOD_CYBERMAN_HEAD(new NamespacedKey(TARDIS.plugin, "wood_cyberman_head")),
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

