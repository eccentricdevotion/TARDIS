package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum ColouredVariant {

    TARDIS_TINTED(new NamespacedKey(TARDIS.plugin, "police_box/tardis_tinted")),
    TARDIS_TINTED_OPEN(new NamespacedKey(TARDIS.plugin, "police_box/tardis_tinted_open")),
    TARDIS_STAINED_TINTED(new NamespacedKey(TARDIS.plugin, "police_box/tardis_stained_tinted")),
    TARDIS_GLASS(new NamespacedKey(TARDIS.plugin, "police_box/tardis_glass")),
    TINTED_FLYING_0(new NamespacedKey(TARDIS.plugin, "police_box/flying/tinted/tinted_flying_0")),
    TINTED_FLYING_1(new NamespacedKey(TARDIS.plugin, "police_box/flying/tinted/tinted_flying_1")),
    TINTED_FLYING_2(new NamespacedKey(TARDIS.plugin, "police_box/flying/tinted/tinted_flying_2")),
    TINTED_FLYING_3(new NamespacedKey(TARDIS.plugin, "police_box/flying/tinted/tinted_flying_3")),
    TINTED_FLYING_4(new NamespacedKey(TARDIS.plugin, "police_box/flying/tinted/tinted_flying_4")),
    TINTED_FLYING_5(new NamespacedKey(TARDIS.plugin, "police_box/flying/tinted/tinted_flying_5")),
    TINTED_FLYING_6(new NamespacedKey(TARDIS.plugin, "police_box/flying/tinted/tinted_flying_6")),
    TINTED_FLYING_7(new NamespacedKey(TARDIS.plugin, "police_box/flying/tinted/tinted_flying_7")),
    TINTED_FLYING_8(new NamespacedKey(TARDIS.plugin, "police_box/flying/tinted/tinted_flying_8")),
    TINTED_FLYING_9(new NamespacedKey(TARDIS.plugin, "police_box/flying/tinted/tinted_flying_9")),
    TINTED_FLYING_10(new NamespacedKey(TARDIS.plugin, "police_box/flying/tinted/tinted_flying_10")),
    TINTED_FLYING_11(new NamespacedKey(TARDIS.plugin, "police_box/flying/tinted/tinted_flying_11")),
    TINTED_FLYING_12(new NamespacedKey(TARDIS.plugin, "police_box/flying/tinted/tinted_flying_12")),
    TINTED_FLYING_13(new NamespacedKey(TARDIS.plugin, "police_box/flying/tinted/tinted_flying_13")),
    TINTED_FLYING_14(new NamespacedKey(TARDIS.plugin, "police_box/flying/tinted/tinted_flying_14")),
    TINTED_FLYING_15(new NamespacedKey(TARDIS.plugin, "police_box/flying/tinted/tinted_flying_15")),
    TINT(new NamespacedKey(TARDIS.plugin, "police_box/tint")),
    TINTED_FLYING_OPEN_0(new NamespacedKey(TARDIS.plugin, "police_box/flying/tinted/tinted_flying_open_0")),
    TINTED_FLYING_OPEN_1(new NamespacedKey(TARDIS.plugin, "police_box/flying/tinted/tinted_flying_open_1")),
    TINTED_FLYING_OPEN_2(new NamespacedKey(TARDIS.plugin, "police_box/flying/tinted/tinted_flying_open_2")),
    TINTED_FLYING_OPEN_3(new NamespacedKey(TARDIS.plugin, "police_box/flying/tinted/tinted_flying_open_3")),
    TINTED_FLYING_OPEN_4(new NamespacedKey(TARDIS.plugin, "police_box/flying/tinted/tinted_flying_open_4")),
    TINTED_FLYING_OPEN_5(new NamespacedKey(TARDIS.plugin, "police_box/flying/tinted/tinted_flying_open_5")),
    TINTED_FLYING_OPEN_6(new NamespacedKey(TARDIS.plugin, "police_box/flying/tinted/tinted_flying_open_6")),
    TINTED_FLYING_OPEN_7(new NamespacedKey(TARDIS.plugin, "police_box/flying/tinted/tinted_flying_open_7")),
    TINTED_FLYING_OPEN_8(new NamespacedKey(TARDIS.plugin, "police_box/flying/tinted/tinted_flying_open_8")),
    TINTED_FLYING_OPEN_9(new NamespacedKey(TARDIS.plugin, "police_box/flying/tinted/tinted_flying_open_9")),
    TINTED_FLYING_OPEN_10(new NamespacedKey(TARDIS.plugin, "police_box/flying/tinted/tinted_flying_open_10")),
    TINTED_FLYING_OPEN_11(new NamespacedKey(TARDIS.plugin, "police_box/flying/tinted/tinted_flying_open_11")),
    TINTED_FLYING_OPEN_12(new NamespacedKey(TARDIS.plugin, "police_box/flying/tinted/tinted_flying_open_12")),
    TINTED_FLYING_OPEN_13(new NamespacedKey(TARDIS.plugin, "police_box/flying/tinted/tinted_flying_open_13")),
    TINTED_FLYING_OPEN_14(new NamespacedKey(TARDIS.plugin, "police_box/flying/tinted/tinted_flying_open_14")),
    TINTED_FLYING_OPEN_15(new NamespacedKey(TARDIS.plugin, "police_box/flying/tinted/tinted_flying_open_15"));

    private final NamespacedKey key;

    ColouredVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
