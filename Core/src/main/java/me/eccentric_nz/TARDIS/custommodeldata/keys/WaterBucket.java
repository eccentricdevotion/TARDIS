package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum WaterBucket {

    ACID_BUCKET(new NamespacedKey(TARDIS.plugin, "planets/acid_bucket")),
    RAIN(new NamespacedKey(TARDIS.plugin, "gui/rain"));

    private final NamespacedKey key;

    WaterBucket(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
