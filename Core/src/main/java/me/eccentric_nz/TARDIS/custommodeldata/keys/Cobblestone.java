package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum Cobblestone {

    STANDARD_SONIC(new NamespacedKey(TARDIS.plugin, "gui/bowl/standard_sonic")),
    SMALL(new NamespacedKey(TARDIS.plugin, "block/seed/small")),
    MEDIUM(new NamespacedKey(TARDIS.plugin, "block/seed/medium")),
    TALL(new NamespacedKey(TARDIS.plugin, "block/seed/tall"));

    private final NamespacedKey key;

    Cobblestone(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}

