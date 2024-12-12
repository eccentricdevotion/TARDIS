package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum SontaranVariant {

    SONTARAN(new NamespacedKey(TARDIS.plugin, "button_sontaran")),
    SONTARAN_ARM(new NamespacedKey(TARDIS.plugin, "sontaran_arm")),
    SONTARAN_WEAPON_ARM(new NamespacedKey(TARDIS.plugin, "sontaran_weapon_arm")),
    SONTARAN_HEAD(new NamespacedKey(TARDIS.plugin, "sontaran_head")),
    SONTARAN_DISGUISE(new NamespacedKey(TARDIS.plugin, "sontaran_disguise")),
    SONTARAN_EARS(new NamespacedKey(TARDIS.plugin, "sontaran_ears")),
    STRAX_EARS(new NamespacedKey(TARDIS.plugin, "strax_ears")),
    SONTARAN_0(new NamespacedKey(TARDIS.plugin, "sontaran_0")),
    SONTARAN_1(new NamespacedKey(TARDIS.plugin, "sontaran_1")),
    SONTARAN_2(new NamespacedKey(TARDIS.plugin, "sontaran_2")),
    SONTARAN_3(new NamespacedKey(TARDIS.plugin, "sontaran_3")),
    SONTARAN_4(new NamespacedKey(TARDIS.plugin, "sontaran_4")),
    SONTARAN_STATIC(new NamespacedKey(TARDIS.plugin, "sontaran_static")),
    SONTARAN_ATTACKING_0(new NamespacedKey(TARDIS.plugin, "sontaran_attacking_0")),
    SONTARAN_ATTACKING_1(new NamespacedKey(TARDIS.plugin, "sontaran_attacking_1")),
    SONTARAN_ATTACKING_2(new NamespacedKey(TARDIS.plugin, "sontaran_attacking_2")),
    SONTARAN_ATTACKING_3(new NamespacedKey(TARDIS.plugin, "sontaran_attacking_3")),
    SONTARAN_ATTACKING_4(new NamespacedKey(TARDIS.plugin, "sontaran_attacking_4"));

    private final NamespacedKey key;

    SontaranVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}

