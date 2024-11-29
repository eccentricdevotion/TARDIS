package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum Buttons {

    BUTTON_AGE(new NamespacedKey(TARDIS.plugin, "genetic/button_age")),
    BUTTON_DNA(new NamespacedKey(TARDIS.plugin, "genetic/button_dna")),
    BUTTON_MASTER_ON(new NamespacedKey(TARDIS.plugin, "genetic/button_master_on")),
    BUTTON_MASTER_OFF(new NamespacedKey(TARDIS.plugin, "genetic/button_master_off")),
    BUTTON_OPTS(new NamespacedKey(TARDIS.plugin, "genetic/button_opts")),
    BUTTON_PREFS(new NamespacedKey(TARDIS.plugin, "gui/bowl/button_prefs")),
    BUTTON_RESTORE(new NamespacedKey(TARDIS.plugin, "genetic/button_restore")),
    BUTTON_TARDIS_MAP(new NamespacedKey(TARDIS.plugin, "gui/map/button_tardis_map")),
    BUTTON_TYPE(new NamespacedKey(TARDIS.plugin, "genetic/button_type"));

    private final NamespacedKey key;

    Buttons(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
