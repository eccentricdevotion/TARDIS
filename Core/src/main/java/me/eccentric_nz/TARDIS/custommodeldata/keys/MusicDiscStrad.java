package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum MusicDiscStrad {

    BLANK_DISK(new NamespacedKey(TARDIS.plugin, "item/disks/blank_disk"));

    private final NamespacedKey key;

    MusicDiscStrad(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
