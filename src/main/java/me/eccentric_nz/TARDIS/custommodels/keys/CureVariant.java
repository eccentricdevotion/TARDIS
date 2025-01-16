package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum CureVariant {

    ANTIDOTE(new NamespacedKey(TARDIS.plugin, "product_antidote")),
    ELIXIR(new NamespacedKey(TARDIS.plugin, "product_elixir")),
    EYEDROPS(new NamespacedKey(TARDIS.plugin, "product_eyedrops")),
    TONIC(new NamespacedKey(TARDIS.plugin, "product_tonic"));

    private final NamespacedKey key;

    CureVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
