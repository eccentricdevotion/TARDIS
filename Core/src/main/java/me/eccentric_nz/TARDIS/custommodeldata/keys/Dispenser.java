package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum Dispenser {

    VAULT(new NamespacedKey(TARDIS.plugin, "item/gui/room/vault"));

    private final NamespacedKey key;

    Dispenser(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
