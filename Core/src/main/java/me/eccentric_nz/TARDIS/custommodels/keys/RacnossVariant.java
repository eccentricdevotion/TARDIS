package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum RacnossVariant {

    BUTTON_RACNOSS(new NamespacedKey(TARDIS.plugin, "button_racnoss")),
    RACNOSS_HEAD(new NamespacedKey(TARDIS.plugin, "racnoss_head")),
    RACNOSS_FEATURES(new NamespacedKey(TARDIS.plugin, "racnoss_features")),
    RACNOSS_STATIC(new NamespacedKey(TARDIS.plugin, "racnoss_static"));

    private final NamespacedKey key;

    RacnossVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
