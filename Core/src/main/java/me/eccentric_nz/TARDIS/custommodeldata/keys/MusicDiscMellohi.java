package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum MusicDiscMellohi {

    BLUEPRINT_DISK(new NamespacedKey(TARDIS.plugin, "item/disks/blueprint_disk"));

    private final NamespacedKey key;

    MusicDiscMellohi(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
