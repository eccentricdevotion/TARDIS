package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum MusicDiscCat {

    BIOME_DISK(new NamespacedKey(TARDIS.plugin, "item/disks/biome_disk"));

    private final NamespacedKey key;

    MusicDiscCat(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
