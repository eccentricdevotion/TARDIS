package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum RedConcrete {


    SURGERY(new NamespacedKey(TARDIS.plugin, "item/gui/room/surgery")),
    CONSOLE_RED(new NamespacedKey(TARDIS.plugin, "item/tardis/console_red")),

    HEAT_BLOCK(new NamespacedKey(TARDIS.plugin, "block/chemistry/heat_block"));

    private final NamespacedKey key;

    RedConcrete(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
