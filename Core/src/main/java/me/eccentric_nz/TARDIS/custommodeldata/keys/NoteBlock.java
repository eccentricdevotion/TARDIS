package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum NoteBlock {

    DISK_STORAGE(new NamespacedKey(TARDIS.plugin, "block/tardis/disk_storage"));

    private final NamespacedKey key;

    NoteBlock(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}

