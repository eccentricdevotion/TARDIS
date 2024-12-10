package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum CentredDoorVariant {

    CENTRED_CLOSED(new NamespacedKey(TARDIS.plugin, "centred_closed")),
    CENTRED_0(new NamespacedKey(TARDIS.plugin, "centred_0")),
    CENTRED_1(new NamespacedKey(TARDIS.plugin, "centred_1")),
    CENTRED_2(new NamespacedKey(TARDIS.plugin, "centred_2")),
    CENTRED_OPEN(new NamespacedKey(TARDIS.plugin, "centred_open")),
    CENTRED_EXTRA(new NamespacedKey(TARDIS.plugin, "centred_extra"));

    private final NamespacedKey key;

    CentredDoorVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
