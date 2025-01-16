package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum SeaDevilVariant {

    BUTTON_SEA_DEVIL(new NamespacedKey(TARDIS.plugin, "button_sea_devil")),
    SEA_DEVIL_HEAD(new NamespacedKey(TARDIS.plugin, "sea_devil_head")),
    SEA_DEVIL_STATIC(new NamespacedKey(TARDIS.plugin, "sea_devil_static"));

    private final NamespacedKey key;

    SeaDevilVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}

