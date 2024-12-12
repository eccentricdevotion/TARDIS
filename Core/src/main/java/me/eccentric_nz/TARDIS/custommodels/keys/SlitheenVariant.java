package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum SlitheenVariant {

    SLITHEEN(new NamespacedKey(TARDIS.plugin, "empty_child_maskslitheen")),
    SLITHEEN_HEAD(new NamespacedKey(TARDIS.plugin, "slitheen_head")),
    SLITHEEN_DISGUISE(new NamespacedKey(TARDIS.plugin, "slitheen_disguise")),
    SLITHEEN_SUIT(new NamespacedKey(TARDIS.plugin, "slitheen_suit")),
    SLITHEEN_HEAD_SKIN(new NamespacedKey(TARDIS.plugin, "slitheen_head")),
    SLITHEEN_CLAW_LEFT(new NamespacedKey(TARDIS.plugin, "slitheen_claw_left")),
    SLITHEEN_CLAW_RIGHT(new NamespacedKey(TARDIS.plugin, "slitheen_claw_right")),
    SLITHEEN_0(new NamespacedKey(TARDIS.plugin, "slitheen_0")),
    SLITHEEN_1(new NamespacedKey(TARDIS.plugin, "slitheen_1")),
    SLITHEEN_2(new NamespacedKey(TARDIS.plugin, "slitheen_2")),
    SLITHEEN_3(new NamespacedKey(TARDIS.plugin, "slitheen_3")),
    SLITHEEN_4(new NamespacedKey(TARDIS.plugin, "slitheen_4")),
    SLITHEEN_STATIC(new NamespacedKey(TARDIS.plugin, "slitheen_static")),
    SLITHEEN_ATTACKING_0(new NamespacedKey(TARDIS.plugin, "slitheen_attacking_0")),
    SLITHEEN_ATTACKING_1(new NamespacedKey(TARDIS.plugin, "slitheen_attacking_1")),
    SLITHEEN_ATTACKING_2(new NamespacedKey(TARDIS.plugin, "slitheen_attacking_2")),
    SLITHEEN_ATTACKING_3(new NamespacedKey(TARDIS.plugin, "slitheen_attacking_3")),
    SLITHEEN_ATTACKING_4(new NamespacedKey(TARDIS.plugin, "slitheen_attacking_4"));

    private final NamespacedKey key;

    SlitheenVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}

