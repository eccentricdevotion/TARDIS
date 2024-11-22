package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum Book {

    INFO(new NamespacedKey(TARDIS.plugin, "gui/bowl/info")),
    VASHTA_NERADA(new NamespacedKey(TARDIS.plugin, "genetic/vashta_nerada")),
    VASHTA_NERADA_ARM(new NamespacedKey(TARDIS.plugin, "monster/vashta_nerada/vashta_nerada_arm")),
    VASHTA_NERADA_HEAD(new NamespacedKey(TARDIS.plugin, "monster/vashta_nerada/vashta_nerada_head")),
    VASHTA_NERADA_DISGUISE(new NamespacedKey(TARDIS.plugin, "monster/vashta_nerada/vashta_nerada_disguise")),
    VASHTA_NERADA_0(new NamespacedKey(TARDIS.plugin, "monster/vashta_nerada/frames/vashta_nerada_0")),
    VASHTA_NERADA_1(new NamespacedKey(TARDIS.plugin, "monster/vashta_nerada/frames/vashta_nerada_1")),
    VASHTA_NERADA_2(new NamespacedKey(TARDIS.plugin, "monster/vashta_nerada/frames/vashta_nerada_2")),
    VASHTA_NERADA_3(new NamespacedKey(TARDIS.plugin, "monster/vashta_nerada/frames/vashta_nerada_3")),
    VASHTA_NERADA_4(new NamespacedKey(TARDIS.plugin, "monster/vashta_nerada/frames/vashta_nerada_4")),
    VASHTA_NERADA_STATIC(new NamespacedKey(TARDIS.plugin, "monster/vashta_nerada/vashta_nerada_static")),
    VASHTA_NERADA_ATTACKING_0(new NamespacedKey(TARDIS.plugin, "monster/vashta_nerada/frames/vashta_nerada_attacking_0")),
    VASHTA_NERADA_ATTACKING_1(new NamespacedKey(TARDIS.plugin, "monster/vashta_nerada/frames/vashta_nerada_attacking_1")),
    VASHTA_NERADA_ATTACKING_2(new NamespacedKey(TARDIS.plugin, "monster/vashta_nerada/frames/vashta_nerada_attacking_2")),
    VASHTA_NERADA_ATTACKING_3(new NamespacedKey(TARDIS.plugin, "monster/vashta_nerada/frames/vashta_nerada_attacking_3")),
    VASHTA_NERADA_ATTACKING_4(new NamespacedKey(TARDIS.plugin, "monster/vashta_nerada/frames/vashta_nerada_attacking_4"));

    private final NamespacedKey key;

    Book(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}

