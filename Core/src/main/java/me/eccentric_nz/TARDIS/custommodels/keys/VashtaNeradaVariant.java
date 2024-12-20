package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum VashtaNeradaVariant {

    BUTTON_VASHTA_NERADA(new NamespacedKey(TARDIS.plugin, "button_vashta_nerada")),
    VASHTA_NERADA_HEAD(new NamespacedKey(TARDIS.plugin, "vashta_nerada_head")),
    VASHTA_NERADA_STATIC(new NamespacedKey(TARDIS.plugin, "vashta_nerada_static"));

    private final NamespacedKey key;

    VashtaNeradaVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}

