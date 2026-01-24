package me.eccentric_nz.TARDIS.rooms.games;

import me.eccentric_nz.TARDIS.rooms.games.pong.Border;
import org.bukkit.Location;

public class GameUtils {

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

    /**
     * Calculates the reflected angle in radians.
     *
     * @param incomingAngle angle in radians (0 to 2*PI)
     * @param border        the border hit
     * @return Rreflected angle in radians
     */
    public static double getReflectedAngle(double incomingAngle, Border border) {
        // Normalize angle to 0 - 2*PI
        double angle = incomingAngle % (2 * Math.PI);
        if (angle < 0) angle += 2 * Math.PI;
        return switch (border) {
            // reflect horizontally: angle becomes PI - angle
            case TOP, BOTTOM -> (Math.PI - angle) % (2 * Math.PI);
            // reflect vertically: angle becomes -angle
            case LEFT, RIGHT -> (2 * Math.PI - angle) % (2 * Math.PI);
        };
    }
}
