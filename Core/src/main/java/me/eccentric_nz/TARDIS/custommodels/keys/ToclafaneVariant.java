package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum ToclafaneVariant {

    TOCLAFANE(new NamespacedKey(TARDIS.plugin, "monster/toclafane/toclafane")),
    TOCLAFANE_ATTACK(new NamespacedKey(TARDIS.plugin, "monster/toclafane/toclafane_attack")),
    BUTTON_TOCLAFANE(new NamespacedKey(TARDIS.plugin, "genetic/toclafane"));

    private final NamespacedKey key;

    ToclafaneVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
