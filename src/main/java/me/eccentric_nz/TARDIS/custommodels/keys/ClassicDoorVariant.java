package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum ClassicDoorVariant {

    CLASSIC_DOOR_CLOSED(new NamespacedKey(TARDIS.plugin, "classic_door_closed")),
    CLASSIC_DOOR_0(new NamespacedKey(TARDIS.plugin, "classic_door_0")),
    CLASSIC_DOOR_1(new NamespacedKey(TARDIS.plugin, "classic_door_1")),
    CLASSIC_DOOR_2(new NamespacedKey(TARDIS.plugin, "classic_door_2")),
    CLASSIC_DOOR_3(new NamespacedKey(TARDIS.plugin, "classic_door_3")),
    CLASSIC_DOOR_4(new NamespacedKey(TARDIS.plugin, "classic_door_4")),
    CLASSIC_DOOR_OPEN(new NamespacedKey(TARDIS.plugin, "classic_door_open"));

    private final NamespacedKey key;

    ClassicDoorVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
