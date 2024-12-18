package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum SilentVariant {

    BUTTON_SILENT(new NamespacedKey(TARDIS.plugin, "button_silent")),
    SILENT(new NamespacedKey(TARDIS.plugin, "silent")),
    SILENT_MOUTH(new NamespacedKey(TARDIS.plugin, "silent_mouth")),
    SILENT_MOUTH_CLOSED(new NamespacedKey(TARDIS.plugin, "silent_mouth_closed")),
    SILENT_MOUTH_OPEN(new NamespacedKey(TARDIS.plugin, "silent_mouth_open")),
    SILENT_ARM_RIGHT(new NamespacedKey(TARDIS.plugin, "silent_arm_right")),
    SILENT_ARM_LEFT(new NamespacedKey(TARDIS.plugin, "silent_arm_left")),
    SILENCE_SIDE_HEAD(new NamespacedKey(TARDIS.plugin, "silence_side_head")),
    SILENT_BEAMING(new NamespacedKey(TARDIS.plugin, "silent_beaming")),
    SILENT_0(new NamespacedKey(TARDIS.plugin, "silent_0")),
    SILENT_1(new NamespacedKey(TARDIS.plugin, "silent_1")),
    SILENT_2(new NamespacedKey(TARDIS.plugin, "silent_2")),
    SILENT_3(new NamespacedKey(TARDIS.plugin, "silent_3")),
    SILENT_4(new NamespacedKey(TARDIS.plugin, "silent_4")),
    SILENT_STATIC(new NamespacedKey(TARDIS.plugin, "silent_static")),
    SILENT_ATTACKING_0(new NamespacedKey(TARDIS.plugin, "silent_attacking_0")),
    SILENT_ATTACKING_1(new NamespacedKey(TARDIS.plugin, "silent_attacking_1")),
    SILENT_ATTACKING_2(new NamespacedKey(TARDIS.plugin, "silent_attacking_2")),
    SILENT_ATTACKING_3(new NamespacedKey(TARDIS.plugin, "silent_attacking_3")),
    SILENT_ATTACKING_4(new NamespacedKey(TARDIS.plugin, "silent_attacking_4"));

    private final NamespacedKey key;

    SilentVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}

