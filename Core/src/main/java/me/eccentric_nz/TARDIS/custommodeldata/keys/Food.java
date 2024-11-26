package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum Food {

    FISH_FINGER(new NamespacedKey(TARDIS.plugin, "food/fish_finger")),
    JAMMY_DODGER(new NamespacedKey(TARDIS.plugin, "food/jammy_dodger")),
    CUSTARD_CREAM(new NamespacedKey(TARDIS.plugin, "food/custard_cream")),
    PAPER_BAG(new NamespacedKey(TARDIS.plugin, "food/paper_bag")),
    BOWL_OF_CUSTARD(new NamespacedKey(TARDIS.plugin, "food/bowl_of_custard"));

    private final NamespacedKey key;

    Food(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
