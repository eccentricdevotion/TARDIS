package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum MusicDiscWait {

    PLAYER_DISK(new NamespacedKey(TARDIS.plugin, "item/disks/player_disk"));

    private final NamespacedKey key;

    MusicDiscWait(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
