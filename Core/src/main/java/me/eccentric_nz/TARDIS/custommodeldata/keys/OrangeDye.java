package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum OrangeDye {

    BUTTON_TYPE(new NamespacedKey(TARDIS.plugin, "genetic/button_type")),
    TARDIS_GLASS(new NamespacedKey(TARDIS.plugin, "block/police_box/tardis_glass")),
    TARDIS_ORANGE(new NamespacedKey(TARDIS.plugin, "block/police_box/tardis_orange")),
    TARDIS_ORANGE_OPEN(new NamespacedKey(TARDIS.plugin, "block/police_box/tardis_orange_open")),
    TARDIS_ORANGE_STAINED(new NamespacedKey(TARDIS.plugin, "block/police_box/tardis_orange_stained")),
    ORANGE_FLYING_0(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/orange/orange_flying_0")),
    ORANGE_FLYING_1(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/orange/orange_flying_1")),
    ORANGE_FLYING_2(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/orange/orange_flying_2")),
    ORANGE_FLYING_3(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/orange/orange_flying_3")),
    ORANGE_FLYING_4(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/orange/orange_flying_4")),
    ORANGE_FLYING_5(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/orange/orange_flying_5")),
    ORANGE_FLYING_6(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/orange/orange_flying_6")),
    ORANGE_FLYING_7(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/orange/orange_flying_7")),
    ORANGE_FLYING_8(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/orange/orange_flying_8")),
    ORANGE_FLYING_9(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/orange/orange_flying_9")),
    ORANGE_FLYING_10(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/orange/orange_flying_10")),
    ORANGE_FLYING_11(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/orange/orange_flying_11")),
    ORANGE_FLYING_12(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/orange/orange_flying_12")),
    ORANGE_FLYING_13(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/orange/orange_flying_13")),
    ORANGE_FLYING_14(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/orange/orange_flying_14")),
    ORANGE_FLYING_15(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/orange/orange_flying_15")),
    TENNANT_0(new NamespacedKey(TARDIS.plugin, "time_rotor/tennant/tennant_0")),
    TENNANT_1(new NamespacedKey(TARDIS.plugin, "time_rotor/tennant/tennant_1")),
    TENNANT_2(new NamespacedKey(TARDIS.plugin, "time_rotor/tennant/tennant_2")),
    TENNANT_3(new NamespacedKey(TARDIS.plugin, "time_rotor/tennant/tennant_3")),
    TENNANT_4(new NamespacedKey(TARDIS.plugin, "time_rotor/tennant/tennant_4")),
    ORANGE_FLYING_OPEN_0(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/orange/orange_flying_open_0")),
    ORANGE_FLYING_OPEN_1(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/orange/orange_flying_open_1")),
    ORANGE_FLYING_OPEN_2(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/orange/orange_flying_open_2")),
    ORANGE_FLYING_OPEN_3(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/orange/orange_flying_open_3")),
    ORANGE_FLYING_OPEN_4(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/orange/orange_flying_open_4")),
    ORANGE_FLYING_OPEN_5(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/orange/orange_flying_open_5")),
    ORANGE_FLYING_OPEN_6(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/orange/orange_flying_open_6")),
    ORANGE_FLYING_OPEN_7(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/orange/orange_flying_open_7")),
    ORANGE_FLYING_OPEN_8(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/orange/orange_flying_open_8")),
    ORANGE_FLYING_OPEN_9(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/orange/orange_flying_open_9")),
    ORANGE_FLYING_OPEN_10(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/orange/orange_flying_open_10")),
    ORANGE_FLYING_OPEN_11(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/orange/orange_flying_open_11")),
    ORANGE_FLYING_OPEN_12(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/orange/orange_flying_open_12")),
    ORANGE_FLYING_OPEN_13(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/orange/orange_flying_open_13")),
    ORANGE_FLYING_OPEN_14(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/orange/orange_flying_open_14")),
    ORANGE_FLYING_OPEN_15(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/orange/orange_flying_open_15"));

    private final NamespacedKey key;

    OrangeDye(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
