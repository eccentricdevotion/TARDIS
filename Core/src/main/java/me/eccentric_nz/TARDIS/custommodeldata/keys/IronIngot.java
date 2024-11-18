package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum IronIngot {

    CYBERMAN(new NamespacedKey(TARDIS.plugin, "genetic/cyberman")),
    CYBERMAN_ARM(new NamespacedKey(TARDIS.plugin, "monster/cyberman/cyberman_arm")),
    CYBERMAN_HEAD(new NamespacedKey(TARDIS.plugin, "monster/cyberman/cyberman_head")),
    CYBERMAN_DISGUISE(new NamespacedKey(TARDIS.plugin, "monster/cyberman/cyberman_disguise")),
    DENSITY(new NamespacedKey(TARDIS.plugin, "gui/particle/density")),
    CYBERMAN_FEATURES(new NamespacedKey(TARDIS.plugin, "lazarus/cyberman_features")),
    CYBER_WEAPON(new NamespacedKey(TARDIS.plugin, "lazarus/cyber_weapon")),
    CYBERMAN_0(new NamespacedKey(TARDIS.plugin, "monster/cyberman/frames/cyberman_0")),
    CYBERMAN_1(new NamespacedKey(TARDIS.plugin, "monster/cyberman/frames/cyberman_1")),
    CYBERMAN_2(new NamespacedKey(TARDIS.plugin, "monster/cyberman/frames/cyberman_2")),
    CYBERMAN_3(new NamespacedKey(TARDIS.plugin, "monster/cyberman/frames/cyberman_3")),
    CYBERMAN_4(new NamespacedKey(TARDIS.plugin, "monster/cyberman/frames/cyberman_4")),
    CYBERMAN_STATIC(new NamespacedKey(TARDIS.plugin, "monster/cyberman/cyberman_static")),
    CYBERMAN_ATTACKING_0(new NamespacedKey(TARDIS.plugin, "monster/cyberman/frames/cyberman_attacking_0")),
    CYBERMAN_ATTACKING_1(new NamespacedKey(TARDIS.plugin, "monster/cyberman/frames/cyberman_attacking_1")),
    CYBERMAN_ATTACKING_2(new NamespacedKey(TARDIS.plugin, "monster/cyberman/frames/cyberman_attacking_2")),
    CYBERMAN_ATTACKING_3(new NamespacedKey(TARDIS.plugin, "monster/cyberman/frames/cyberman_attacking_3")),
    CYBERMAN_ATTACKING_4(new NamespacedKey(TARDIS.plugin, "monster/cyberman/frames/cyberman_attacking_4"));

    private final NamespacedKey key;

    IronIngot(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}

