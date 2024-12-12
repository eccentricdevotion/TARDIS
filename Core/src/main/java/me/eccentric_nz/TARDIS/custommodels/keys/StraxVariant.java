package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum StraxVariant {

    STRAX(new NamespacedKey(TARDIS.plugin, "button_strax")),
    STRAX_ARM(new NamespacedKey(TARDIS.plugin, "strax_arm")),
    STRAX_HEAD(new NamespacedKey(TARDIS.plugin, "strax_head")),
    STRAX_DISGUISE(new NamespacedKey(TARDIS.plugin, "strax_disguise")),
    STRAX_0(new NamespacedKey(TARDIS.plugin, "strax_0")),
    STRAX_1(new NamespacedKey(TARDIS.plugin, "strax_1")),
    STRAX_2(new NamespacedKey(TARDIS.plugin, "strax_2")),
    STRAX_3(new NamespacedKey(TARDIS.plugin, "strax_3")),
    STRAX_4(new NamespacedKey(TARDIS.plugin, "strax_4")),
    STRAX_STATIC(new NamespacedKey(TARDIS.plugin, "strax_static")),
    STRAX_ATTACKING_0(new NamespacedKey(TARDIS.plugin, "strax_attacking_0")),
    STRAX_ATTACKING_1(new NamespacedKey(TARDIS.plugin, "strax_attacking_1")),
    STRAX_ATTACKING_2(new NamespacedKey(TARDIS.plugin, "strax_attacking_2")),
    STRAX_ATTACKING_3(new NamespacedKey(TARDIS.plugin, "strax_attacking_3")),
    STRAX_ATTACKING_4(new NamespacedKey(TARDIS.plugin, "strax_attacking_4"));

    private final NamespacedKey key;

    StraxVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}

