package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum PinkDye {

    BUTTON_TYPE(new NamespacedKey(TARDIS.plugin, "item/genetic/button_type")),
    TARDIS_PINK(new NamespacedKey(TARDIS.plugin, "block/police_box/tardis_pink")),
    TARDIS_PINK_OPEN(new NamespacedKey(TARDIS.plugin, "block/police_box/tardis_pink_open")),
    TARDIS_PINK_STAINED(new NamespacedKey(TARDIS.plugin, "block/police_box/tardis_pink_stained")),
    TARDIS_GLASS(new NamespacedKey(TARDIS.plugin, "block/police_box/tardis_glass")),
    PINK_FLYING_0(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/pink/pink_flying_0")),
    PINK_FLYING_1(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/pink/pink_flying_1")),
    PINK_FLYING_2(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/pink/pink_flying_2")),
    PINK_FLYING_3(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/pink/pink_flying_3")),
    PINK_FLYING_4(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/pink/pink_flying_4")),
    PINK_FLYING_5(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/pink/pink_flying_5")),
    PINK_FLYING_6(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/pink/pink_flying_6")),
    PINK_FLYING_7(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/pink/pink_flying_7")),
    PINK_FLYING_8(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/pink/pink_flying_8")),
    PINK_FLYING_9(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/pink/pink_flying_9")),
    PINK_FLYING_10(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/pink/pink_flying_10")),
    PINK_FLYING_11(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/pink/pink_flying_11")),
    PINK_FLYING_12(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/pink/pink_flying_12")),
    PINK_FLYING_13(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/pink/pink_flying_13")),
    PINK_FLYING_14(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/pink/pink_flying_14")),
    PINK_FLYING_15(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/pink/pink_flying_15")),
    PINK_FLYING_OPEN_0(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/pink/pink_flying_open_0")),
    PINK_FLYING_OPEN_1(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/pink/pink_flying_open_1")),
    PINK_FLYING_OPEN_2(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/pink/pink_flying_open_2")),
    PINK_FLYING_OPEN_3(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/pink/pink_flying_open_3")),
    PINK_FLYING_OPEN_4(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/pink/pink_flying_open_4")),
    PINK_FLYING_OPEN_5(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/pink/pink_flying_open_5")),
    PINK_FLYING_OPEN_6(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/pink/pink_flying_open_6")),
    PINK_FLYING_OPEN_7(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/pink/pink_flying_open_7")),
    PINK_FLYING_OPEN_8(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/pink/pink_flying_open_8")),
    PINK_FLYING_OPEN_9(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/pink/pink_flying_open_9")),
    PINK_FLYING_OPEN_10(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/pink/pink_flying_open_10")),
    PINK_FLYING_OPEN_11(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/pink/pink_flying_open_11")),
    PINK_FLYING_OPEN_12(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/pink/pink_flying_open_12")),
    PINK_FLYING_OPEN_13(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/pink/pink_flying_open_13")),
    PINK_FLYING_OPEN_14(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/pink/pink_flying_open_14")),
    PINK_FLYING_OPEN_15(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/pink/pink_flying_open_15"));

    private final NamespacedKey key;

    PinkDye(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
