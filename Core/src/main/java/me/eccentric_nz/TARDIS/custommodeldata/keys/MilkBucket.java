package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum MilkBucket {

    ANTIDOTE(new NamespacedKey(TARDIS.plugin, "item/products/antidote")),
    ELIXIR(new NamespacedKey(TARDIS.plugin, "item/products/elixir")),
    EYEDROPS(new NamespacedKey(TARDIS.plugin, "item/products/eyedrops")),
    TONIC(new NamespacedKey(TARDIS.plugin, "item/products/tonic"));

    private final NamespacedKey key;

    MilkBucket(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
