package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum PoliceBoxDoorVariant {

    POLICE_BOX_DOOR_0(new NamespacedKey(TARDIS.plugin, "tardis/police_box_door_0")),
    POLICE_BOX_DOOR_1(new NamespacedKey(TARDIS.plugin, "tardis/police_box_door_1")),
    POLICE_BOX_DOOR_2(new NamespacedKey(TARDIS.plugin, "tardis/police_box_door_2")),
    POLICE_BOX_DOOR_3(new NamespacedKey(TARDIS.plugin, "tardis/police_box_door_3")),
    POLICE_BOX_DOOR_4(new NamespacedKey(TARDIS.plugin, "tardis/police_box_door_4")),
    POLICE_BOX_DOOR_BOTH(new NamespacedKey(TARDIS.plugin, "tardis/police_box_door_both"));

    private final NamespacedKey key;

    PoliceBoxDoorVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
