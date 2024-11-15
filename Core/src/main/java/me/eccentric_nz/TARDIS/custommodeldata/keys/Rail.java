package me.eccentric_nz.TARDIS.custommodeldata.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum Rail {

    DIRECTION_NORTH(new NamespacedKey(TARDIS.plugin, "block/controls/direction_north")),
    DIRECTION_NORTH_EAST(new NamespacedKey(TARDIS.plugin, "block/controls/direction_north_east")),
    DIRECTION_EAST(new NamespacedKey(TARDIS.plugin, "block/controls/direction_east")),
    DIRECTION_SOUTH_EAST(new NamespacedKey(TARDIS.plugin, "block/controls/direction_south_east")),
    DIRECTION_SOUTH(new NamespacedKey(TARDIS.plugin, "block/controls/direction_south")),
    DIRECTION_SOUTH_WEST(new NamespacedKey(TARDIS.plugin, "block/controls/direction_south_west")),
    DIRECTION_WEST(new NamespacedKey(TARDIS.plugin, "block/controls/direction_west")),
    DIRECTION_NORTH_WEST(new NamespacedKey(TARDIS.plugin, "block/controls/direction_north_west"));

    private final NamespacedKey key;

    Rail(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
