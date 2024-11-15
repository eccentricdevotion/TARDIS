package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum TubeCoralBlock {

    AQUARIUM(new NamespacedKey(TARDIS.plugin, "item/gui/room/aquarium"));

    private final NamespacedKey key;

    TubeCoralBlock(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
