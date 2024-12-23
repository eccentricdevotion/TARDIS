package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum ScarecrowVariant {

    BUTTON_SCARECROW(new NamespacedKey(TARDIS.plugin, "button_scarecrow")),
    SCARECROW_HEAD(new NamespacedKey(TARDIS.plugin, "scarecrow_head")),
    SCARECROW_STATIC(new NamespacedKey(TARDIS.plugin, "scarecrow_static"));

    private final NamespacedKey key;

    ScarecrowVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
