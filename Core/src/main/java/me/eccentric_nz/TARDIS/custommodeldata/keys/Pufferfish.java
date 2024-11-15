package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum Pufferfish {


    HATH(new NamespacedKey(TARDIS.plugin, "item/genetic/hath")),

    HATH_ARM(new NamespacedKey(TARDIS.plugin, "item/monster/hath/hath_arm")),

    HATH_WEAPON_ARM(new NamespacedKey(TARDIS.plugin, "item/monster/hath/hath_weapon_arm")),

    HATH_HEAD(new NamespacedKey(TARDIS.plugin, "item/monster/hath/hath_head")),

    HATH_DISGUISE(new NamespacedKey(TARDIS.plugin, "item/monster/hath/hath_disguise")),

    HATH_FEATURES(new NamespacedKey(TARDIS.plugin, "item/lazarus/hath_features")),

    HATH_0(new NamespacedKey(TARDIS.plugin, "item/monster/hath/frames/hath_0")),

    HATH_1(new NamespacedKey(TARDIS.plugin, "item/monster/hath/frames/hath_1")),

    HATH_2(new NamespacedKey(TARDIS.plugin, "item/monster/hath/frames/hath_2")),

    HATH_3(new NamespacedKey(TARDIS.plugin, "item/monster/hath/frames/hath_3")),

    HATH_4(new NamespacedKey(TARDIS.plugin, "item/monster/hath/frames/hath_4")),

    HATH_STATIC(new NamespacedKey(TARDIS.plugin, "item/monster/hath/hath_static")),

    HATH_ATTACKING_0(new NamespacedKey(TARDIS.plugin, "item/monster/hath/frames/hath_attacking_0")),

    HATH_ATTACKING_1(new NamespacedKey(TARDIS.plugin, "item/monster/hath/frames/hath_attacking_1")),

    HATH_ATTACKING_2(new NamespacedKey(TARDIS.plugin, "item/monster/hath/frames/hath_attacking_2")),

    HATH_ATTACKING_3(new NamespacedKey(TARDIS.plugin, "item/monster/hath/frames/hath_attacking_3")),

    HATH_ATTACKING_4(new NamespacedKey(TARDIS.plugin, "item/monster/hath/frames/hath_attacking_4"));

    private final NamespacedKey key;

    Pufferfish(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
