package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum MushroomStew {

    BOWL_OF_CUSTARD(new NamespacedKey(TARDIS.plugin, "item/food/bowl_of_custard"));

    private final NamespacedKey key;

    MushroomStew(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
