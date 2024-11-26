package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum GreenDye {

    BUTTON_TYPE(new NamespacedKey(TARDIS.plugin, "genetic/button_type")),
    TARDIS_GLASS(new NamespacedKey(TARDIS.plugin, "block/police_box/tardis_glass")),
    TARDIS_GREEN(new NamespacedKey(TARDIS.plugin, "block/police_box/tardis_green")),
    TARDIS_GREEN_OPEN(new NamespacedKey(TARDIS.plugin, "block/police_box/tardis_green_open")),
    TARDIS_GREEN_STAINED(new NamespacedKey(TARDIS.plugin, "block/police_box/tardis_green_stained")),
    GREEN_FLYING_0(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/green/green_flying_0")),
    GREEN_FLYING_1(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/green/green_flying_1")),
    GREEN_FLYING_2(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/green/green_flying_2")),
    GREEN_FLYING_3(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/green/green_flying_3")),
    GREEN_FLYING_4(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/green/green_flying_4")),
    GREEN_FLYING_5(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/green/green_flying_5")),
    GREEN_FLYING_6(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/green/green_flying_6")),
    GREEN_FLYING_7(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/green/green_flying_7")),
    GREEN_FLYING_8(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/green/green_flying_8")),
    GREEN_FLYING_9(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/green/green_flying_9")),
    GREEN_FLYING_10(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/green/green_flying_10")),
    GREEN_FLYING_11(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/green/green_flying_11")),
    GREEN_FLYING_12(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/green/green_flying_12")),
    GREEN_FLYING_13(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/green/green_flying_13")),
    GREEN_FLYING_14(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/green/green_flying_14")),
    GREEN_FLYING_15(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/green/green_flying_15")),
    RUSTIC_0(new NamespacedKey(TARDIS.plugin, "time_rotor/rustic/rustic_0")),
    RUSTIC_1(new NamespacedKey(TARDIS.plugin, "time_rotor/rustic/rustic_1")),
    RUSTIC_2(new NamespacedKey(TARDIS.plugin, "time_rotor/rustic/rustic_2")),
    RUSTIC_3(new NamespacedKey(TARDIS.plugin, "time_rotor/rustic/rustic_3")),
    RUSTIC_4(new NamespacedKey(TARDIS.plugin, "time_rotor/rustic/rustic_4")),
    GREEN_FLYING_OPEN_0(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/green/green_flying_open_0")),
    GREEN_FLYING_OPEN_1(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/green/green_flying_open_1")),
    GREEN_FLYING_OPEN_2(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/green/green_flying_open_2")),
    GREEN_FLYING_OPEN_3(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/green/green_flying_open_3")),
    GREEN_FLYING_OPEN_4(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/green/green_flying_open_4")),
    GREEN_FLYING_OPEN_5(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/green/green_flying_open_5")),
    GREEN_FLYING_OPEN_6(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/green/green_flying_open_6")),
    GREEN_FLYING_OPEN_7(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/green/green_flying_open_7")),
    GREEN_FLYING_OPEN_8(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/green/green_flying_open_8")),
    GREEN_FLYING_OPEN_9(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/green/green_flying_open_9")),
    GREEN_FLYING_OPEN_10(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/green/green_flying_open_10")),
    GREEN_FLYING_OPEN_11(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/green/green_flying_open_11")),
    GREEN_FLYING_OPEN_12(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/green/green_flying_open_12")),
    GREEN_FLYING_OPEN_13(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/green/green_flying_open_13")),
    GREEN_FLYING_OPEN_14(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/green/green_flying_open_14")),
    GREEN_FLYING_OPEN_15(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/green/green_flying_open_15"));

    private final NamespacedKey key;

    GreenDye(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
