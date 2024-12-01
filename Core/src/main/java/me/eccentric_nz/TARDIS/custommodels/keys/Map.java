package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum Map {

    LOAD_SELECTED_PROGRAM_IN_EDITOR(new NamespacedKey(TARDIS.plugin, "gui/bowl/load_selected_program_in_editor")),
    LOAD_MAP(new NamespacedKey(TARDIS.plugin, "gui/map/load_map")),
    TRANSMAT_LOCATION(new NamespacedKey(TARDIS.plugin, "gui/map/transmat_location")),
    WHERE_AM_I(new NamespacedKey(TARDIS.plugin, "gui/map/where_am_i"));

    private final NamespacedKey key;

    Map(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
