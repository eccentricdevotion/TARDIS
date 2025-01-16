package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum Food {

    FISH_FINGER(new NamespacedKey(TARDIS.plugin, "fish_finger")),
    JAMMY_DODGER(new NamespacedKey(TARDIS.plugin, "jammy_dodger")),
    CUSTARD_CREAM(new NamespacedKey(TARDIS.plugin, "custard_cream")),
    PAPER_BAG(new NamespacedKey(TARDIS.plugin, "paper_bag")),
    BOWL_OF_CUSTARD(new NamespacedKey(TARDIS.plugin, "bowl_of_custard"));

    private final NamespacedKey key;

    Food(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
