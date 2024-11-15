package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum YellowConcrete {

    CONSOLE_YELLOW(new NamespacedKey(TARDIS.plugin, "item/tardis/console_yellow")),
    LAB(new NamespacedKey(TARDIS.plugin, "block/chemistry/lab"));

    private final NamespacedKey key;

    YellowConcrete(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}

