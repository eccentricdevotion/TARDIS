package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum OssifiedVariant {

    OSSIFIED(new NamespacedKey(TARDIS.plugin, "genetic/ossified")),
    OSSIFIED_ARM(new NamespacedKey(TARDIS.plugin, "monster/ossified/ossified_arm")),
    OSSIFIED_HEAD(new NamespacedKey(TARDIS.plugin, "monster/ossified/ossified_head")),
    OSSIFIED_DISGUISE(new NamespacedKey(TARDIS.plugin, "monster/ossified/ossified_disguise")),
    OSSIFIED_0(new NamespacedKey(TARDIS.plugin, "monster/ossified/frames/ossified_0")),
    OSSIFIED_1(new NamespacedKey(TARDIS.plugin, "monster/ossified/frames/ossified_1")),
    OSSIFIED_2(new NamespacedKey(TARDIS.plugin, "monster/ossified/frames/ossified_2")),
    OSSIFIED_3(new NamespacedKey(TARDIS.plugin, "monster/ossified/frames/ossified_3")),
    OSSIFIED_4(new NamespacedKey(TARDIS.plugin, "monster/ossified/frames/ossified_4")),
    OSSIFIED_STATIC(new NamespacedKey(TARDIS.plugin, "monster/ossified/ossified_static")),
    OSSIFIED_ATTACKING_0(new NamespacedKey(TARDIS.plugin, "monster/ossified/frames/ossified_attacking_0")),
    OSSIFIED_ATTACKING_1(new NamespacedKey(TARDIS.plugin, "monster/ossified/frames/ossified_attacking_1")),
    OSSIFIED_ATTACKING_2(new NamespacedKey(TARDIS.plugin, "monster/ossified/frames/ossified_attacking_2")),
    OSSIFIED_ATTACKING_3(new NamespacedKey(TARDIS.plugin, "monster/ossified/frames/ossified_attacking_3")),
    OSSIFIED_ATTACKING_4(new NamespacedKey(TARDIS.plugin, "monster/ossified/frames/ossified_attacking_4"));

    private final NamespacedKey key;

    OssifiedVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
