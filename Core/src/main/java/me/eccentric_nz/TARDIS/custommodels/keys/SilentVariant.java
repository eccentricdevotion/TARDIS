package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum SilentVariant {

    BUTTON_SILENT(new NamespacedKey(TARDIS.plugin, "button_silent")),
    SILENT(new NamespacedKey(TARDIS.plugin, "silent")),
    SILENT_MOUTH_CLOSED(new NamespacedKey(TARDIS.plugin, "silent_mouth_closed")),
    SILENT_MOUTH_OPEN(new NamespacedKey(TARDIS.plugin, "silent_mouth_open")),
    SILENT_BEAMING(new NamespacedKey(TARDIS.plugin, "silent_beaming")),
    SILENT_STATIC(new NamespacedKey(TARDIS.plugin, "silent_static")),
    SILENCE_HAND(new NamespacedKey(TARDIS.plugin, "silent_hand")),
    SILENCE_OFFHAND(new NamespacedKey(TARDIS.plugin, "silent_offhand"));

    private final NamespacedKey key;

    SilentVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}

