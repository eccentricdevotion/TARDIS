package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum SpiderEye {

    RACNOSS(new NamespacedKey(TARDIS.plugin, "genetic/racnoss")),
    RACNOSS_HEAD(new NamespacedKey(TARDIS.plugin, "monster/racnoss/racnoss_head")),
    RACNOSS_DISGUISE(new NamespacedKey(TARDIS.plugin, "monster/racnoss/racnoss_disguise")),
    RACNOSS_0(new NamespacedKey(TARDIS.plugin, "monster/racnoss/frames/racnoss_0")),
    RACNOSS_1(new NamespacedKey(TARDIS.plugin, "monster/racnoss/frames/racnoss_1")),
    RACNOSS_2(new NamespacedKey(TARDIS.plugin, "monster/racnoss/frames/racnoss_2")),
    RACNOSS_STATIC(new NamespacedKey(TARDIS.plugin, "monster/racnoss/racnoss_static"));

    private final NamespacedKey key;

    SpiderEye(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
