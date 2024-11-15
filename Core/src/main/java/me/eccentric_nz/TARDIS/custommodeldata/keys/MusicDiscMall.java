package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum MusicDiscMall {

    PRESET_DISK(new NamespacedKey(TARDIS.plugin, "item/disks/preset_disk"));

    private final NamespacedKey key;

    MusicDiscMall(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
