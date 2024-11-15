package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum MusicDiscWard {

    HANDLES_DISK(new NamespacedKey(TARDIS.plugin, "item/disks/handles_disk"));

    private final NamespacedKey key;

    MusicDiscWard(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
