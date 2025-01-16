package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum TheBeastVariant {

    BUTTON_THE_BEAST(new NamespacedKey(TARDIS.plugin, "button_the_beast")),
    THE_BEAST_STATIC(new NamespacedKey(TARDIS.plugin, "the_beast_static")),
    THE_BEAST_HEAD(new NamespacedKey(TARDIS.plugin, "the_beast_head"));

    private final NamespacedKey key;

    TheBeastVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
