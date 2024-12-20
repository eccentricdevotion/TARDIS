package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum MireVariant {

    BUTTON_THE_MIRE(new NamespacedKey(TARDIS.plugin, "button_the_mire")),
    THE_MIRE_HEAD(new NamespacedKey(TARDIS.plugin, "the_mire_head")),
    THE_MIRE_DISGUISE(new NamespacedKey(TARDIS.plugin, "the_mire_disguise")),
    THE_MIRE_HELMETLESS(new NamespacedKey(TARDIS.plugin, "the_mire_helmetless")),
    MIRE_HELMET(new NamespacedKey(TARDIS.plugin, "mire_helmet")),
    MIRE_RIGHT_ARM(new NamespacedKey(TARDIS.plugin, "mire_right_arm")),
    MIRE_LEFT_ARM(new NamespacedKey(TARDIS.plugin, "mire_left_arm")),
    THE_MIRE_STATIC(new NamespacedKey(TARDIS.plugin, "the_mire_static"));

    private final NamespacedKey key;

    MireVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}

