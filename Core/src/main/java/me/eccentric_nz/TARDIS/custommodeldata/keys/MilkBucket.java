package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum MilkBucket {

    ANTIDOTE(new NamespacedKey(TARDIS.plugin, "products/antidote")),
    ELIXIR(new NamespacedKey(TARDIS.plugin, "products/elixir")),
    EYEDROPS(new NamespacedKey(TARDIS.plugin, "products/eyedrops")),
    TONIC(new NamespacedKey(TARDIS.plugin, "products/tonic"));

    private final NamespacedKey key;

    MilkBucket(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
