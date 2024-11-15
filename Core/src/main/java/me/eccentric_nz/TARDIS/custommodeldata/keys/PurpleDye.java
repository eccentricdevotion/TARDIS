package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum PurpleDye {

    BUTTON_TYPE(new NamespacedKey(TARDIS.plugin, "item/genetic/button_type")),
    TARDIS_PURPLE(new NamespacedKey(TARDIS.plugin, "block/police_box/tardis_purple")),
    TARDIS_PURPLE_OPEN(new NamespacedKey(TARDIS.plugin, "block/police_box/tardis_purple_open")),
    TARDIS_PURPLE_STAINED(new NamespacedKey(TARDIS.plugin, "block/police_box/tardis_purple_stained")),
    TARDIS_GLASS(new NamespacedKey(TARDIS.plugin, "block/police_box/tardis_glass")),
    PURPLE_FLYING_0(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/purple/purple_flying_0")),
    PURPLE_FLYING_1(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/purple/purple_flying_1")),
    PURPLE_FLYING_2(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/purple/purple_flying_2")),
    PURPLE_FLYING_3(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/purple/purple_flying_3")),
    PURPLE_FLYING_4(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/purple/purple_flying_4")),
    PURPLE_FLYING_5(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/purple/purple_flying_5")),
    PURPLE_FLYING_6(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/purple/purple_flying_6")),
    PURPLE_FLYING_7(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/purple/purple_flying_7")),
    PURPLE_FLYING_8(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/purple/purple_flying_8")),
    PURPLE_FLYING_9(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/purple/purple_flying_9")),
    PURPLE_FLYING_10(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/purple/purple_flying_10")),
    PURPLE_FLYING_11(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/purple/purple_flying_11")),
    PURPLE_FLYING_12(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/purple/purple_flying_12")),
    PURPLE_FLYING_13(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/purple/purple_flying_13")),
    PURPLE_FLYING_14(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/purple/purple_flying_14")),
    PURPLE_FLYING_15(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/purple/purple_flying_15")),
    PURPLE_FLYING_OPEN_0(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/purple/purple_flying_open_0")),
    PURPLE_FLYING_OPEN_1(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/purple/purple_flying_open_1")),
    PURPLE_FLYING_OPEN_2(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/purple/purple_flying_open_2")),
    PURPLE_FLYING_OPEN_3(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/purple/purple_flying_open_3")),
    PURPLE_FLYING_OPEN_4(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/purple/purple_flying_open_4")),
    PURPLE_FLYING_OPEN_5(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/purple/purple_flying_open_5")),
    PURPLE_FLYING_OPEN_6(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/purple/purple_flying_open_6")),
    PURPLE_FLYING_OPEN_7(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/purple/purple_flying_open_7")),
    PURPLE_FLYING_OPEN_8(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/purple/purple_flying_open_8")),
    PURPLE_FLYING_OPEN_9(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/purple/purple_flying_open_9")),
    PURPLE_FLYING_OPEN_10(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/purple/purple_flying_open_10")),
    PURPLE_FLYING_OPEN_11(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/purple/purple_flying_open_11")),
    PURPLE_FLYING_OPEN_12(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/purple/purple_flying_open_12")),
    PURPLE_FLYING_OPEN_13(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/purple/purple_flying_open_13")),
    PURPLE_FLYING_OPEN_14(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/purple/purple_flying_open_14")),
    PURPLE_FLYING_OPEN_15(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/purple/purple_flying_open_15"));

    private final NamespacedKey key;

    PurpleDye(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
