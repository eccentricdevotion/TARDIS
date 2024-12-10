package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum BlockBreak {

    DESTROY_0(new NamespacedKey(TARDIS.plugin, "destroy_0")),
    DESTROY_1(new NamespacedKey(TARDIS.plugin, "destroy_1")),
    DESTROY_2(new NamespacedKey(TARDIS.plugin, "destroy_2")),
    DESTROY_3(new NamespacedKey(TARDIS.plugin, "destroy_3")),
    DESTROY_4(new NamespacedKey(TARDIS.plugin, "destroy_4")),
    DESTROY_5(new NamespacedKey(TARDIS.plugin, "destroy_5")),
    DESTROY_6(new NamespacedKey(TARDIS.plugin, "destroy_6")),
    DESTROY_7(new NamespacedKey(TARDIS.plugin, "destroy_7")),
    DESTROY_8(new NamespacedKey(TARDIS.plugin, "destroy_8")),
    DESTROY_9(new NamespacedKey(TARDIS.plugin, "destroy_9"));

    private final NamespacedKey key;

    BlockBreak(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
