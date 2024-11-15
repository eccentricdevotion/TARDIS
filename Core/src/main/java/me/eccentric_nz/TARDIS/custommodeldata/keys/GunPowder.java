package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum GunPowder {

    TOCLAFANE(new NamespacedKey(TARDIS.plugin, "monster/toclafane/toclafane")),
    TOCLAFANE_ATTACK(new NamespacedKey(TARDIS.plugin, "monster/toclafane/toclafane_attack")),
    BUTTON_TOCLAFANE(new NamespacedKey(TARDIS.plugin, "genetic/toclafane")),
    THUNDER(new NamespacedKey(TARDIS.plugin, "gui/thunder"));

    private final NamespacedKey key;

    GunPowder(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
