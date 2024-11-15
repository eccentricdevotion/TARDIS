package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum WaxedOxidizedCutCopper {

    BONE(new NamespacedKey(TARDIS.plugin, "block/seed/bone"));

    private final NamespacedKey key;

    WaxedOxidizedCutCopper(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}

