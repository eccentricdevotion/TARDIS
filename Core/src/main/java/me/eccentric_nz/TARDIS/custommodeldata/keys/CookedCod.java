package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum CookedCod {

    FISH_FINGER(new NamespacedKey(TARDIS.plugin, "item/food/fish_finger"));

    private final NamespacedKey key;

    CookedCod(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
