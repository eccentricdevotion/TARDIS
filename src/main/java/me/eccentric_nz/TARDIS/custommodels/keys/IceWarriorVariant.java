package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum IceWarriorVariant {

    BUTTON_ICE_WARRIOR(new NamespacedKey(TARDIS.plugin, "button_ice_warrior")),
    ICE_WARRIOR_DAGGER(new NamespacedKey(TARDIS.plugin, "ice_warrior_dagger")),
    ICE_WARRIOR_HEAD(new NamespacedKey(TARDIS.plugin, "ice_warrior_head")),
    ICE_WARRIOR_STATIC(new NamespacedKey(TARDIS.plugin, "ice_warrior_static"));

    private final NamespacedKey key;

    IceWarriorVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
