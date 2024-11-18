package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum MuddyMangroveRoots {

    MANGROVE(new NamespacedKey(TARDIS.plugin, "gui/room/mangrove"));

    private final NamespacedKey key;

    MuddyMangroveRoots(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
