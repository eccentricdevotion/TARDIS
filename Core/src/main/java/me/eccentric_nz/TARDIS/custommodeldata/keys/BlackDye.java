package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum BlackDye {

    BUTTON_TYPE(new NamespacedKey(TARDIS.plugin, "genetic/button_type")),
    TARDIS_BLACK(new NamespacedKey(TARDIS.plugin, "block/police_box/tardis_black")),
    TARDIS_BLACK_OPEN(new NamespacedKey(TARDIS.plugin, "block/police_box/tardis_black_open")),
    TARDIS_BLACK_STAINED(new NamespacedKey(TARDIS.plugin, "block/police_box/tardis_black_stained")),
    TARDIS_GLASS(new NamespacedKey(TARDIS.plugin, "block/police_box/tardis_glass")),
    BLACK_FLYING_0(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/black/black_flying_0")),
    BLACK_FLYING_1(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/black/black_flying_1")),
    BLACK_FLYING_2(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/black/black_flying_2")),
    BLACK_FLYING_3(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/black/black_flying_3")),
    BLACK_FLYING_4(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/black/black_flying_4")),
    BLACK_FLYING_5(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/black/black_flying_5")),
    BLACK_FLYING_6(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/black/black_flying_6")),
    BLACK_FLYING_7(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/black/black_flying_7")),
    BLACK_FLYING_8(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/black/black_flying_8")),
    BLACK_FLYING_9(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/black/black_flying_9")),
    BLACK_FLYING_10(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/black/black_flying_10")),
    BLACK_FLYING_11(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/black/black_flying_11")),
    BLACK_FLYING_12(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/black/black_flying_12")),
    BLACK_FLYING_13(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/black/black_flying_13")),
    BLACK_FLYING_14(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/black/black_flying_14")),
    BLACK_FLYING_15(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/black/black_flying_15")),
    EARLY_0(new NamespacedKey(TARDIS.plugin, "time_rotor/early/early_0")),
    EARLY_1(new NamespacedKey(TARDIS.plugin, "time_rotor/early/early_1")),
    EARLY_2(new NamespacedKey(TARDIS.plugin, "time_rotor/early/early_2")),
    EARLY_3(new NamespacedKey(TARDIS.plugin, "time_rotor/early/early_3")),
    EARLY_4(new NamespacedKey(TARDIS.plugin, "time_rotor/early/early_4")),
    EARLY_5(new NamespacedKey(TARDIS.plugin, "time_rotor/early/early_5")),
    EARLY_6(new NamespacedKey(TARDIS.plugin, "time_rotor/early/early_6")),
    EARLY_7(new NamespacedKey(TARDIS.plugin, "time_rotor/early/early_7")),
    BLACK_FLYING_OPEN_0(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/black/black_flying_open_0")),
    BLACK_FLYING_OPEN_1(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/black/black_flying_open_1")),
    BLACK_FLYING_OPEN_2(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/black/black_flying_open_2")),
    BLACK_FLYING_OPEN_3(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/black/black_flying_open_3")),
    BLACK_FLYING_OPEN_4(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/black/black_flying_open_4")),
    BLACK_FLYING_OPEN_5(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/black/black_flying_open_5")),
    BLACK_FLYING_OPEN_6(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/black/black_flying_open_6")),
    BLACK_FLYING_OPEN_7(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/black/black_flying_open_7")),
    BLACK_FLYING_OPEN_8(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/black/black_flying_open_8")),
    BLACK_FLYING_OPEN_9(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/black/black_flying_open_9")),
    BLACK_FLYING_OPEN_10(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/black/black_flying_open_10")),
    BLACK_FLYING_OPEN_11(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/black/black_flying_open_11")),
    BLACK_FLYING_OPEN_12(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/black/black_flying_open_12")),
    BLACK_FLYING_OPEN_13(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/black/black_flying_open_13")),
    BLACK_FLYING_OPEN_14(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/black/black_flying_open_14")),
    BLACK_FLYING_OPEN_15(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/black/black_flying_open_15"));

    private final NamespacedKey key;

    BlackDye(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
