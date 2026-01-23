package me.eccentric_nz.TARDIS.rooms.games;

import org.bukkit.Location;

public class GameUtils {

    public static Location centre(Location location) {
        location.setX(location.getBlockX() + 0.5d);
        location.setZ(location.getBlockZ() + 0.5d);
        return location;
    }
}
