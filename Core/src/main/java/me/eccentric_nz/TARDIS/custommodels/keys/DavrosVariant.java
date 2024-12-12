package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum DavrosVariant {

    BUTTON_DAVROS(new NamespacedKey(TARDIS.plugin, "button_davros")),
    DAVROS_HEAD(new NamespacedKey(TARDIS.plugin, "davros_head")),
    DAVROS_DISGUISE(new NamespacedKey(TARDIS.plugin, "davros_disguise")),
    DAVROS(new NamespacedKey(TARDIS.plugin, "davros"));

    private final NamespacedKey key;

    DavrosVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
