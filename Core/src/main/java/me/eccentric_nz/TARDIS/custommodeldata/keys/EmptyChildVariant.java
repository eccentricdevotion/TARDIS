package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum EmptyChildVariant {

    EMPTY_CHILD(new NamespacedKey(TARDIS.plugin, "genetic/empty_child")),
    EMPTY_CHILD_ARM(new NamespacedKey(TARDIS.plugin, "monster/empty_child/empty_child_arm")),
    EMPTY_CHILD_HEAD(new NamespacedKey(TARDIS.plugin, "monster/empty_child/empty_child_head")),
    EMPTY_CHILD_DISGUISE(new NamespacedKey(TARDIS.plugin, "monster/empty_child/empty_child_disguise")),
    EMPTY_CHILD_MASK_FEATURE(new NamespacedKey(TARDIS.plugin, "lazarus/empty_child_mask")),
    EMPTY_CHILD_0(new NamespacedKey(TARDIS.plugin, "monster/empty_child/frames/empty_child_0")),
    EMPTY_CHILD_1(new NamespacedKey(TARDIS.plugin, "monster/empty_child/frames/empty_child_1")),
    EMPTY_CHILD_2(new NamespacedKey(TARDIS.plugin, "monster/empty_child/frames/empty_child_2")),
    EMPTY_CHILD_3(new NamespacedKey(TARDIS.plugin, "monster/empty_child/frames/empty_child_3")),
    EMPTY_CHILD_4(new NamespacedKey(TARDIS.plugin, "monster/empty_child/frames/empty_child_4")),
    EMPTY_CHILD_STATIC(new NamespacedKey(TARDIS.plugin, "monster/empty_child/empty_child_static")),
    EMPTY_CHILD_ATTACKING_0(new NamespacedKey(TARDIS.plugin, "monster/empty_child/frames/empty_child_attacking_0")),
    EMPTY_CHILD_ATTACKING_1(new NamespacedKey(TARDIS.plugin, "monster/empty_child/frames/empty_child_attacking_1")),
    EMPTY_CHILD_ATTACKING_2(new NamespacedKey(TARDIS.plugin, "monster/empty_child/frames/empty_child_attacking_2")),
    EMPTY_CHILD_ATTACKING_3(new NamespacedKey(TARDIS.plugin, "monster/empty_child/frames/empty_child_attacking_3")),
    EMPTY_CHILD_ATTACKING_4(new NamespacedKey(TARDIS.plugin, "monster/empty_child/frames/empty_child_attacking_4")),
    EMPTY_CHILD_MASK(new NamespacedKey(TARDIS.plugin, "monster/empty_child/empty_child_mask")),
    EMPTY_CHILD_OVERLAY(new NamespacedKey(TARDIS.plugin, "/monster/empty_child/overlay"));

    private final NamespacedKey key;

    EmptyChildVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}

