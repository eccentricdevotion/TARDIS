package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum TardisDoorVariant {

    TARDIS_DOOR_CLOSED(new NamespacedKey(TARDIS.plugin, "doors/tardis_door_closed")),
    TARDIS_DOOR_0(new NamespacedKey(TARDIS.plugin, "doors/tardis_door_0")),
    TARDIS_DOOR_1(new NamespacedKey(TARDIS.plugin, "doors/tardis_door_1")),
    TARDIS_DOOR_2(new NamespacedKey(TARDIS.plugin, "doors/tardis_door_2")),
    TARDIS_DOOR_OPEN(new NamespacedKey(TARDIS.plugin, "doors/tardis_door_open")),
    TARDIS_DOOR_EXTRA(new NamespacedKey(TARDIS.plugin, "doors/tardis_door_extra"));

    private final NamespacedKey key;

    TardisDoorVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
