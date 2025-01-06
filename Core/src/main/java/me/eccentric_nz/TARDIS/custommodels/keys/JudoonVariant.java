package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum JudoonVariant {

    BUTTON_JUDOON(new NamespacedKey(TARDIS.plugin, "button_judoon")),
    JUDOON_HEAD(new NamespacedKey(TARDIS.plugin, "judoon_head")),
    JUDOON_STATIC(new NamespacedKey(TARDIS.plugin, "judoon_static")),
    JUDOON_GUARD(new NamespacedKey(TARDIS.plugin, "judoon_guard")),
    JUDOON_WEAPON_ACTIVE(new NamespacedKey(TARDIS.plugin, "judoon_weapon")),
    JUDOON_WEAPON_RESTING(new NamespacedKey(TARDIS.plugin, "judoon_weapon_resting"));

    private final NamespacedKey key;

    JudoonVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
