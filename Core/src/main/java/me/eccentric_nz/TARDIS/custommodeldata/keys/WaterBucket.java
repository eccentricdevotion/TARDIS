package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum WaterBucket {

    ACID_BUCKET(new NamespacedKey(TARDIS.plugin, "item/planets/acid_bucket")),
    RAIN(new NamespacedKey(TARDIS.plugin, "item/gui/rain"));

    private final NamespacedKey key;

    WaterBucket(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
