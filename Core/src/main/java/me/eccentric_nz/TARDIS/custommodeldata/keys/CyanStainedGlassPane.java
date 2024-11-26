package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum CyanStainedGlassPane {

    TARDIS_TENNANT(new NamespacedKey(TARDIS.plugin, "block/police_box/tardis_tennant")),
    TARDIS_TENNANT_OPEN(new NamespacedKey(TARDIS.plugin, "block/police_box/tardis_tennant_open")),
    TARDIS_TENNANT_STAINED(new NamespacedKey(TARDIS.plugin, "block/police_box/tardis_tennant_stained")),
    TARDIS_GLASS(new NamespacedKey(TARDIS.plugin, "block/police_box/tardis_glass")),
    TENNANT_FLYING_0(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/tennant/tennant_flying_0")),
    TENNANT_FLYING_1(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/tennant/tennant_flying_1")),
    TENNANT_FLYING_2(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/tennant/tennant_flying_2")),
    TENNANT_FLYING_3(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/tennant/tennant_flying_3")),
    TENNANT_FLYING_4(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/tennant/tennant_flying_4")),
    TENNANT_FLYING_5(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/tennant/tennant_flying_5")),
    TENNANT_FLYING_6(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/tennant/tennant_flying_6")),
    TENNANT_FLYING_7(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/tennant/tennant_flying_7")),
    TENNANT_FLYING_8(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/tennant/tennant_flying_8")),
    TENNANT_FLYING_9(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/tennant/tennant_flying_9")),
    TENNANT_FLYING_10(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/tennant/tennant_flying_10")),
    TENNANT_FLYING_11(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/tennant/tennant_flying_11")),
    TENNANT_FLYING_12(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/tennant/tennant_flying_12")),
    TENNANT_FLYING_13(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/tennant/tennant_flying_13")),
    TENNANT_FLYING_14(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/tennant/tennant_flying_14")),
    TENNANT_FLYING_15(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/tennant/tennant_flying_15")),
    TENNANT_FLYING_OPEN_0(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/tennant/tennant_flying_open_0")),
    TENNANT_FLYING_OPEN_1(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/tennant/tennant_flying_open_1")),
    TENNANT_FLYING_OPEN_2(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/tennant/tennant_flying_open_2")),
    TENNANT_FLYING_OPEN_3(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/tennant/tennant_flying_open_3")),
    TENNANT_FLYING_OPEN_4(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/tennant/tennant_flying_open_4")),
    TENNANT_FLYING_OPEN_5(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/tennant/tennant_flying_open_5")),
    TENNANT_FLYING_OPEN_6(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/tennant/tennant_flying_open_6")),
    TENNANT_FLYING_OPEN_7(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/tennant/tennant_flying_open_7")),
    TENNANT_FLYING_OPEN_8(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/tennant/tennant_flying_open_8")),
    TENNANT_FLYING_OPEN_9(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/tennant/tennant_flying_open_9")),
    TENNANT_FLYING_OPEN_10(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/tennant/tennant_flying_open_10")),
    TENNANT_FLYING_OPEN_11(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/tennant/tennant_flying_open_11")),
    TENNANT_FLYING_OPEN_12(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/tennant/tennant_flying_open_12")),
    TENNANT_FLYING_OPEN_13(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/tennant/tennant_flying_open_13")),
    TENNANT_FLYING_OPEN_14(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/tennant/tennant_flying_open_14")),
    TENNANT_FLYING_OPEN_15(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/tennant/tennant_flying_open_15"));

    private final NamespacedKey key;

    CyanStainedGlassPane(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
