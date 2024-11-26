package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum GrayDye {

    BUTTON_TYPE(new NamespacedKey(TARDIS.plugin, "genetic/button_type")),
    TARDIS_GLASS(new NamespacedKey(TARDIS.plugin, "block/police_box/tardis_glass")),
    TARDIS_GRAY(new NamespacedKey(TARDIS.plugin, "block/police_box/tardis_gray")),
    TARDIS_GRAY_OPEN(new NamespacedKey(TARDIS.plugin, "block/police_box/tardis_gray_open")),
    TARDIS_GRAY_STAINED(new NamespacedKey(TARDIS.plugin, "block/police_box/tardis_gray_stained")),
    GRAY_FLYING_0(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/gray/gray_flying_0")),
    GRAY_FLYING_1(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/gray/gray_flying_1")),
    GRAY_FLYING_2(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/gray/gray_flying_2")),
    GRAY_FLYING_3(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/gray/gray_flying_3")),
    GRAY_FLYING_4(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/gray/gray_flying_4")),
    GRAY_FLYING_5(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/gray/gray_flying_5")),
    GRAY_FLYING_6(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/gray/gray_flying_6")),
    GRAY_FLYING_7(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/gray/gray_flying_7")),
    GRAY_FLYING_8(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/gray/gray_flying_8")),
    GRAY_FLYING_9(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/gray/gray_flying_9")),
    GRAY_FLYING_10(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/gray/gray_flying_10")),
    GRAY_FLYING_11(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/gray/gray_flying_11")),
    GRAY_FLYING_12(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/gray/gray_flying_12")),
    GRAY_FLYING_13(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/gray/gray_flying_13")),
    GRAY_FLYING_14(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/gray/gray_flying_14")),
    GRAY_FLYING_15(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/gray/gray_flying_15")),
    TIME_ROTOR_TWELFTH_OFF(new NamespacedKey(TARDIS.plugin, "time_rotor/time_rotor_twelfth_off")),
    GRAY_FLYING_OPEN_0(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/gray/gray_flying_open_0")),
    GRAY_FLYING_OPEN_1(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/gray/gray_flying_open_1")),
    GRAY_FLYING_OPEN_2(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/gray/gray_flying_open_2")),
    GRAY_FLYING_OPEN_3(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/gray/gray_flying_open_3")),
    GRAY_FLYING_OPEN_4(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/gray/gray_flying_open_4")),
    GRAY_FLYING_OPEN_5(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/gray/gray_flying_open_5")),
    GRAY_FLYING_OPEN_6(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/gray/gray_flying_open_6")),
    GRAY_FLYING_OPEN_7(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/gray/gray_flying_open_7")),
    GRAY_FLYING_OPEN_8(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/gray/gray_flying_open_8")),
    GRAY_FLYING_OPEN_9(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/gray/gray_flying_open_9")),
    GRAY_FLYING_OPEN_10(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/gray/gray_flying_open_10")),
    GRAY_FLYING_OPEN_11(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/gray/gray_flying_open_11")),
    GRAY_FLYING_OPEN_12(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/gray/gray_flying_open_12")),
    GRAY_FLYING_OPEN_13(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/gray/gray_flying_open_13")),
    GRAY_FLYING_OPEN_14(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/gray/gray_flying_open_14")),
    GRAY_FLYING_OPEN_15(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/gray/gray_flying_open_15"));

    private final NamespacedKey key;

    GrayDye(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
