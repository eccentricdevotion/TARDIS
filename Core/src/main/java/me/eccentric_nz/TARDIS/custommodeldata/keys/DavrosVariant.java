package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum DavrosVariant {

    BUTTON_DAVROS(new NamespacedKey(TARDIS.plugin, "genetic/davros")),
    DAVROS_HEAD(new NamespacedKey(TARDIS.plugin, "monster/davros/davros_head")),
    DAVROS_DISGUISE(new NamespacedKey(TARDIS.plugin, "monster/davros/davros_disguise")),
    DAVROS(new NamespacedKey(TARDIS.plugin, "monster/davros/davros"));

    private final NamespacedKey key;

    DavrosVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
