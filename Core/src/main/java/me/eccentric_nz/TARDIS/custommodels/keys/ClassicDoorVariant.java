package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum ClassicDoorVariant {

    CLASSIC_DOOR(new NamespacedKey(TARDIS.plugin, "tardis/classic_door")),
    CLASSIC_DOOR_OPEN_0(new NamespacedKey(TARDIS.plugin, "tardis/classic_door_open_0")),
    CLASSIC_DOOR_OPEN_1(new NamespacedKey(TARDIS.plugin, "tardis/classic_door_open_1")),
    CLASSIC_DOOR_OPEN_2(new NamespacedKey(TARDIS.plugin, "tardis/classic_door_open_2")),
    CLASSIC_DOOR_OPEN_3(new NamespacedKey(TARDIS.plugin, "tardis/classic_door_open_3")),
    CLASSIC_DOOR_OPEN_4(new NamespacedKey(TARDIS.plugin, "tardis/classic_door_open_4")),
    CLASSIC_DOOR_OPEN_5(new NamespacedKey(TARDIS.plugin, "tardis/classic_door_open_5"));

    private final NamespacedKey key;

    ClassicDoorVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
