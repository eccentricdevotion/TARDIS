package me.eccentric_nz.TARDIS.rooms.games;

import org.bukkit.Location;

public class GameUtils {
    
    public static int[][] DIRECTIONS = {
            {1, 0}, {-1, 0},
            {1, 1}, {1, -1},
            {-1, 1}, {-1, -1}
    };

    /**
     * Centres a location on a block.
     *
     * @param location a player loaction
     * @return the location adjusted to the centre of the block
     */
    public static Location centre(Location location) {
        location.setX(location.getBlockX() + 0.5d);
        location.setZ(location.getBlockZ() + 0.5d);
        return location;
    }
}
