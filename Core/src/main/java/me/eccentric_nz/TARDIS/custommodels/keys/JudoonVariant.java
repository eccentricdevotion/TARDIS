package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum JudoonVariant {

    BUTTON_JUDOON(new NamespacedKey(TARDIS.plugin, "button_judoon")),
    JUDOON_HEAD(new NamespacedKey(TARDIS.plugin, "judoon_head")),
    JUDOON_ARM(new NamespacedKey(TARDIS.plugin, "judoon_arm")),
    JUDOON_WEAPON_RESTING(new NamespacedKey(TARDIS.plugin, "judoon_weapon_arm_1")),
    JUDOON_SNOUT(new NamespacedKey(TARDIS.plugin, "judoon_snout")),
    JUDOON_WEAPON_ACTIVE(new NamespacedKey(TARDIS.plugin, "judoon_weapon_arm_2")),
    JUDOON_DISGUISE(new NamespacedKey(TARDIS.plugin, "judoon_disguise")),
    JUDOON_MONSTER_HEAD(new NamespacedKey(TARDIS.plugin, "judoon_monster_head")),
    JUDOON_0(new NamespacedKey(TARDIS.plugin, "judoon_0")),
    JUDOON_1(new NamespacedKey(TARDIS.plugin, "judoon_1")),
    JUDOON_2(new NamespacedKey(TARDIS.plugin, "judoon_2")),
    JUDOON_3(new NamespacedKey(TARDIS.plugin, "judoon_3")),
    JUDOON_4(new NamespacedKey(TARDIS.plugin, "judoon_4")),
    JUDOON_STATIC(new NamespacedKey(TARDIS.plugin, "judoon_static")),
    JUDOON_GUARD_0(new NamespacedKey(TARDIS.plugin, "judoon_guard_0")),
    JUDOON_GUARD_1(new NamespacedKey(TARDIS.plugin, "judoon_guard_1")),
    JUDOON_GUARD_2(new NamespacedKey(TARDIS.plugin, "judoon_guard_2")),
    JUDOON_GUARD_3(new NamespacedKey(TARDIS.plugin, "judoon_guard_3")),
    JUDOON_GUARD_4(new NamespacedKey(TARDIS.plugin, "judoon_guard_4")),
    JUDOON_GUARD(new NamespacedKey(TARDIS.plugin, "judoon_guard"));

    private final NamespacedKey key;

    JudoonVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
