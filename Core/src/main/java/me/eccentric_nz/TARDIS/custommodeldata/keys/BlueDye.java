package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum BlueDye {

    BUTTON_TYPE(new NamespacedKey(TARDIS.plugin, "item/genetic/button_type")),
    TARDIS_BLUE(new NamespacedKey(TARDIS.plugin, "block/police_box/tardis_blue")),
    TARDIS_BLUE_OPEN(new NamespacedKey(TARDIS.plugin, "block/police_box/tardis_blue_open")),
    TARDIS_BLUE_STAINED(new NamespacedKey(TARDIS.plugin, "block/police_box/tardis_blue_stained")),
    TARDIS_GLASS(new NamespacedKey(TARDIS.plugin, "block/police_box/tardis_glass")),
    BLUE_FLYING_0(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/blue/blue_flying_0")),
    BLUE_FLYING_1(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/blue/blue_flying_1")),
    BLUE_FLYING_2(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/blue/blue_flying_2")),
    BLUE_FLYING_3(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/blue/blue_flying_3")),
    BLUE_FLYING_4(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/blue/blue_flying_4")),
    BLUE_FLYING_5(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/blue/blue_flying_5")),
    BLUE_FLYING_6(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/blue/blue_flying_6")),
    BLUE_FLYING_7(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/blue/blue_flying_7")),
    BLUE_FLYING_8(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/blue/blue_flying_8")),
    BLUE_FLYING_9(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/blue/blue_flying_9")),
    BLUE_FLYING_10(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/blue/blue_flying_10")),
    BLUE_FLYING_11(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/blue/blue_flying_11")),
    BLUE_FLYING_12(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/blue/blue_flying_12")),
    BLUE_FLYING_13(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/blue/blue_flying_13")),
    BLUE_FLYING_14(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/blue/blue_flying_14")),
    BLUE_FLYING_15(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/blue/blue_flying_15")),
    ENGINE_ROTOR_0(new NamespacedKey(TARDIS.plugin, "item/time_rotor/engine_rotor/engine_rotor_0")),
    ENGINE_ROTOR_1(new NamespacedKey(TARDIS.plugin, "item/time_rotor/engine_rotor/engine_rotor_1")),
    ENGINE_ROTOR_2(new NamespacedKey(TARDIS.plugin, "item/time_rotor/engine_rotor/engine_rotor_2")),
    ENGINE_ROTOR_3(new NamespacedKey(TARDIS.plugin, "item/time_rotor/engine_rotor/engine_rotor_3")),
    ENGINE_ROTOR_4(new NamespacedKey(TARDIS.plugin, "item/time_rotor/engine_rotor/engine_rotor_4")),
    ENGINE_ROTOR_5(new NamespacedKey(TARDIS.plugin, "item/time_rotor/engine_rotor/engine_rotor_5")),
    BLUE_FLYING_OPEN_0(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/blue/blue_flying_open_0")),
    BLUE_FLYING_OPEN_1(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/blue/blue_flying_open_1")),
    BLUE_FLYING_OPEN_2(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/blue/blue_flying_open_2")),
    BLUE_FLYING_OPEN_3(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/blue/blue_flying_open_3")),
    BLUE_FLYING_OPEN_4(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/blue/blue_flying_open_4")),
    BLUE_FLYING_OPEN_5(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/blue/blue_flying_open_5")),
    BLUE_FLYING_OPEN_6(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/blue/blue_flying_open_6")),
    BLUE_FLYING_OPEN_7(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/blue/blue_flying_open_7")),
    BLUE_FLYING_OPEN_8(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/blue/blue_flying_open_8")),
    BLUE_FLYING_OPEN_9(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/blue/blue_flying_open_9")),
    BLUE_FLYING_OPEN_10(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/blue/blue_flying_open_10")),
    BLUE_FLYING_OPEN_11(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/blue/blue_flying_open_11")),
    BLUE_FLYING_OPEN_12(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/blue/blue_flying_open_12")),
    BLUE_FLYING_OPEN_13(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/blue/blue_flying_open_13")),
    BLUE_FLYING_OPEN_14(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/blue/blue_flying_open_14")),
    BLUE_FLYING_OPEN_15(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/blue/blue_flying_open_15"));

    private final NamespacedKey key;

    BlueDye(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
