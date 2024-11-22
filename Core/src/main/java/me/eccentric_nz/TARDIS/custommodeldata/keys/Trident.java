package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum Trident {

    DALEK_BOW(new NamespacedKey(TARDIS.plugin, "monster/dalek/dalek_bow"));

    private final NamespacedKey key;

    Trident(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
