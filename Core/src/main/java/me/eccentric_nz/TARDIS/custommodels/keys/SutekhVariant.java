package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum SutekhVariant {

    BUTTON_SUTEKH(new NamespacedKey(TARDIS.plugin, "button_sutekh")),
    SUTEKH_HEAD(new NamespacedKey(TARDIS.plugin, "sutekh_head")),
    SUTEKH_STATIC(new NamespacedKey(TARDIS.plugin, "sutekh_static"));

    private final NamespacedKey key;

    SutekhVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
