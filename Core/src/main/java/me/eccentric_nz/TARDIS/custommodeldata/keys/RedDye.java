package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum RedDye {

    BUTTON_TYPE(new NamespacedKey(TARDIS.plugin, "genetic/button_type")),
    TARDIS_GLASS(new NamespacedKey(TARDIS.plugin, "block/police_box/tardis_glass")),
    TARDIS_RED(new NamespacedKey(TARDIS.plugin, "block/police_box/tardis_red")),
    TARDIS_RED_OPEN(new NamespacedKey(TARDIS.plugin, "block/police_box/tardis_red_open")),
    TARDIS_RED_STAINED(new NamespacedKey(TARDIS.plugin, "block/police_box/tardis_red_stained")),
    RED_FLYING_0(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/red/red_flying_0")),
    RED_FLYING_1(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/red/red_flying_1")),
    RED_FLYING_2(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/red/red_flying_2")),
    RED_FLYING_3(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/red/red_flying_3")),
    RED_FLYING_4(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/red/red_flying_4")),
    RED_FLYING_5(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/red/red_flying_5")),
    RED_FLYING_6(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/red/red_flying_6")),
    RED_FLYING_7(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/red/red_flying_7")),
    RED_FLYING_8(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/red/red_flying_8")),
    RED_FLYING_9(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/red/red_flying_9")),
    RED_FLYING_10(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/red/red_flying_10")),
    RED_FLYING_11(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/red/red_flying_11")),
    RED_FLYING_12(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/red/red_flying_12")),
    RED_FLYING_13(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/red/red_flying_13")),
    RED_FLYING_14(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/red/red_flying_14")),
    RED_FLYING_15(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/red/red_flying_15")),
    CONSOLE_0(new NamespacedKey(TARDIS.plugin, "time_rotor/console/console_0")),
    CONSOLE_1(new NamespacedKey(TARDIS.plugin, "time_rotor/console/console_1")),
    CONSOLE_2(new NamespacedKey(TARDIS.plugin, "time_rotor/console/console_2")),
    CONSOLE_3(new NamespacedKey(TARDIS.plugin, "time_rotor/console/console_3")),
    CONSOLE_4(new NamespacedKey(TARDIS.plugin, "time_rotor/console/console_4")),
    RED_FLYING_OPEN_0(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/red/red_flying_open_0")),
    RED_FLYING_OPEN_1(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/red/red_flying_open_1")),
    RED_FLYING_OPEN_2(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/red/red_flying_open_2")),
    RED_FLYING_OPEN_3(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/red/red_flying_open_3")),
    RED_FLYING_OPEN_4(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/red/red_flying_open_4")),
    RED_FLYING_OPEN_5(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/red/red_flying_open_5")),
    RED_FLYING_OPEN_6(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/red/red_flying_open_6")),
    RED_FLYING_OPEN_7(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/red/red_flying_open_7")),
    RED_FLYING_OPEN_8(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/red/red_flying_open_8")),
    RED_FLYING_OPEN_9(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/red/red_flying_open_9")),
    RED_FLYING_OPEN_10(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/red/red_flying_open_10")),
    RED_FLYING_OPEN_11(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/red/red_flying_open_11")),
    RED_FLYING_OPEN_12(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/red/red_flying_open_12")),
    RED_FLYING_OPEN_13(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/red/red_flying_open_13")),
    RED_FLYING_OPEN_14(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/red/red_flying_open_14")),
    RED_FLYING_OPEN_15(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/red/red_flying_open_15"));

    private final NamespacedKey key;

    RedDye(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
