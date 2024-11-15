package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum ClayBall {

    TYPE_40_CLOSED(new NamespacedKey(TARDIS.plugin, "block/police_box/type_40_closed")),
    TYPE_40_OPEN(new NamespacedKey(TARDIS.plugin, "block/police_box/type_40_open")),
    TYPE_40_STAINED(new NamespacedKey(TARDIS.plugin, "block/police_box/type_40_stained")),
    TYPE_40_GLASS(new NamespacedKey(TARDIS.plugin, "block/police_box/type_40_glass")),
    TYPE_40_FLYING_0(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/type_40/type_40_flying_0")),
    TYPE_40_FLYING_1(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/type_40/type_40_flying_1")),
    TYPE_40_FLYING_2(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/type_40/type_40_flying_2")),
    TYPE_40_FLYING_3(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/type_40/type_40_flying_3")),
    TYPE_40_FLYING_4(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/type_40/type_40_flying_4")),
    TYPE_40_FLYING_5(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/type_40/type_40_flying_5")),
    TYPE_40_FLYING_6(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/type_40/type_40_flying_6")),
    TYPE_40_FLYING_7(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/type_40/type_40_flying_7")),
    TYPE_40_FLYING_8(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/type_40/type_40_flying_8")),
    TYPE_40_FLYING_9(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/type_40/type_40_flying_9")),
    TYPE_40_FLYING_10(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/type_40/type_40_flying_10")),
    TYPE_40_FLYING_11(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/type_40/type_40_flying_11")),
    TYPE_40_FLYING_12(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/type_40/type_40_flying_12")),
    TYPE_40_FLYING_13(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/type_40/type_40_flying_13")),
    TYPE_40_FLYING_14(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/type_40/type_40_flying_14")),
    TYPE_40_FLYING_15(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/type_40/type_40_flying_15")),
    TYPE_40_FLYING_OPEN_0(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/type_40/type_40_flying_open_0")),
    TYPE_40_FLYING_OPEN_1(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/type_40/type_40_flying_open_1")),
    TYPE_40_FLYING_OPEN_2(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/type_40/type_40_flying_open_2")),
    TYPE_40_FLYING_OPEN_3(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/type_40/type_40_flying_open_3")),
    TYPE_40_FLYING_OPEN_4(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/type_40/type_40_flying_open_4")),
    TYPE_40_FLYING_OPEN_5(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/type_40/type_40_flying_open_5")),
    TYPE_40_FLYING_OPEN_6(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/type_40/type_40_flying_open_6")),
    TYPE_40_FLYING_OPEN_7(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/type_40/type_40_flying_open_7")),
    TYPE_40_FLYING_OPEN_8(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/type_40/type_40_flying_open_8")),
    TYPE_40_FLYING_OPEN_9(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/type_40/type_40_flying_open_9")),
    TYPE_40_FLYING_OPEN_10(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/type_40/type_40_flying_open_10")),
    TYPE_40_FLYING_OPEN_11(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/type_40/type_40_flying_open_11")),
    TYPE_40_FLYING_OPEN_12(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/type_40/type_40_flying_open_12")),
    TYPE_40_FLYING_OPEN_13(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/type_40/type_40_flying_open_13")),
    TYPE_40_FLYING_OPEN_14(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/type_40/type_40_flying_open_14")),
    TYPE_40_FLYING_OPEN_15(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/type_40/type_40_flying_open_15"));

    private final NamespacedKey key;

    ClayBall(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
