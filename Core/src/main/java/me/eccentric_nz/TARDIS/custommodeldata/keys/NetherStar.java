package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum NetherStar {

    ADMIN(new NamespacedKey(TARDIS.plugin, "item/gui/admin")),
    BUTTON_PREFS(new NamespacedKey(TARDIS.plugin, "item/gui/bowl/button_prefs")),
    ADD_COMPANION(new NamespacedKey(TARDIS.plugin, "item/gui/add_companion"));

    private final NamespacedKey key;

    NetherStar(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
