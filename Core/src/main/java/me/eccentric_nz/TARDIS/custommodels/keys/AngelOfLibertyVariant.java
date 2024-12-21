package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum AngelOfLibertyVariant {

    BUTTON_ANGEL_OF_LIBERTY(new NamespacedKey(TARDIS.plugin, "button_liberty")),
    ANGEL_OF_LIBERTY_STATIC(new NamespacedKey(TARDIS.plugin, "liberty_static")),
    ANGEL_OF_LIBERTY_HEAD(new NamespacedKey(TARDIS.plugin, "liberty_head"));

    private final NamespacedKey key;

    AngelOfLibertyVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
