package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum LightButton {

    BLOCK_BUTTON(new NamespacedKey(TARDIS.plugin, "gui/lights/block_button")),
    LEVELS_BUTTON(new NamespacedKey(TARDIS.plugin, "gui/lights/levels_button")),
    SEQUENCE_BUTTON(new NamespacedKey(TARDIS.plugin, "gui/lights/sequence_button")),
    EDIT_BUTTON(new NamespacedKey(TARDIS.plugin, "gui/lights/edit_button")),
    LIGHT_LEVELS(new NamespacedKey(TARDIS.plugin, "gui/lights/light_levels")),
    LIGHTS_BUTTON(new NamespacedKey(TARDIS.plugin, "gui/lights/lights_button")),
    DELAY(new NamespacedKey(TARDIS.plugin, "gui/lights/delay")),
    CHANGE(new NamespacedKey(TARDIS.plugin, "gui/lights/change")),
    CONVERT(new NamespacedKey(TARDIS.plugin, "gui/lights/convert"));

    private final NamespacedKey key;

    LightButton(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
