package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum MagmaBlock {

    SPHERE_0(new NamespacedKey(TARDIS.plugin, "block/sphere_0")),
    SPHERE_1(new NamespacedKey(TARDIS.plugin, "block/sphere_1")),
    SPHERE_2(new NamespacedKey(TARDIS.plugin, "block/sphere_2")),
    SPHERE_3(new NamespacedKey(TARDIS.plugin, "block/sphere_3")),
    SPHERE_4(new NamespacedKey(TARDIS.plugin, "block/sphere_4"));

    private final NamespacedKey key;

    MagmaBlock(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
