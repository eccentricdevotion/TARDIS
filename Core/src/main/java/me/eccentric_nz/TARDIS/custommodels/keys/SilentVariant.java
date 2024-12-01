package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum SilentVariant {

    SILENT(new NamespacedKey(TARDIS.plugin, "monster/silent/silent")),
    SILENT_MOUTH(new NamespacedKey(TARDIS.plugin, "monster/silent/silent_mouth")),
    BUTTON_SILENT(new NamespacedKey(TARDIS.plugin, "genetic/silent")),
    SILENT_MOUTH_CLOSED(new NamespacedKey(TARDIS.plugin, "monster/silent/silent_mouth_closed")),
    SILENT_MOUTH_OPEN(new NamespacedKey(TARDIS.plugin, "monster/silent/silent_mouth_open")),
    SILENT_ARM_RIGHT(new NamespacedKey(TARDIS.plugin, "monster/silent/silent_arm_right")),
    SILENT_ARM_LEFT(new NamespacedKey(TARDIS.plugin, "monster/silent/silent_arm_left")),
    SILENCE_SIDE_HEAD(new NamespacedKey(TARDIS.plugin, "lazarus/silence_side_head")),
    SILENT_BEAMING(new NamespacedKey(TARDIS.plugin, "monster/silent/silent_beaming")),
    SILENT_0(new NamespacedKey(TARDIS.plugin, "monster/silent/frames/silent_0")),
    SILENT_1(new NamespacedKey(TARDIS.plugin, "monster/silent/frames/silent_1")),
    SILENT_2(new NamespacedKey(TARDIS.plugin, "monster/silent/frames/silent_2")),
    SILENT_3(new NamespacedKey(TARDIS.plugin, "monster/silent/frames/silent_3")),
    SILENT_4(new NamespacedKey(TARDIS.plugin, "monster/silent/frames/silent_4")),
    SILENT_STATIC(new NamespacedKey(TARDIS.plugin, "monster/silent/silent_static")),
    SILENT_ATTACKING_0(new NamespacedKey(TARDIS.plugin, "monster/silent/frames/silent_attacking_0")),
    SILENT_ATTACKING_1(new NamespacedKey(TARDIS.plugin, "monster/silent/frames/silent_attacking_1")),
    SILENT_ATTACKING_2(new NamespacedKey(TARDIS.plugin, "monster/silent/frames/silent_attacking_2")),
    SILENT_ATTACKING_3(new NamespacedKey(TARDIS.plugin, "monster/silent/frames/silent_attacking_3")),
    SILENT_ATTACKING_4(new NamespacedKey(TARDIS.plugin, "monster/silent/frames/silent_attacking_4"));

    private final NamespacedKey key;

    SilentVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}

