package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum EnderPearl {

    PANDORICA(new NamespacedKey(TARDIS.plugin, "block/pandorica/pandorica")),
    PANDORICA_OPEN(new NamespacedKey(TARDIS.plugin, "block/pandorica/pandorica_open")),
    PANDORICA_STAINED(new NamespacedKey(TARDIS.plugin, "block/pandorica/pandorica_stained")),
    PANDORICA_GLASS(new NamespacedKey(TARDIS.plugin, "block/pandorica/pandorica_glass")),
    PANDORICA_75(new NamespacedKey(TARDIS.plugin, "block/pandorica/pandorica_75")),
    PANDORICA_50(new NamespacedKey(TARDIS.plugin, "block/pandorica/pandorica_50")),
    PANDORICA_25(new NamespacedKey(TARDIS.plugin, "block/pandorica/pandorica_25")),
    PANDORICA_FLYING_0(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/pandorica/pandorica_flying_0")),
    PANDORICA_FLYING_1(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/pandorica/pandorica_flying_1")),
    PANDORICA_FLYING_2(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/pandorica/pandorica_flying_2")),
    PANDORICA_FLYING_3(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/pandorica/pandorica_flying_3")),
    PANDORICA_FLYING_4(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/pandorica/pandorica_flying_4")),
    PANDORICA_FLYING_5(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/pandorica/pandorica_flying_5")),
    PANDORICA_FLYING_6(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/pandorica/pandorica_flying_6")),
    PANDORICA_FLYING_7(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/pandorica/pandorica_flying_7")),
    PANDORICA_FLYING_8(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/pandorica/pandorica_flying_8")),
    PANDORICA_FLYING_9(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/pandorica/pandorica_flying_9")),
    PANDORICA_FLYING_10(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/pandorica/pandorica_flying_10")),
    PANDORICA_FLYING_11(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/pandorica/pandorica_flying_11")),
    PANDORICA_FLYING_12(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/pandorica/pandorica_flying_12")),
    PANDORICA_FLYING_13(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/pandorica/pandorica_flying_13")),
    PANDORICA_FLYING_14(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/pandorica/pandorica_flying_14")),
    PANDORICA_FLYING_15(new NamespacedKey(TARDIS.plugin, "block/police_box/flying/pandorica/pandorica_flying_15"));

    private final NamespacedKey key;

    EnderPearl(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
