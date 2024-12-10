package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum ModelledButton {

    RANDOM_BUTTON_0(new NamespacedKey(TARDIS.plugin, "console_random_button_0")),
    SAVES_BUTTON_0(new NamespacedKey(TARDIS.plugin, "console_saves_button_0")),
    BACK_BUTTON_0(new NamespacedKey(TARDIS.plugin, "console_back_button_0")),
    LIGHT_SWITCH_0(new NamespacedKey(TARDIS.plugin, "console_light_switch_0")),
    TOGGLE_WOOL_BUTTON_0(new NamespacedKey(TARDIS.plugin, "console_toggle_wool_button_0")),
    ARTRON_BUTTON_0(new NamespacedKey(TARDIS.plugin, "console_artron_button_0")),
    REBUILD_BUTTON_0(new NamespacedKey(TARDIS.plugin, "console_rebuild_button_0")),
    SCANNER_BUTTON_0(new NamespacedKey(TARDIS.plugin, "console_scanner_button_0")),
    WXYZ_0(new NamespacedKey(TARDIS.plugin, "console_wxyz_0")),
    WXYZ_W(new NamespacedKey(TARDIS.plugin, "console_wxyz_w")),
    WXYZ_Y(new NamespacedKey(TARDIS.plugin, "console_wxyz_y")),
    WXYZ_X(new NamespacedKey(TARDIS.plugin, "console_wxyz_x")),
    WXYZ_Z(new NamespacedKey(TARDIS.plugin, "console_wxyz_z")),
    RANDOM_BUTTON_1(new NamespacedKey(TARDIS.plugin, "console_random_button_1")),
    SAVES_BUTTON_1(new NamespacedKey(TARDIS.plugin, "console_saves_button_1")),
    BACK_BUTTON_1(new NamespacedKey(TARDIS.plugin, "console_back_button_1")),
    LIGHT_SWITCH_1(new NamespacedKey(TARDIS.plugin, "console_light_switch_1")),
    TOGGLE_WOOL_BUTTON_1(new NamespacedKey(TARDIS.plugin, "console_toggle_wool_button_1")),
    ARTRON_BUTTON_1(new NamespacedKey(TARDIS.plugin, "console_artron_button_1")),
    REBUILD_BUTTON_1(new NamespacedKey(TARDIS.plugin, "console_rebuild_button_1")),
    SCANNER_BUTTON_1(new NamespacedKey(TARDIS.plugin, "console_scanner_button_1"));

    private final NamespacedKey key;

    ModelledButton(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
