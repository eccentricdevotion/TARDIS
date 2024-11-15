package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum MusicDiscChirp {

    SAVE_DISK(new NamespacedKey(TARDIS.plugin, "item/disks/save_disk"));

    private final NamespacedKey key;

    MusicDiscChirp(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
