package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum Map {

    LOAD_SELECTED_PROGRAM_IN_EDITOR(new NamespacedKey(TARDIS.plugin, "item/gui/bowl/load_selected_program_in_editor")),
    LOAD_MAP(new NamespacedKey(TARDIS.plugin, "item/gui/map/load_map")),
    BUTTON_TARDIS_MAP(new NamespacedKey(TARDIS.plugin, "item/gui/map/button_tardis_map")),
    TRANSMAT_LOCATION(new NamespacedKey(TARDIS.plugin, "item/gui/map/transmat_location")),
    MONITOR(new NamespacedKey(TARDIS.plugin, "item/tardis/monitor")),
    SCREEN(new NamespacedKey(TARDIS.plugin, "block/controls/screen"));

    private final NamespacedKey key;

    Map(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
