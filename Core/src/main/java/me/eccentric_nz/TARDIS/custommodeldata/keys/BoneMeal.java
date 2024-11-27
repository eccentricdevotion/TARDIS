package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum BoneMeal {

    SYCORAX(new NamespacedKey(TARDIS.plugin, "genetic/sycorax")),
    SYCORAX_ARM(new NamespacedKey(TARDIS.plugin, "monster/sycorax/sycorax_arm")),
    SYCORAX_HEAD(new NamespacedKey(TARDIS.plugin, "monster/sycorax/sycorax_head")),
    SYCORAX_DISGUISE(new NamespacedKey(TARDIS.plugin, "monster/sycorax/sycorax_disguise")),
    SYCORAX_0(new NamespacedKey(TARDIS.plugin, "monster/sycorax/frames/sycorax_0")),
    SYCORAX_1(new NamespacedKey(TARDIS.plugin, "monster/sycorax/frames/sycorax_1")),
    SYCORAX_2(new NamespacedKey(TARDIS.plugin, "monster/sycorax/frames/sycorax_2")),
    SYCORAX_3(new NamespacedKey(TARDIS.plugin, "monster/sycorax/frames/sycorax_3")),
    SYCORAX_4(new NamespacedKey(TARDIS.plugin, "monster/sycorax/frames/sycorax_4")),
    SYCORAX_STATIC(new NamespacedKey(TARDIS.plugin, "monster/sycorax/sycorax_static")),
    SYCORAX_ATTACKING_0(new NamespacedKey(TARDIS.plugin, "monster/sycorax/frames/sycorax_attacking_0")),
    SYCORAX_ATTACKING_1(new NamespacedKey(TARDIS.plugin, "monster/sycorax/frames/sycorax_attacking_1")),
    SYCORAX_ATTACKING_2(new NamespacedKey(TARDIS.plugin, "monster/sycorax/frames/sycorax_attacking_2")),
    SYCORAX_ATTACKING_3(new NamespacedKey(TARDIS.plugin, "monster/sycorax/frames/sycorax_attacking_3")),
    SYCORAX_ATTACKING_4(new NamespacedKey(TARDIS.plugin, "monster/sycorax/frames/sycorax_attacking_4"));

    private final NamespacedKey key;

    BoneMeal(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
