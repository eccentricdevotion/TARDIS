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

    private static final double HALF_PI = Math.PI * 0.5;
    private static final double ONEDOT5_PI = Math.PI * 1.5;
    private static final double TWO_PI = Math.PI * 2;

    /**
     * Calculates the reflected angle in radians.
     *
     * @param angle  angle in radians (0 to 2*PI)
     * @param border the border hit
     * @return Rreflected angle in radians
     */
    public static double getReflectedAngle(double angle, Border border) {
        switch (border) {
            case TOP -> {
                if (angle > 0 && angle < Math.PI / 2) {
                    return Math.PI - angle;
                }
                if (angle > ONEDOT5_PI && angle < TWO_PI) {
                    return (TWO_PI) - angle;
                }
            }
            case BOTTOM -> {
                if (angle > HALF_PI && angle < Math.PI) {
                    return Math.PI + (Math.PI - angle);
                }
                if (angle > 0 && angle < HALF_PI) {
                    return (TWO_PI) - angle;
                }
            }
            case LEFT -> {
                if (angle > 0 && angle < (HALF_PI)) {
                    return Math.PI - angle;
                }
                if (angle > (ONEDOT5_PI) && angle < (TWO_PI)) {
                    return Math.PI + ((TWO_PI) - angle);
                }
            }
            case RIGHT -> {
                if (angle > (Math.PI / 2) && angle < Math.PI) {
                    return (angle - Math.PI);
                }
                if (angle > Math.PI && angle < (ONEDOT5_PI)) {
                    return (TWO_PI) - (angle - Math.PI);
                }
            }
        }
        return angle;
    }
}
