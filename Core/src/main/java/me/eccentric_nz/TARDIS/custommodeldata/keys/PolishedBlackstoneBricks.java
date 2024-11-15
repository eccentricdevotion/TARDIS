package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum PolishedBlackstoneBricks {


    OBSERVATORY(new NamespacedKey(TARDIS.plugin, "item/gui/room/observatory"));

    private final NamespacedKey key;

    PolishedBlackstoneBricks(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
