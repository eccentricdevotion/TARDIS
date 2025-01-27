package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum K9Variant {

    BUTTON_K9(new NamespacedKey(TARDIS.plugin, "button_k9")),
    K9(new NamespacedKey(TARDIS.plugin, "k9")),
    K9_1(new NamespacedKey(TARDIS.plugin, "k9_ears_1")),
    K9_2(new NamespacedKey(TARDIS.plugin, "k9_ears_2")),
    K9_3(new NamespacedKey(TARDIS.plugin, "k9_ears_3")),
    K9_4(new NamespacedKey(TARDIS.plugin, "k9_ears_4"));

    private final NamespacedKey key;

    K9Variant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}

