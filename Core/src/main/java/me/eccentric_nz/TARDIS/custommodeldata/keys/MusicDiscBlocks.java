package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum MusicDiscBlocks {

    AREA_DISK(new NamespacedKey(TARDIS.plugin, "item/disks/area_disk"));

    private final NamespacedKey key;

    MusicDiscBlocks(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
