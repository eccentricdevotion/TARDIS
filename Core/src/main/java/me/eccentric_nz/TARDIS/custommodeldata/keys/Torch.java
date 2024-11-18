package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum Torch {


    ANGEL_OF_LIBERTY_TORCH(new NamespacedKey(TARDIS.plugin, "lazarus/angel_of_liberty_torch"));

    private final NamespacedKey key;

    Torch(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
