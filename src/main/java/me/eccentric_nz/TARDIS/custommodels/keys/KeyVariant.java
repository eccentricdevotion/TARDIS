package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum KeyVariant {

    BRASS_YALE(new NamespacedKey(TARDIS.plugin, "brass_yale_key")),
    BRASS_PLAIN(new NamespacedKey(TARDIS.plugin, "brass_plain_key")),
    SPADE_SHAPED(new NamespacedKey(TARDIS.plugin, "spade_shaped_key")),
    SILVER_YALE(new NamespacedKey(TARDIS.plugin, "silver_yale_key")),
    SEAL_OF_RASSILON(new NamespacedKey(TARDIS.plugin, "seal_of_rassilon_key")),
    SILVER_VARIANT(new NamespacedKey(TARDIS.plugin, "silver_variant_key")),
    SILVER_PLAIN(new NamespacedKey(TARDIS.plugin, "silver_plain_key")),
    SILVER_NEW(new NamespacedKey(TARDIS.plugin, "silver_new_key")),
    SILVER_ERA(new NamespacedKey(TARDIS.plugin, "silver_era_key")),
    SILVER_STRING(new NamespacedKey(TARDIS.plugin, "silver_string_key")),
    FILTER(new NamespacedKey(TARDIS.plugin, "perception_filter_key")),
    BRASS_STRING(new NamespacedKey(TARDIS.plugin, "brass_string_key")),
    BROMLEY_GOLD(new NamespacedKey(TARDIS.plugin, "bromley_gold_key")),
    PERCEPTION_FILTER(new NamespacedKey(TARDIS.plugin, "perception_filter_string_key")),
    REMOTE(new NamespacedKey(TARDIS.plugin, "remote_key"));

    private final NamespacedKey key;

    KeyVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
