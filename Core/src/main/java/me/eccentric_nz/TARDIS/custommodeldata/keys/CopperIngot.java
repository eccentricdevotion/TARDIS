package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum CopperIngot {

    TEST(new NamespacedKey(TARDIS.plugin, "gui/particle/test"));

    private final NamespacedKey key;

    CopperIngot(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
