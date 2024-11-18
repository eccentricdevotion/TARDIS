package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum GoldIngot {

    SPEED(new NamespacedKey(TARDIS.plugin, "gui/particle/speed")),
    ELIXIR_OF_LIFE(new NamespacedKey(TARDIS.plugin, "regeneration/elixir_of_life"));

    private final NamespacedKey key;

    GoldIngot(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
