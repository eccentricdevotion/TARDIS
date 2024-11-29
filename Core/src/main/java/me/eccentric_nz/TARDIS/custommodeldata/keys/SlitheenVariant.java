package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum SlitheenVariant {

    SLITHEEN(new NamespacedKey(TARDIS.plugin, "genetic/slitheen")),
    SLITHEEN_HEAD(new NamespacedKey(TARDIS.plugin, "monster/slitheen/slitheen_head")),
    SLITHEEN_DISGUISE(new NamespacedKey(TARDIS.plugin, "monster/slitheen/slitheen_disguise")),
    SLITHEEN_SUIT(new NamespacedKey(TARDIS.plugin, "monster/slitheen/slitheen_suit")),
    SLITHEEN_HEAD_SKIN(new NamespacedKey(TARDIS.plugin, "lazarus/slitheen_head")),
    SLITHEEN_CLAW_LEFT(new NamespacedKey(TARDIS.plugin, "lazarus/slitheen_claw_left")),
    SLITHEEN_CLAW_RIGHT(new NamespacedKey(TARDIS.plugin, "lazarus/slitheen_claw_right")),
    SLITHEEN_0(new NamespacedKey(TARDIS.plugin, "monster/slitheen/frames/slitheen_0")),
    SLITHEEN_1(new NamespacedKey(TARDIS.plugin, "monster/slitheen/frames/slitheen_1")),
    SLITHEEN_2(new NamespacedKey(TARDIS.plugin, "monster/slitheen/frames/slitheen_2")),
    SLITHEEN_3(new NamespacedKey(TARDIS.plugin, "monster/slitheen/frames/slitheen_3")),
    SLITHEEN_4(new NamespacedKey(TARDIS.plugin, "monster/slitheen/frames/slitheen_4")),
    SLITHEEN_STATIC(new NamespacedKey(TARDIS.plugin, "monster/slitheen/slitheen_static")),
    SLITHEEN_ATTACKING_0(new NamespacedKey(TARDIS.plugin, "monster/slitheen/frames/slitheen_attacking_0")),
    SLITHEEN_ATTACKING_1(new NamespacedKey(TARDIS.plugin, "monster/slitheen/frames/slitheen_attacking_1")),
    SLITHEEN_ATTACKING_2(new NamespacedKey(TARDIS.plugin, "monster/slitheen/frames/slitheen_attacking_2")),
    SLITHEEN_ATTACKING_3(new NamespacedKey(TARDIS.plugin, "monster/slitheen/frames/slitheen_attacking_3")),
    SLITHEEN_ATTACKING_4(new NamespacedKey(TARDIS.plugin, "monster/slitheen/frames/slitheen_attacking_4"));

    private final NamespacedKey key;

    SlitheenVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}

