package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum DalekSecVariant {

    BUTTON_DALEK_SEC(new NamespacedKey(TARDIS.plugin, "button_dalek_sec")),
    DALEK_SEC_HEAD(new NamespacedKey(TARDIS.plugin, "dalek_sec_head")),
    DALEK_SEC_TENTACLES(new NamespacedKey(TARDIS.plugin, "dalek_sec_tentacles")),
    DALEK_SEC_STATIC(new NamespacedKey(TARDIS.plugin, "dalek_sec_static"));

    private final NamespacedKey key;

    DalekSecVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
