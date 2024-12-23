package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum OodVariant {

    BUTTON_OOD(new NamespacedKey(TARDIS.plugin, "button_ood")),
    OOD_HEAD(new NamespacedKey(TARDIS.plugin, "ood_head")),
    OOD_REDEYE_HEAD(new NamespacedKey(TARDIS.plugin, "ood_redeye_head")),
    OOD_BLACK_STATIC(new NamespacedKey(TARDIS.plugin, "ood_static_black")),
    OOD_BLUE_STATIC(new NamespacedKey(TARDIS.plugin, "ood_static_blue")),
    OOD_BROWN_STATIC(new NamespacedKey(TARDIS.plugin, "ood_static_brown")),
    OOD_REDEYE_BLACK_STATIC(new NamespacedKey(TARDIS.plugin, "ood_redeye_static_black")),
    OOD_REDEYE_BLUE_STATIC(new NamespacedKey(TARDIS.plugin, "ood_redeye_static_blue")),
    OOD_REDEYE_BROWN_STATIC(new NamespacedKey(TARDIS.plugin, "ood_redeye_static_brown"));

    private final NamespacedKey key;

    OodVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}

