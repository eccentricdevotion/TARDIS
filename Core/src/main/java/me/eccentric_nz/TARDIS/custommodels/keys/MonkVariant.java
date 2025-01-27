package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum MonkVariant {

    BUTTON_HEADLESS_MONK(new NamespacedKey(TARDIS.plugin, "button_headless_monk")),
    HEADLESS_MONK_HEAD(new NamespacedKey(TARDIS.plugin, "headless_monk_head")),
    HEADLESS_MONK_STATIC(new NamespacedKey(TARDIS.plugin, "headless_monk_static")),
    HEADLESS_MONK_ALTERNATE(new NamespacedKey(TARDIS.plugin, "headless_monk_alternate")),
    HEADLESS_MONK_SWORD(new NamespacedKey(TARDIS.plugin, "headless_monk_sword"));

    private final NamespacedKey key;

    MonkVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}

