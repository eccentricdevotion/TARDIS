package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum SycoraxVariant {

    BUTTON_SYCORAX(new NamespacedKey(TARDIS.plugin, "button_sycorax")),
    SYCORAX_HEAD(new NamespacedKey(TARDIS.plugin, "sycorax_head")),
    SYCORAX_DISGUISE(new NamespacedKey(TARDIS.plugin, "sycorax_disguise")),
    SYCORAX_STATIC(new NamespacedKey(TARDIS.plugin, "sycorax_static"));

    private final NamespacedKey key;

    SycoraxVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
