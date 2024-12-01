package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum CompanionVariant {

    COMPANION_LIST(new NamespacedKey(TARDIS.plugin, "gui/companion_list")),
    COMPANION_ALL(new NamespacedKey(TARDIS.plugin, "gui/companion_all")),
    ADD_COMPANION(new NamespacedKey(TARDIS.plugin, "gui/add_companion"));

    private final NamespacedKey key;

    CompanionVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
