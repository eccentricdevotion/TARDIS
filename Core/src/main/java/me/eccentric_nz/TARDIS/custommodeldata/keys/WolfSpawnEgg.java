package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum WolfSpawnEgg {

    TARDIS_BAD_WOLF_CLOSED(new NamespacedKey(TARDIS.plugin, "block/police_box/tardis_bad_wolf_closed")),
    TARDIS_BAD_WOLF_OPEN(new NamespacedKey(TARDIS.plugin, "block/police_box/tardis_bad_wolf_open")),
    TARDIS_BLUE_STAINED(new NamespacedKey(TARDIS.plugin, "block/police_box/tardis_blue_stained")),
    TARDIS_GLASS(new NamespacedKey(TARDIS.plugin, "block/police_box/tardis_glass")),
    BAD_WOLF_FLYING_0(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/bad_wolf/bad_wolf_flying_0")),
    BAD_WOLF_FLYING_1(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/bad_wolf/bad_wolf_flying_1")),
    BAD_WOLF_FLYING_2(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/bad_wolf/bad_wolf_flying_2")),
    BAD_WOLF_FLYING_3(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/bad_wolf/bad_wolf_flying_3")),
    BAD_WOLF_FLYING_4(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/bad_wolf/bad_wolf_flying_4")),
    BAD_WOLF_FLYING_5(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/bad_wolf/bad_wolf_flying_5")),
    BAD_WOLF_FLYING_6(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/bad_wolf/bad_wolf_flying_6")),
    BAD_WOLF_FLYING_7(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/bad_wolf/bad_wolf_flying_7")),
    BAD_WOLF_FLYING_8(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/bad_wolf/bad_wolf_flying_8")),
    BAD_WOLF_FLYING_9(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/bad_wolf/bad_wolf_flying_9")),
    BAD_WOLF_FLYING_10(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/bad_wolf/bad_wolf_flying_10")),
    BAD_WOLF_FLYING_11(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/bad_wolf/bad_wolf_flying_11")),
    BAD_WOLF_FLYING_12(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/bad_wolf/bad_wolf_flying_12")),
    BAD_WOLF_FLYING_13(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/bad_wolf/bad_wolf_flying_13")),
    BAD_WOLF_FLYING_14(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/bad_wolf/bad_wolf_flying_14")),
    BAD_WOLF_FLYING_15(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/bad_wolf/bad_wolf_flying_15")),
    TINTED_FLYING_OPEN_0(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/tinted/tinted_flying_open_0")),
    TINTED_FLYING_OPEN_1(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/tinted/tinted_flying_open_1")),
    TINTED_FLYING_OPEN_2(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/tinted/tinted_flying_open_2")),
    TINTED_FLYING_OPEN_3(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/tinted/tinted_flying_open_3")),
    TINTED_FLYING_OPEN_4(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/tinted/tinted_flying_open_4")),
    TINTED_FLYING_OPEN_5(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/tinted/tinted_flying_open_5")),
    TINTED_FLYING_OPEN_6(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/tinted/tinted_flying_open_6")),
    TINTED_FLYING_OPEN_7(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/tinted/tinted_flying_open_7")),
    TINTED_FLYING_OPEN_8(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/tinted/tinted_flying_open_8")),
    TINTED_FLYING_OPEN_9(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/tinted/tinted_flying_open_9")),
    TINTED_FLYING_OPEN_10(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/tinted/tinted_flying_open_10")),
    TINTED_FLYING_OPEN_11(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/tinted/tinted_flying_open_11")),
    TINTED_FLYING_OPEN_12(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/tinted/tinted_flying_open_12")),
    TINTED_FLYING_OPEN_13(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/tinted/tinted_flying_open_13")),
    TINTED_FLYING_OPEN_14(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/tinted/tinted_flying_open_14")),
    TINTED_FLYING_OPEN_15(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/tinted/tinted_flying_open_15"));

    private final NamespacedKey key;

    WolfSpawnEgg(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
