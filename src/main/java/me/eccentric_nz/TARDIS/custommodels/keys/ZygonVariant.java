package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum ZygonVariant {

    BUTTON_ZYGON(new NamespacedKey(TARDIS.plugin, "button_zygon")),
    ZYGON_HEAD(new NamespacedKey(TARDIS.plugin, "zygon_head")),
    ZYGON_DISGUISE(new NamespacedKey(TARDIS.plugin, "zygon_disguise")),
    ZYGON_STATIC(new NamespacedKey(TARDIS.plugin, "zygon_static"));

    private final NamespacedKey key;

    ZygonVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
