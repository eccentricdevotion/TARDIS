package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum FlowerPot {

    SONIC_DOCK_OFF(new NamespacedKey(TARDIS.plugin, "sonic/sonic_dock_off")),
    SONIC_DOCK_ON(new NamespacedKey(TARDIS.plugin, "sonic/sonic_dock_on")),
    SONIC_DOCK(new NamespacedKey(TARDIS.plugin, "block/controls/sonic_dock")),
    SONIC_DOCK_CHARGING(new NamespacedKey(TARDIS.plugin, "block/controls/sonic_dock_charging")),
    SONIC_GENERATOR(new NamespacedKey(TARDIS.plugin, "sonic/sonic_generator"));

    private final NamespacedKey key;

    FlowerPot(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}

