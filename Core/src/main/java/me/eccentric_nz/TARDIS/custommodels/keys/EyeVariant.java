package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum EyeVariant {

    SPHERE_0(new NamespacedKey(TARDIS.plugin, "eye/sphere_0")),
    SPHERE_1(new NamespacedKey(TARDIS.plugin, "eye/sphere_1")),
    SPHERE_2(new NamespacedKey(TARDIS.plugin, "eye/sphere_2")),
    SPHERE_3(new NamespacedKey(TARDIS.plugin, "eye/sphere_3")),
    SPHERE_4(new NamespacedKey(TARDIS.plugin, "eye/sphere_4"));

    private final NamespacedKey key;

    EyeVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
