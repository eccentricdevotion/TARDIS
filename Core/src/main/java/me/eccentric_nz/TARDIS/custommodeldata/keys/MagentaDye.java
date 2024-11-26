package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum MagentaDye {

    BUTTON_TYPE(new NamespacedKey(TARDIS.plugin, "genetic/button_type")),
    TARDIS_GLASS(new NamespacedKey(TARDIS.plugin, "block/police_box/tardis_glass")),
    TARDIS_MAGENTA(new NamespacedKey(TARDIS.plugin, "block/police_box/tardis_magenta")),
    TARDIS_MAGENTA_OPEN(new NamespacedKey(TARDIS.plugin, "block/police_box/tardis_magenta_open")),
    TARDIS_MAGENTA_STAINED(new NamespacedKey(TARDIS.plugin, "block/police_box/tardis_magenta_stained")),
    MAGENTA_FLYING_0(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/magenta/magenta_flying_0")),
    MAGENTA_FLYING_1(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/magenta/magenta_flying_1")),
    MAGENTA_FLYING_2(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/magenta/magenta_flying_2")),
    MAGENTA_FLYING_3(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/magenta/magenta_flying_3")),
    MAGENTA_FLYING_4(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/magenta/magenta_flying_4")),
    MAGENTA_FLYING_5(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/magenta/magenta_flying_5")),
    MAGENTA_FLYING_6(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/magenta/magenta_flying_6")),
    MAGENTA_FLYING_7(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/magenta/magenta_flying_7")),
    MAGENTA_FLYING_8(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/magenta/magenta_flying_8")),
    MAGENTA_FLYING_9(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/magenta/magenta_flying_9")),
    MAGENTA_FLYING_10(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/magenta/magenta_flying_10")),
    MAGENTA_FLYING_11(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/magenta/magenta_flying_11")),
    MAGENTA_FLYING_12(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/magenta/magenta_flying_12")),
    MAGENTA_FLYING_13(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/magenta/magenta_flying_13")),
    MAGENTA_FLYING_14(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/magenta/magenta_flying_14")),
    MAGENTA_FLYING_15(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/magenta/magenta_flying_15")),
    MAGENTA_FLYING_OPEN_0(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/magenta/magenta_flying_open_0")),
    MAGENTA_FLYING_OPEN_1(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/magenta/magenta_flying_open_1")),
    MAGENTA_FLYING_OPEN_2(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/magenta/magenta_flying_open_2")),
    MAGENTA_FLYING_OPEN_3(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/magenta/magenta_flying_open_3")),
    MAGENTA_FLYING_OPEN_4(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/magenta/magenta_flying_open_4")),
    MAGENTA_FLYING_OPEN_5(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/magenta/magenta_flying_open_5")),
    MAGENTA_FLYING_OPEN_6(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/magenta/magenta_flying_open_6")),
    MAGENTA_FLYING_OPEN_7(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/magenta/magenta_flying_open_7")),
    MAGENTA_FLYING_OPEN_8(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/magenta/magenta_flying_open_8")),
    MAGENTA_FLYING_OPEN_9(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/magenta/magenta_flying_open_9")),
    MAGENTA_FLYING_OPEN_10(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/magenta/magenta_flying_open_10")),
    MAGENTA_FLYING_OPEN_11(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/magenta/magenta_flying_open_11")),
    MAGENTA_FLYING_OPEN_12(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/magenta/magenta_flying_open_12")),
    MAGENTA_FLYING_OPEN_13(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/magenta/magenta_flying_open_13")),
    MAGENTA_FLYING_OPEN_14(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/magenta/magenta_flying_open_14")),
    MAGENTA_FLYING_OPEN_15(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/magenta/magenta_flying_open_15"));

    private final NamespacedKey key;

    MagentaDye(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
