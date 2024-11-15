package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum FireworkRocket {

    EXCITE(new NamespacedKey(TARDIS.plugin, "item/gui/excite"));

    private final NamespacedKey key;

    FireworkRocket(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
