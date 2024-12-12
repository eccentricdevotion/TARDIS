package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum LightButton {

    BLOCK_BUTTON(new NamespacedKey(TARDIS.plugin, "lights_block_button")),
    LEVELS_BUTTON(new NamespacedKey(TARDIS.plugin, "lights_levels_button")),
    SEQUENCE_BUTTON(new NamespacedKey(TARDIS.plugin, "lights_sequence_button")),
    EDIT_BUTTON(new NamespacedKey(TARDIS.plugin, "lights_edit_button")),
    LIGHT_LEVELS(new NamespacedKey(TARDIS.plugin, "lights_light_levels")),
    LIGHTS_BUTTON(new NamespacedKey(TARDIS.plugin, "lights_lights_button")),
    DELAY(new NamespacedKey(TARDIS.plugin, "lights_delay")),
    CHANGE(new NamespacedKey(TARDIS.plugin, "lights_change")),
    CONVERT(new NamespacedKey(TARDIS.plugin, "lights_convert"));

    private final NamespacedKey key;

    LightButton(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
