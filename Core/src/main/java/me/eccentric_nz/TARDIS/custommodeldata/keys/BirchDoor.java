package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum BirchDoor {

    BONE_DOOR(new NamespacedKey(TARDIS.plugin, "block/tardis/bone_door")),
    BONE_DOOR_OPEN_0(new NamespacedKey(TARDIS.plugin, "block/tardis/bone_door_open_0")),
    BONE_DOOR_OPEN_1(new NamespacedKey(TARDIS.plugin, "block/tardis/bone_door_open_1")),
    BONE_DOOR_OPEN_2(new NamespacedKey(TARDIS.plugin, "block/tardis/bone_door_open_2")),
    BONE_DOOR_OPEN_3(new NamespacedKey(TARDIS.plugin, "block/tardis/bone_door_open_3")),
    BONE_DOOR_OPEN_4(new NamespacedKey(TARDIS.plugin, "block/tardis/bone_door_open_4")),
    BONE_DOOR_OPEN_5(new NamespacedKey(TARDIS.plugin, "block/tardis/bone_door_open_5"));

    private final NamespacedKey key;

    BirchDoor(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
