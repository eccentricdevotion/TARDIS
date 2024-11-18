package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum SoulLantern {

    EXTERIOR(new NamespacedKey(TARDIS.plugin, "gui/lights/exterior"));

    private final NamespacedKey key;

    SoulLantern(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}

