package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum IronDoor {


    TARDIS_DOOR_0(new NamespacedKey(TARDIS.plugin, "block/tardis/tardis_door_0")),

    TARDIS_DOOR_1(new NamespacedKey(TARDIS.plugin, "block/tardis/tardis_door_1")),

    TARDIS_DOOR_2(new NamespacedKey(TARDIS.plugin, "block/tardis/tardis_door_2")),

    TARDIS_DOOR_3(new NamespacedKey(TARDIS.plugin, "block/tardis/tardis_door_3")),

    TARDIS_DOOR_4(new NamespacedKey(TARDIS.plugin, "block/tardis/tardis_door_4")),

    TARDIS_DOOR_BOTH_OPEN(new NamespacedKey(TARDIS.plugin, "block/tardis/tardis_door_both_open"));

    private final NamespacedKey key;

    IronDoor(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
