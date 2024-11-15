package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum LimeStainedGlass {


    TINT_LIME(new NamespacedKey(TARDIS.plugin, "block/lights/tint_lime")),

    COMPUTER_DISK(new NamespacedKey(TARDIS.plugin, "item/equipment/computer_disk"));

    private final NamespacedKey key;

    LimeStainedGlass(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
