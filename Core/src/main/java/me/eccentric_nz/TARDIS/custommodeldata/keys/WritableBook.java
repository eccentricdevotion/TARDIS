package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum WritableBook {

    BUTTON_DNA(new NamespacedKey(TARDIS.plugin, "genetic/button_dna")),
    COMPANION_LIST(new NamespacedKey(TARDIS.plugin, "gui/companion_list")),
    COMPANION_ALL(new NamespacedKey(TARDIS.plugin, "gui/companion_all"));

    private final NamespacedKey key;

    WritableBook(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
