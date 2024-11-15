package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum Furnace {

    LAZARUS(new NamespacedKey(TARDIS.plugin, "item/gui/room/lazarus")),
    ARTRON_FURNACE(new NamespacedKey(TARDIS.plugin, "item/tardis/artron_furnace")),
    ARTRON_FURNACE_LIT(new NamespacedKey(TARDIS.plugin, "item/tardis/artron_furnace_lit"));

    private final NamespacedKey key;

    Furnace(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
