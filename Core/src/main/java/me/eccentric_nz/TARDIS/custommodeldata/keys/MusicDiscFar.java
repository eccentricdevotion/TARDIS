package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum MusicDiscFar {

    CONTROL_DISK(new NamespacedKey(TARDIS.plugin, "item/disks/control_disk"));

    private final NamespacedKey key;

    MusicDiscFar(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
