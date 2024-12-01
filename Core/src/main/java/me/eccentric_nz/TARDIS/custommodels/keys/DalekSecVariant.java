package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum DalekSecVariant {

    DALEK_SEC(new NamespacedKey(TARDIS.plugin, "genetic/dalek_sec")),
    DALEK_SEC_HEAD(new NamespacedKey(TARDIS.plugin, "monster/dalek_sec/dalek_sec_head")),
    DALEK_SEC_DISGUISE(new NamespacedKey(TARDIS.plugin, "monster/dalek_sec/dalek_sec_disguise")),
    DALEK_SEC_TENTACLES(new NamespacedKey(TARDIS.plugin, "lazarus/dalek_sec_tentacles")),
    DALEK_SEC_0(new NamespacedKey(TARDIS.plugin, "monster/dalek_sec/frames/dalek_sec_0")),
    DALEK_SEC_1(new NamespacedKey(TARDIS.plugin, "monster/dalek_sec/frames/dalek_sec_1")),
    DALEK_SEC_2(new NamespacedKey(TARDIS.plugin, "monster/dalek_sec/frames/dalek_sec_2")),
    DALEK_SEC_3(new NamespacedKey(TARDIS.plugin, "monster/dalek_sec/frames/dalek_sec_3")),
    DALEK_SEC_4(new NamespacedKey(TARDIS.plugin, "monster/dalek_sec/frames/dalek_sec_4")),
    DALEK_SEC_STATIC(new NamespacedKey(TARDIS.plugin, "monster/dalek_sec/dalek_sec_static")),
    DALEK_SEC_ATTACKING_0(new NamespacedKey(TARDIS.plugin, "monster/dalek_sec/frames/dalek_sec_attacking_0")),
    DALEK_SEC_ATTACKING_1(new NamespacedKey(TARDIS.plugin, "monster/dalek_sec/frames/dalek_sec_attacking_1")),
    DALEK_SEC_ATTACKING_2(new NamespacedKey(TARDIS.plugin, "monster/dalek_sec/frames/dalek_sec_attacking_2")),
    DALEK_SEC_ATTACKING_3(new NamespacedKey(TARDIS.plugin, "monster/dalek_sec/frames/dalek_sec_attacking_3")),
    DALEK_SEC_ATTACKING_4(new NamespacedKey(TARDIS.plugin, "monster/dalek_sec/frames/dalek_sec_attacking_4"));

    private final NamespacedKey key;

    DalekSecVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
