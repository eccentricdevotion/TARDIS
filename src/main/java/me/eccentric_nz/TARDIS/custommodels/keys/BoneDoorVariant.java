package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum BoneDoorVariant {

    BONE_DOOR_CLOSED(new NamespacedKey(TARDIS.plugin, "bone_door_closed")),
    BONE_DOOR_0(new NamespacedKey(TARDIS.plugin, "bone_door_0")),
    BONE_DOOR_1(new NamespacedKey(TARDIS.plugin, "bone_door_1")),
    BONE_DOOR_2(new NamespacedKey(TARDIS.plugin, "bone_door_2")),
    BONE_DOOR_3(new NamespacedKey(TARDIS.plugin, "bone_door_3")),
    BONE_DOOR_4(new NamespacedKey(TARDIS.plugin, "bone_door_4")),
    BONE_DOOR_OPEN(new NamespacedKey(TARDIS.plugin, "bone_door_open"));

    private final NamespacedKey key;

    BoneDoorVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
