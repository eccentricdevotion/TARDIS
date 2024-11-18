package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum BrownDye {

    BUTTON_TYPE(new NamespacedKey(TARDIS.plugin, "genetic/button_type")),
    TARDIS_BROWN(new NamespacedKey(TARDIS.plugin, "block/police_box/tardis_brown")),
    TARDIS_BROWN_OPEN(new NamespacedKey(TARDIS.plugin, "block/police_box/tardis_brown_open")),
    TARDIS_BROWN_STAINED(new NamespacedKey(TARDIS.plugin, "block/police_box/tardis_brown_stained")),
    TARDIS_GLASS(new NamespacedKey(TARDIS.plugin, "block/police_box/tardis_glass")),
    BROWN_FLYING_0(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/brown/brown_flying_0")),
    BROWN_FLYING_1(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/brown/brown_flying_1")),
    BROWN_FLYING_2(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/brown/brown_flying_2")),
    BROWN_FLYING_3(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/brown/brown_flying_3")),
    BROWN_FLYING_4(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/brown/brown_flying_4")),
    BROWN_FLYING_5(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/brown/brown_flying_5")),
    BROWN_FLYING_6(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/brown/brown_flying_6")),
    BROWN_FLYING_7(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/brown/brown_flying_7")),
    BROWN_FLYING_8(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/brown/brown_flying_8")),
    BROWN_FLYING_9(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/brown/brown_flying_9")),
    BROWN_FLYING_10(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/brown/brown_flying_10")),
    BROWN_FLYING_11(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/brown/brown_flying_11")),
    BROWN_FLYING_12(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/brown/brown_flying_12")),
    BROWN_FLYING_13(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/brown/brown_flying_13")),
    BROWN_FLYING_14(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/brown/brown_flying_14")),
    BROWN_FLYING_15(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/brown/brown_flying_15")),
    ELEVENTH_0(new NamespacedKey(TARDIS.plugin, "time_rotor/eleventh/eleventh_0")),
    ELEVENTH_1(new NamespacedKey(TARDIS.plugin, "time_rotor/eleventh/eleventh_1")),
    ELEVENTH_2(new NamespacedKey(TARDIS.plugin, "time_rotor/eleventh/eleventh_2")),
    ELEVENTH_3(new NamespacedKey(TARDIS.plugin, "time_rotor/eleventh/eleventh_3")),
    ELEVENTH_4(new NamespacedKey(TARDIS.plugin, "time_rotor/eleventh/eleventh_4")),
    BROWN_FLYING_OPEN_0(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/brown/brown_flying_open_0")),
    BROWN_FLYING_OPEN_1(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/brown/brown_flying_open_1")),
    BROWN_FLYING_OPEN_2(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/brown/brown_flying_open_2")),
    BROWN_FLYING_OPEN_3(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/brown/brown_flying_open_3")),
    BROWN_FLYING_OPEN_4(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/brown/brown_flying_open_4")),
    BROWN_FLYING_OPEN_5(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/brown/brown_flying_open_5")),
    BROWN_FLYING_OPEN_6(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/brown/brown_flying_open_6")),
    BROWN_FLYING_OPEN_7(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/brown/brown_flying_open_7")),
    BROWN_FLYING_OPEN_8(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/brown/brown_flying_open_8")),
    BROWN_FLYING_OPEN_9(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/brown/brown_flying_open_9")),
    BROWN_FLYING_OPEN_10(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/brown/brown_flying_open_10")),
    BROWN_FLYING_OPEN_11(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/brown/brown_flying_open_11")),
    BROWN_FLYING_OPEN_12(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/brown/brown_flying_open_12")),
    BROWN_FLYING_OPEN_13(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/brown/brown_flying_open_13")),
    BROWN_FLYING_OPEN_14(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/brown/brown_flying_open_14")),
    BROWN_FLYING_OPEN_15(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/brown/brown_flying_open_15")),
    SLIDE_RACK(new NamespacedKey(TARDIS.plugin, "equipment/slide_rack"));

    private final NamespacedKey key;

    BrownDye(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
