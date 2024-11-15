package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum CrimsonButton {

    BUTTON_DAVROS(new NamespacedKey(TARDIS.plugin, "item/genetic/davros")),
    DAVROS_HEAD(new NamespacedKey(TARDIS.plugin, "item/monster/davros/davros_head")),
    DAVROS_DISGUISE(new NamespacedKey(TARDIS.plugin, "item/monster/davros/davros_disguise")),
    DAVROS(new NamespacedKey(TARDIS.plugin, "item/monster/davros/davros"));

    private final NamespacedKey key;

    CrimsonButton(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
