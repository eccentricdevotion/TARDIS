package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum OssifiedVariant {

    BUTTON_OSSIFIED(new NamespacedKey(TARDIS.plugin, "button_ossified")),
    OSSIFIED_ARM(new NamespacedKey(TARDIS.plugin, "ossified_arm")),
    OSSIFIED_HEAD(new NamespacedKey(TARDIS.plugin, "ossified_head")),
    OSSIFIED_STATIC(new NamespacedKey(TARDIS.plugin, "ossified_static"));

    private final NamespacedKey key;

    OssifiedVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
