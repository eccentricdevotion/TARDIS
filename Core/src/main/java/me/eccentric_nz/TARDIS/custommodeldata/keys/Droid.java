package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum Droid {

    CLOCKWORK_DROID(new NamespacedKey(TARDIS.plugin, "genetic/clockwork_droid")),
    CLOCKWORK_DROID_ARM(new NamespacedKey(TARDIS.plugin, "monster/clockwork_droid/clockwork_droid_arm")),
    CLOCKWORK_DROID_HEAD(new NamespacedKey(TARDIS.plugin, "monster/clockwork_droid/clockwork_droid_head")),
    CLOCKWORK_DROID_DISGUISE(new NamespacedKey(TARDIS.plugin, "monster/clockwork_droid/clockwork_droid_disguise")),
    CLOCKWORK_DROID_FEMALE_ARM(new NamespacedKey(TARDIS.plugin, "monster/clockwork_droid/clockwork_droid_female_arm")),
    CLOCKWORK_DROID_FEMALE_HEAD(new NamespacedKey(TARDIS.plugin, "monster/clockwork_droid/clockwork_droid_female_head")),
    CLOCKWORK_DROID_FEMALE_DISGUISE(new NamespacedKey(TARDIS.plugin, "monster/clockwork_droid/clockwork_droid_female_disguise")),
    CLOCKWORK_DROID_0(new NamespacedKey(TARDIS.plugin, "monster/clockwork_droid/frames/clockwork_droid_0")),
    CLOCKWORK_DROID_1(new NamespacedKey(TARDIS.plugin, "monster/clockwork_droid/frames/clockwork_droid_1")),
    CLOCKWORK_DROID_2(new NamespacedKey(TARDIS.plugin, "monster/clockwork_droid/frames/clockwork_droid_2")),
    CLOCKWORK_DROID_3(new NamespacedKey(TARDIS.plugin, "monster/clockwork_droid/frames/clockwork_droid_3")),
    CLOCKWORK_DROID_4(new NamespacedKey(TARDIS.plugin, "monster/clockwork_droid/frames/clockwork_droid_4")),
    CLOCKWORK_DROID_STATIC(new NamespacedKey(TARDIS.plugin, "monster/clockwork_droid/clockwork_droid_static")),
    CLOCKWORK_DROID_ATTACKING_0(new NamespacedKey(TARDIS.plugin, "monster/clockwork_droid/frames/clockwork_droid_attacking_0")),
    CLOCKWORK_DROID_ATTACKING_1(new NamespacedKey(TARDIS.plugin, "monster/clockwork_droid/frames/clockwork_droid_attacking_1")),
    CLOCKWORK_DROID_ATTACKING_2(new NamespacedKey(TARDIS.plugin, "monster/clockwork_droid/frames/clockwork_droid_attacking_2")),
    CLOCKWORK_DROID_ATTACKING_3(new NamespacedKey(TARDIS.plugin, "monster/clockwork_droid/frames/clockwork_droid_attacking_3")),
    CLOCKWORK_DROID_ATTACKING_4(new NamespacedKey(TARDIS.plugin, "monster/clockwork_droid/frames/clockwork_droid_attacking_4")),
    CLOCKWORK_DROID_FEMALE_0(new NamespacedKey(TARDIS.plugin, "monster/clockwork_droid/frames/clockwork_droid_female_0")),
    CLOCKWORK_DROID_FEMALE_1(new NamespacedKey(TARDIS.plugin, "monster/clockwork_droid/frames/clockwork_droid_female_1")),
    CLOCKWORK_DROID_FEMALE_2(new NamespacedKey(TARDIS.plugin, "monster/clockwork_droid/frames/clockwork_droid_female_2")),
    CLOCKWORK_DROID_FEMALE_3(new NamespacedKey(TARDIS.plugin, "monster/clockwork_droid/frames/clockwork_droid_female_3")),
    CLOCKWORK_DROID_FEMALE_4(new NamespacedKey(TARDIS.plugin, "monster/clockwork_droid/frames/clockwork_droid_female_4")),
    CLOCKWORK_DROID_FEMALE_STATIC(new NamespacedKey(TARDIS.plugin, "monster/clockwork_droid/clockwork_droid_female_static")),
    CLOCKWORK_DROID_FEMALE_ATTACKING_0(new NamespacedKey(TARDIS.plugin, "monster/clockwork_droid/frames/clockwork_droid_female_attacking_0")),
    CLOCKWORK_DROID_FEMALE_ATTACKING_1(new NamespacedKey(TARDIS.plugin, "monster/clockwork_droid/frames/clockwork_droid_female_attacking_1")),
    CLOCKWORK_DROID_FEMALE_ATTACKING_2(new NamespacedKey(TARDIS.plugin, "monster/clockwork_droid/frames/clockwork_droid_female_attacking_2")),
    CLOCKWORK_DROID_FEMALE_ATTACKING_3(new NamespacedKey(TARDIS.plugin, "monster/clockwork_droid/frames/clockwork_droid_female_attacking_3")),
    CLOCKWORK_DROID_FEMALE_ATTACKING_4(new NamespacedKey(TARDIS.plugin, "monster/clockwork_droid/frames/clockwork_droid_female_attacking_4"));

    private final NamespacedKey key;

    Droid(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
