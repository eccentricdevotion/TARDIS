package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum Brick {

    WEEPING_ANGEL(new NamespacedKey(TARDIS.plugin, "genetic/weeping_angel")),
    WEEPING_ANGEL_ARM(new NamespacedKey(TARDIS.plugin, "monster/weeping_angel/weeping_angel_arm")),
    WEEPING_ANGEL_HEAD(new NamespacedKey(TARDIS.plugin, "monster/weeping_angel/weeping_angel_head")),
    WEEPING_ANGEL_WINGS_M(new NamespacedKey(TARDIS.plugin, "monster/weeping_angel/weeping_angel_wings")),
    WEEPING_ANGEL_STATIC(new NamespacedKey(TARDIS.plugin, "monster/weeping_angel/weeping_angel_static")),
    WEEPING_ANGEL_WINGS(new NamespacedKey(TARDIS.plugin, "lazarus/weeping_angel_wings")),
    WEEPING_ANGEL_DISGUISE(new NamespacedKey(TARDIS.plugin, "monster/weeping_angel/weeping_angel_disguise")),
    WEEPING_ANGEL_0(new NamespacedKey(TARDIS.plugin, "monster/weeping_angel/frames/weeping_angel_0")),
    WEEPING_ANGEL_1(new NamespacedKey(TARDIS.plugin, "monster/weeping_angel/frames/weeping_angel_1")),
    WEEPING_ANGEL_2(new NamespacedKey(TARDIS.plugin, "monster/weeping_angel/frames/weeping_angel_2")),
    WEEPING_ANGEL_3(new NamespacedKey(TARDIS.plugin, "monster/weeping_angel/frames/weeping_angel_3")),
    WEEPING_ANGEL_4(new NamespacedKey(TARDIS.plugin, "monster/weeping_angel/frames/weeping_angel_4")),
    WEEPING_ANGEL_ATTACKING_0(new NamespacedKey(TARDIS.plugin, "monster/weeping_angel/frames/weeping_angel_attacking_0")),
    WEEPING_ANGEL_ATTACKING_1(new NamespacedKey(TARDIS.plugin, "monster/weeping_angel/frames/weeping_angel_attacking_1")),
    WEEPING_ANGEL_ATTACKING_2(new NamespacedKey(TARDIS.plugin, "monster/weeping_angel/frames/weeping_angel_attacking_2")),
    WEEPING_ANGEL_ATTACKING_3(new NamespacedKey(TARDIS.plugin, "monster/weeping_angel/frames/weeping_angel_attacking_3")),
    WEEPING_ANGEL_ATTACKING_4(new NamespacedKey(TARDIS.plugin, "monster/weeping_angel/frames/weeping_angel_attacking_4")),
    BIOME_READER(new NamespacedKey(TARDIS.plugin, "tardis/biome_reader"));

    private final NamespacedKey key;

    Brick(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
