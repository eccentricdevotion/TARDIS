package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum WeepingAngelVariant {

    BUTTON_WEEPING_ANGEL(new NamespacedKey(TARDIS.plugin, "button_weeping_angel")),
    WEEPING_ANGEL_ARM(new NamespacedKey(TARDIS.plugin, "weeping_angel_arm")),
    WEEPING_ANGEL_HEAD(new NamespacedKey(TARDIS.plugin, "weeping_angel_head")),
    WEEPING_ANGEL_WING_SINGLE(new NamespacedKey(TARDIS.plugin, "weeping_angel_wing")),
    WEEPING_ANGEL_WINGS_M(new NamespacedKey(TARDIS.plugin, "weeping_angel_wings")),
    WEEPING_ANGEL_STATIC(new NamespacedKey(TARDIS.plugin, "weeping_angel_static")),
    WEEPING_ANGEL_WINGS(new NamespacedKey(TARDIS.plugin, "weeping_angel_wings")),
    WEEPING_ANGEL_DISGUISE(new NamespacedKey(TARDIS.plugin, "weeping_angel_disguise")),
    WEEPING_ANGEL_0(new NamespacedKey(TARDIS.plugin, "weeping_angel_0")),
    WEEPING_ANGEL_1(new NamespacedKey(TARDIS.plugin, "weeping_angel_1")),
    WEEPING_ANGEL_2(new NamespacedKey(TARDIS.plugin, "weeping_angel_2")),
    WEEPING_ANGEL_3(new NamespacedKey(TARDIS.plugin, "weeping_angel_3")),
    WEEPING_ANGEL_4(new NamespacedKey(TARDIS.plugin, "weeping_angel_4")),
    WEEPING_ANGEL_ATTACKING_0(new NamespacedKey(TARDIS.plugin, "weeping_angel_attacking_0")),
    WEEPING_ANGEL_ATTACKING_1(new NamespacedKey(TARDIS.plugin, "weeping_angel_attacking_1")),
    WEEPING_ANGEL_ATTACKING_2(new NamespacedKey(TARDIS.plugin, "weeping_angel_attacking_2")),
    WEEPING_ANGEL_ATTACKING_3(new NamespacedKey(TARDIS.plugin, "weeping_angel_attacking_3")),
    WEEPING_ANGEL_ATTACKING_4(new NamespacedKey(TARDIS.plugin, "weeping_angel_attacking_4"));

    private final NamespacedKey key;

    WeepingAngelVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
