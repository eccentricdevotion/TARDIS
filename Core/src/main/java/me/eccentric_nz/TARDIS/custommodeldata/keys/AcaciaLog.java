package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum AcaciaLog {

    HUTCH(new NamespacedKey(TARDIS.plugin, "gui/room/hutch"));

    private final NamespacedKey key;

    AcaciaLog(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
