package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum NetheriteScrap {

    THE_MIRE(new NamespacedKey(TARDIS.plugin, "genetic/the_mire")),
    THE_MIRE_HEAD(new NamespacedKey(TARDIS.plugin, "monster/mire/the_mire_head")),
    THE_MIRE_DISGUISE(new NamespacedKey(TARDIS.plugin, "monster/mire/the_mire_disguise")),
    THE_MIRE_HELMETLESS(new NamespacedKey(TARDIS.plugin, "monster/mire/the_mire_helmetless")),
    MIRE_HELMET(new NamespacedKey(TARDIS.plugin, "lazarus/mire_helmet")),
    MIRE_RIGHT_ARM(new NamespacedKey(TARDIS.plugin, "lazarus/mire_right_arm")),
    MIRE_LEFT_ARM(new NamespacedKey(TARDIS.plugin, "lazarus/mire_left_arm")),
    THE_MIRE_0(new NamespacedKey(TARDIS.plugin, "monster/mire/frames/the_mire_0")),
    THE_MIRE_1(new NamespacedKey(TARDIS.plugin, "monster/mire/frames/the_mire_1")),
    THE_MIRE_2(new NamespacedKey(TARDIS.plugin, "monster/mire/frames/the_mire_2")),
    THE_MIRE_3(new NamespacedKey(TARDIS.plugin, "monster/mire/frames/the_mire_3")),
    THE_MIRE_4(new NamespacedKey(TARDIS.plugin, "monster/mire/frames/the_mire_4")),
    THE_MIRE_STATIC(new NamespacedKey(TARDIS.plugin, "monster/mire/the_mire_static")),
    THE_MIRE_ATTACKING_0(new NamespacedKey(TARDIS.plugin, "monster/mire/frames/the_mire_attacking_0")),
    THE_MIRE_ATTACKING_1(new NamespacedKey(TARDIS.plugin, "monster/mire/frames/the_mire_attacking_1")),
    THE_MIRE_ATTACKING_2(new NamespacedKey(TARDIS.plugin, "monster/mire/frames/the_mire_attacking_2")),
    THE_MIRE_ATTACKING_3(new NamespacedKey(TARDIS.plugin, "monster/mire/frames/the_mire_attacking_3")),
    THE_MIRE_ATTACKING_4(new NamespacedKey(TARDIS.plugin, "monster/mire/frames/the_mire_attacking_4"));

    private final NamespacedKey key;

    NetheriteScrap(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}

