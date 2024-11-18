package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum Bow {

    DALEK_BOW(new NamespacedKey(TARDIS.plugin, "monster/dalek/dalek_bow")),
    SILURIAN_BOW(new NamespacedKey(TARDIS.plugin, "monster/silurian/silurian_bow")),
    SILURIAN_GUN_ARM(new NamespacedKey(TARDIS.plugin, "monster/silurian/silurian_gun_arm"));

    private final NamespacedKey key;

    Bow(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
