package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum DroidVariant {

    BUTTON_CLOCKWORK_DROID(new NamespacedKey(TARDIS.plugin, "button_clockwork_droid")),
    CLOCKWORK_DROID_KEY(new NamespacedKey(TARDIS.plugin, "clockwork_droid_key")),
    CLOCKWORK_DROID_HEAD(new NamespacedKey(TARDIS.plugin, "clockwork_droid_head")),
    CLOCKWORK_DROID_STATIC(new NamespacedKey(TARDIS.plugin, "clockwork_droid_static")),
    CLOCKWORK_DROID_FEMALE_KEY(new NamespacedKey(TARDIS.plugin, "clockwork_droid_female_key")),
    CLOCKWORK_DROID_FEMALE_HEAD(new NamespacedKey(TARDIS.plugin, "clockwork_droid_female_head")),
    CLOCKWORK_DROID_FEMALE_STATIC(new NamespacedKey(TARDIS.plugin, "clockwork_droid_female_static"));

    private final NamespacedKey key;

    DroidVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
