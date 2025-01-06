package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum StraxVariant {

    BUTTON_STRAX(new NamespacedKey(TARDIS.plugin, "button_strax")),
    STRAX_HEAD(new NamespacedKey(TARDIS.plugin, "strax_head")),
    STRAX_DISGUISE(new NamespacedKey(TARDIS.plugin, "strax_disguise")),
    STRAX_STATIC(new NamespacedKey(TARDIS.plugin, "strax_static"));

    private final NamespacedKey key;

    StraxVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}

