package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum ToclafaneVariant {

    BUTTON_TOCLAFANE(new NamespacedKey(TARDIS.plugin, "button_toclafane")),
    TOCLAFANE(new NamespacedKey(TARDIS.plugin, "toclafane")),
    TOCLAFANE_ATTACK(new NamespacedKey(TARDIS.plugin, "toclafane_attack"));

    private final NamespacedKey key;

    ToclafaneVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
