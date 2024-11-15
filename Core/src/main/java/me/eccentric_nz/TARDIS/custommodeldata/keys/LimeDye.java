package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum LimeDye {

    BUTTON_TYPE(new NamespacedKey(TARDIS.plugin, "item/genetic/button_type")),
    TARDIS_LIME(new NamespacedKey(TARDIS.plugin, "block/police_box/tardis_lime")),
    TARDIS_LIME_OPEN(new NamespacedKey(TARDIS.plugin, "block/police_box/tardis_lime_open")),
    TARDIS_LIME_STAINED(new NamespacedKey(TARDIS.plugin, "block/police_box/tardis_lime_stained")),
    TARDIS_GLASS(new NamespacedKey(TARDIS.plugin, "block/police_box/tardis_glass")),
    LIME_FLYING_0(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/lime/lime_flying_0")),
    LIME_FLYING_1(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/lime/lime_flying_1")),
    LIME_FLYING_2(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/lime/lime_flying_2")),
    LIME_FLYING_3(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/lime/lime_flying_3")),
    LIME_FLYING_4(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/lime/lime_flying_4")),
    LIME_FLYING_5(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/lime/lime_flying_5")),
    LIME_FLYING_6(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/lime/lime_flying_6")),
    LIME_FLYING_7(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/lime/lime_flying_7")),
    LIME_FLYING_8(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/lime/lime_flying_8")),
    LIME_FLYING_9(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/lime/lime_flying_9")),
    LIME_FLYING_10(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/lime/lime_flying_10")),
    LIME_FLYING_11(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/lime/lime_flying_11")),
    LIME_FLYING_12(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/lime/lime_flying_12")),
    LIME_FLYING_13(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/lime/lime_flying_13")),
    LIME_FLYING_14(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/lime/lime_flying_14")),
    LIME_FLYING_15(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/lime/lime_flying_15")),
    LIME_FLYING_OPEN_0(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/lime/lime_flying_open_0")),
    LIME_FLYING_OPEN_1(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/lime/lime_flying_open_1")),
    LIME_FLYING_OPEN_2(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/lime/lime_flying_open_2")),
    LIME_FLYING_OPEN_3(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/lime/lime_flying_open_3")),
    LIME_FLYING_OPEN_4(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/lime/lime_flying_open_4")),
    LIME_FLYING_OPEN_5(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/lime/lime_flying_open_5")),
    LIME_FLYING_OPEN_6(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/lime/lime_flying_open_6")),
    LIME_FLYING_OPEN_7(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/lime/lime_flying_open_7")),
    LIME_FLYING_OPEN_8(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/lime/lime_flying_open_8")),
    LIME_FLYING_OPEN_9(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/lime/lime_flying_open_9")),
    LIME_FLYING_OPEN_10(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/lime/lime_flying_open_10")),
    LIME_FLYING_OPEN_11(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/lime/lime_flying_open_11")),
    LIME_FLYING_OPEN_12(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/lime/lime_flying_open_12")),
    LIME_FLYING_OPEN_13(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/lime/lime_flying_open_13")),
    LIME_FLYING_OPEN_14(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/lime/lime_flying_open_14")),
    LIME_FLYING_OPEN_15(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/lime/lime_flying_open_15"));

    private final NamespacedKey key;

    LimeDye(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
