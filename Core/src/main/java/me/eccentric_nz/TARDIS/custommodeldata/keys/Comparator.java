package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum Comparator {

    APPLY(new NamespacedKey(TARDIS.plugin, "gui/apply")),
    BUTTON_MASTER_ON(new NamespacedKey(TARDIS.plugin, "genetic/button_master_on")),
    BUTTON_MASTER_OFF(new NamespacedKey(TARDIS.plugin, "genetic/button_master_off")),
    CONSOLE(new NamespacedKey(TARDIS.plugin, "gui/lights/console"));

    private final NamespacedKey key;

    Comparator(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}

