package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum BambooButton {

    RANDOM_BUTTON_0(new NamespacedKey(TARDIS.plugin, "block/controls/random_button_0")),
    SAVES_BUTTON_0(new NamespacedKey(TARDIS.plugin, "block/controls/saves_button_0")),
    BACK_BUTTON_0(new NamespacedKey(TARDIS.plugin, "block/controls/back_button_0")),
    LIGHT_SWITCH_0(new NamespacedKey(TARDIS.plugin, "block/controls/light_switch_0")),
    TOGGLE_WOOL_BUTTON_0(new NamespacedKey(TARDIS.plugin, "block/controls/toggle_wool_button_0")),
    ARTRON_BUTTON_0(new NamespacedKey(TARDIS.plugin, "block/controls/artron_button_0")),
    REBUILD_BUTTON_0(new NamespacedKey(TARDIS.plugin, "block/controls/rebuild_button_0")),
    SCANNER_BUTTON_0(new NamespacedKey(TARDIS.plugin, "block/controls/scanner_button_0")),
    WXYZ_0(new NamespacedKey(TARDIS.plugin, "block/controls/wxyz_0")),
    WXYZ_W(new NamespacedKey(TARDIS.plugin, "block/controls/wxyz_w")),
    WXYZ_Y(new NamespacedKey(TARDIS.plugin, "block/controls/wxyz_y")),
    WXYZ_X(new NamespacedKey(TARDIS.plugin, "block/controls/wxyz_x")),
    WXYZ_Z(new NamespacedKey(TARDIS.plugin, "block/controls/wxyz_z")),
    RANDOM_BUTTON_1(new NamespacedKey(TARDIS.plugin, "block/controls/random_button_1")),
    SAVES_BUTTON_1(new NamespacedKey(TARDIS.plugin, "block/controls/saves_button_1")),
    BACK_BUTTON_1(new NamespacedKey(TARDIS.plugin, "block/controls/back_button_1")),
    LIGHT_SWITCH_1(new NamespacedKey(TARDIS.plugin, "block/controls/light_switch_1")),
    TOGGLE_WOOL_BUTTON_1(new NamespacedKey(TARDIS.plugin, "block/controls/toggle_wool_button_1")),
    ARTRON_BUTTON_1(new NamespacedKey(TARDIS.plugin, "block/controls/artron_button_1")),
    REBUILD_BUTTON_1(new NamespacedKey(TARDIS.plugin, "block/controls/rebuild_button_1")),
    SCANNER_BUTTON_1(new NamespacedKey(TARDIS.plugin, "block/controls/scanner_button_1"));

    private final NamespacedKey key;

    BambooButton(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
