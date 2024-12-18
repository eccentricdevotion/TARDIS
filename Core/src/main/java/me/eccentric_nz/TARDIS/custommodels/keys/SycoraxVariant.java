package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum SycoraxVariant {

    BUTTON_SYCORAX(new NamespacedKey(TARDIS.plugin, "button_sycorax")),
    SYCORAX_ARM(new NamespacedKey(TARDIS.plugin, "sycorax_arm")),
    SYCORAX_HEAD(new NamespacedKey(TARDIS.plugin, "sycorax_head")),
    SYCORAX_DISGUISE(new NamespacedKey(TARDIS.plugin, "sycorax_disguise")),
    SYCORAX_0(new NamespacedKey(TARDIS.plugin, "sycorax_0")),
    SYCORAX_1(new NamespacedKey(TARDIS.plugin, "sycorax_1")),
    SYCORAX_2(new NamespacedKey(TARDIS.plugin, "sycorax_2")),
    SYCORAX_3(new NamespacedKey(TARDIS.plugin, "sycorax_3")),
    SYCORAX_4(new NamespacedKey(TARDIS.plugin, "sycorax_4")),
    SYCORAX_STATIC(new NamespacedKey(TARDIS.plugin, "sycorax_static")),
    SYCORAX_ATTACKING_0(new NamespacedKey(TARDIS.plugin, "sycorax_attacking_0")),
    SYCORAX_ATTACKING_1(new NamespacedKey(TARDIS.plugin, "sycorax_attacking_1")),
    SYCORAX_ATTACKING_2(new NamespacedKey(TARDIS.plugin, "sycorax_attacking_2")),
    SYCORAX_ATTACKING_3(new NamespacedKey(TARDIS.plugin, "sycorax_attacking_3")),
    SYCORAX_ATTACKING_4(new NamespacedKey(TARDIS.plugin, "sycorax_attacking_4"));

    private final NamespacedKey key;

    SycoraxVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
