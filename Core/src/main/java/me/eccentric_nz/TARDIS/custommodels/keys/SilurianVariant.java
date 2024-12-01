package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum SilurianVariant {

    SILURIAN(new NamespacedKey(TARDIS.plugin, "genetic/silurian")),
    SILURIAN_ARM(new NamespacedKey(TARDIS.plugin, "monster/silurian/silurian_arm")),
    SILURIAN_HEAD(new NamespacedKey(TARDIS.plugin, "monster/silurian/silurian_head")),
    SILURIAN_DISGUISE(new NamespacedKey(TARDIS.plugin, "monster/silurian/silurian_disguise")),
    SILURIAN_CREST(new NamespacedKey(TARDIS.plugin, "lazarus/silurian_crest")),
    SILURIAN_0(new NamespacedKey(TARDIS.plugin, "monster/silurian/frames/silurian_0")),
    SILURIAN_1(new NamespacedKey(TARDIS.plugin, "monster/silurian/frames/silurian_1")),
    SILURIAN_2(new NamespacedKey(TARDIS.plugin, "monster/silurian/frames/silurian_2")),
    SILURIAN_3(new NamespacedKey(TARDIS.plugin, "monster/silurian/frames/silurian_3")),
    SILURIAN_4(new NamespacedKey(TARDIS.plugin, "monster/silurian/frames/silurian_4")),
    SILURIAN_STATIC(new NamespacedKey(TARDIS.plugin, "monster/silurian/silurian_static")),
    SILURIAN_ATTACKING_0(new NamespacedKey(TARDIS.plugin, "monster/silurian/frames/silurian_attacking_0")),
    SILURIAN_ATTACKING_1(new NamespacedKey(TARDIS.plugin, "monster/silurian/frames/silurian_attacking_1")),
    SILURIAN_ATTACKING_2(new NamespacedKey(TARDIS.plugin, "monster/silurian/frames/silurian_attacking_2")),
    SILURIAN_ATTACKING_3(new NamespacedKey(TARDIS.plugin, "monster/silurian/frames/silurian_attacking_3")),
    SILURIAN_ATTACKING_4(new NamespacedKey(TARDIS.plugin, "monster/silurian/frames/silurian_attacking_4")),
    SILURIAN_BOW(new NamespacedKey(TARDIS.plugin, "monster/silurian/silurian_bow")),
    SILURIAN_GUN_ARM(new NamespacedKey(TARDIS.plugin, "monster/silurian/silurian_gun_arm"));

    private final NamespacedKey key;

    SilurianVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}

