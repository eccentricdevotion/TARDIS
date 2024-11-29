package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum KeyVariant {

    BRASS_YALE(new NamespacedKey(TARDIS.plugin, "key/brass_yale")),
    BRASS_PLAIN(new NamespacedKey(TARDIS.plugin, "key/brass_plain")),
    SPADE_SHAPED(new NamespacedKey(TARDIS.plugin, "key/spade_shaped")),
    SILVER_YALE(new NamespacedKey(TARDIS.plugin, "key/silver_yale")),
    SEAL_OF_RASSILON(new NamespacedKey(TARDIS.plugin, "key/seal_of_rassilon")),
    SILVER_VARIANT(new NamespacedKey(TARDIS.plugin, "key/silver_variant")),
    SILVER_PLAIN(new NamespacedKey(TARDIS.plugin, "key/silver_plain")),
    SILVER_NEW(new NamespacedKey(TARDIS.plugin, "key/silver_new")),
    SILVER_ERA(new NamespacedKey(TARDIS.plugin, "key/silver_era")),
    SILVER_STRING(new NamespacedKey(TARDIS.plugin, "key/silver_string")),
    FILTER(new NamespacedKey(TARDIS.plugin, "key/filter")),
    BRASS_STRING(new NamespacedKey(TARDIS.plugin, "key/brass_string")),
    BROMLEY_GOLD(new NamespacedKey(TARDIS.plugin, "key/bromley_gold")),
    PERCEPTION_FILTER(new NamespacedKey(TARDIS.plugin, "key/perception_filter")),
    REMOTE(new NamespacedKey(TARDIS.plugin, "key/remote"));

    private final NamespacedKey key;

    KeyVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
