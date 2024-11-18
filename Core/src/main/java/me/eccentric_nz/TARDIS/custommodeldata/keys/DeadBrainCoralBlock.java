package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum DeadBrainCoralBlock {

    SHELL(new NamespacedKey(TARDIS.plugin, "gui/room/shell"));

    private final NamespacedKey key;

    DeadBrainCoralBlock(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
