package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum CyanDye {

    BUTTON_TYPE(new NamespacedKey(TARDIS.plugin, "item/genetic/button_type")),
    TARDIS_CYAN(new NamespacedKey(TARDIS.plugin, "block/police_box/tardis_cyan")),
    TARDIS_CYAN_OPEN(new NamespacedKey(TARDIS.plugin, "block/police_box/tardis_cyan_open")),
    TARDIS_CYAN_STAINED(new NamespacedKey(TARDIS.plugin, "block/police_box/tardis_cyan_stained")),
    TARDIS_GLASS(new NamespacedKey(TARDIS.plugin, "block/police_box/tardis_glass")),
    CYAN_FLYING_0(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/cyan/cyan_flying_0")),
    CYAN_FLYING_1(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/cyan/cyan_flying_1")),
    CYAN_FLYING_2(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/cyan/cyan_flying_2")),
    CYAN_FLYING_3(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/cyan/cyan_flying_3")),
    CYAN_FLYING_4(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/cyan/cyan_flying_4")),
    CYAN_FLYING_5(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/cyan/cyan_flying_5")),
    CYAN_FLYING_6(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/cyan/cyan_flying_6")),
    CYAN_FLYING_7(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/cyan/cyan_flying_7")),
    CYAN_FLYING_8(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/cyan/cyan_flying_8")),
    CYAN_FLYING_9(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/cyan/cyan_flying_9")),
    CYAN_FLYING_10(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/cyan/cyan_flying_10")),
    CYAN_FLYING_11(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/cyan/cyan_flying_11")),
    CYAN_FLYING_12(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/cyan/cyan_flying_12")),
    CYAN_FLYING_13(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/cyan/cyan_flying_13")),
    CYAN_FLYING_14(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/cyan/cyan_flying_14")),
    CYAN_FLYING_15(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/cyan/cyan_flying_15")),
    DELTA_0(new NamespacedKey(TARDIS.plugin, "item/time_rotor/delta/delta_0")),
    DELTA_1(new NamespacedKey(TARDIS.plugin, "item/time_rotor/delta/delta_1")),
    DELTA_2(new NamespacedKey(TARDIS.plugin, "item/time_rotor/delta/delta_2")),
    DELTA_3(new NamespacedKey(TARDIS.plugin, "item/time_rotor/delta/delta_3")),
    DELTA_4(new NamespacedKey(TARDIS.plugin, "item/time_rotor/delta/delta_4")),
    DELTA_5(new NamespacedKey(TARDIS.plugin, "item/time_rotor/delta/delta_5")),
    CYAN_FLYING_OPEN_0(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/cyan/cyan_flying_open_0")),
    CYAN_FLYING_OPEN_1(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/cyan/cyan_flying_open_1")),
    CYAN_FLYING_OPEN_2(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/cyan/cyan_flying_open_2")),
    CYAN_FLYING_OPEN_3(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/cyan/cyan_flying_open_3")),
    CYAN_FLYING_OPEN_4(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/cyan/cyan_flying_open_4")),
    CYAN_FLYING_OPEN_5(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/cyan/cyan_flying_open_5")),
    CYAN_FLYING_OPEN_6(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/cyan/cyan_flying_open_6")),
    CYAN_FLYING_OPEN_7(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/cyan/cyan_flying_open_7")),
    CYAN_FLYING_OPEN_8(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/cyan/cyan_flying_open_8")),
    CYAN_FLYING_OPEN_9(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/cyan/cyan_flying_open_9")),
    CYAN_FLYING_OPEN_10(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/cyan/cyan_flying_open_10")),
    CYAN_FLYING_OPEN_11(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/cyan/cyan_flying_open_11")),
    CYAN_FLYING_OPEN_12(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/cyan/cyan_flying_open_12")),
    CYAN_FLYING_OPEN_13(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/cyan/cyan_flying_open_13")),
    CYAN_FLYING_OPEN_14(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/cyan/cyan_flying_open_14")),
    CYAN_FLYING_OPEN_15(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/cyan/cyan_flying_open_15"));

    private final NamespacedKey key;

    CyanDye(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
