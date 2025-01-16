package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum VampireOfVeniceVariant {

    BUTTON_VAMPIRE(new NamespacedKey(TARDIS.plugin, "button_vampire_of_venice")),
    VAMPIRE_HEAD(new NamespacedKey(TARDIS.plugin, "vampire_of_venice_head")),
    VAMPIRE_FEATURES(new NamespacedKey(TARDIS.plugin, "vampire_of_venice_features")),
    VAMPIRE_STATIC(new NamespacedKey(TARDIS.plugin, "vampire_of_venice_static"));

    private final NamespacedKey key;

    VampireOfVeniceVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
