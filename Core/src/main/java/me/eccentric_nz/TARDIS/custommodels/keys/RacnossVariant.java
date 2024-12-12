package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum RacnossVariant {

    RACNOSS(new NamespacedKey(TARDIS.plugin, "button_racnoss")),
    RACNOSS_HEAD(new NamespacedKey(TARDIS.plugin, "racnoss_head")),
    RACNOSS_DISGUISE(new NamespacedKey(TARDIS.plugin, "racnoss_disguise")),
    RACNOSS_0(new NamespacedKey(TARDIS.plugin, "racnoss_0")),
    RACNOSS_1(new NamespacedKey(TARDIS.plugin, "racnoss_1")),
    RACNOSS_2(new NamespacedKey(TARDIS.plugin, "racnoss_2")),
    RACNOSS_STATIC(new NamespacedKey(TARDIS.plugin, "racnoss_static"));

    private final NamespacedKey key;

    RacnossVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
