package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum Gravel {

    DESTROY_0(new NamespacedKey(TARDIS.plugin, "block/destroy/destroy_0")),
    DESTROY_1(new NamespacedKey(TARDIS.plugin, "block/destroy/destroy_1")),
    DESTROY_2(new NamespacedKey(TARDIS.plugin, "block/destroy/destroy_2")),
    DESTROY_3(new NamespacedKey(TARDIS.plugin, "block/destroy/destroy_3")),
    DESTROY_4(new NamespacedKey(TARDIS.plugin, "block/destroy/destroy_4")),
    DESTROY_5(new NamespacedKey(TARDIS.plugin, "block/destroy/destroy_5")),
    DESTROY_6(new NamespacedKey(TARDIS.plugin, "block/destroy/destroy_6")),
    DESTROY_7(new NamespacedKey(TARDIS.plugin, "block/destroy/destroy_7")),
    DESTROY_8(new NamespacedKey(TARDIS.plugin, "block/destroy/destroy_8")),
    DESTROY_9(new NamespacedKey(TARDIS.plugin, "block/destroy/destroy_9"));

    private final NamespacedKey key;

    Gravel(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
