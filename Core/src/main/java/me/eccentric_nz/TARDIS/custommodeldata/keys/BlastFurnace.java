package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum BlastFurnace {

    CHEMISTRY(new NamespacedKey(TARDIS.plugin, "item/gui/room/chemistry"));

    private final NamespacedKey key;

    BlastFurnace(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
