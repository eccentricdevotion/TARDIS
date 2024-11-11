package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum GoldNugget {

    BRASS_YALE(new NamespacedKey(TARDIS.plugin, "item/key/brass_yale")),
    BRASS_PLAIN(new NamespacedKey(TARDIS.plugin, "item/key/brass_plain")),
    SPADE_SHAPED(new NamespacedKey(TARDIS.plugin, "item/key/spade_shaped")),
    SILVER_YALE(new NamespacedKey(TARDIS.plugin, "item/key/silver_yale")),
    SEAL_OF_RASSILON(new NamespacedKey(TARDIS.plugin, "item/key/seal_of_rassilon")),
    SILVER_VARIANT(new NamespacedKey(TARDIS.plugin, "item/key/silver_variant")),
    SILVER_PLAIN(new NamespacedKey(TARDIS.plugin, "item/key/silver_plain")),
    SILVER_NEW(new NamespacedKey(TARDIS.plugin, "item/key/silver_new")),
    SILVER_ERA(new NamespacedKey(TARDIS.plugin, "item/key/silver_era")),
    SILVER_STRING(new NamespacedKey(TARDIS.plugin, "item/key/silver_string")),
    FILTER(new NamespacedKey(TARDIS.plugin, "item/key/filter")),
    BRASS_STRING(new NamespacedKey(TARDIS.plugin, "item/key/brass_string")),
    BROMLEY_GOLD(new NamespacedKey(TARDIS.plugin, "item/key/bromley_gold")),
    PERCEPTION_FILTER(new NamespacedKey(TARDIS.plugin, "item/key/perception_filter")),
    REMOTE(new NamespacedKey(TARDIS.plugin, "item/key/remote"));

    private final NamespacedKey key;

    GoldNugget(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
