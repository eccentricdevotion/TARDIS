package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum Cookie {

    JAMMY_DODGER(new NamespacedKey(TARDIS.plugin, "item/food/jammy_dodger")),
    CUSTARD_CREAM(new NamespacedKey(TARDIS.plugin, "item/food/custard_cream"));

    private final NamespacedKey key;

    Cookie(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
