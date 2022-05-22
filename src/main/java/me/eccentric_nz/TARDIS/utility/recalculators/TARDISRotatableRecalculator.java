package me.eccentric_nz.TARDIS.utility.recalculators;

import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Rotatable;

public class TARDISRotatableRecalculator {


    public BlockData recalculate(BlockData b, COMPASS d) {
        switch (d) {
            case WEST -> {
                // rotate 180°
                return oneEighty(b);
            }
            case NORTH -> {
                // rotate anti-clockwise
                return antiClockwise(b);
            }
            case SOUTH -> {
                // rotate clockwise
                return clockwise(b);
            }
            default -> {
                // do nothing
                return b;
            }
        }
    }

    private BlockData clockwise(BlockData b) {
        Rotatable rail = (Rotatable) b;
        BlockFace face;
        switch (rail.getRotation()) {
            case NORTH -> face = BlockFace.EAST;
            case NORTH_NORTH_EAST -> face = BlockFace.EAST_SOUTH_EAST;
            case NORTH_EAST -> face = BlockFace.SOUTH_EAST;
            case EAST_NORTH_EAST -> face = BlockFace.SOUTH_SOUTH_EAST;
            case EAST -> face = BlockFace.SOUTH;
            case EAST_SOUTH_EAST -> face = BlockFace.SOUTH_SOUTH_WEST;
            case SOUTH_EAST -> face = BlockFace.SOUTH_WEST;
            case SOUTH_SOUTH_EAST -> face = BlockFace.WEST_SOUTH_WEST;
            case SOUTH -> face = BlockFace.WEST;
            case SOUTH_SOUTH_WEST -> face = BlockFace.WEST_NORTH_WEST;
            case SOUTH_WEST -> face = BlockFace.NORTH_WEST;
            case WEST_SOUTH_WEST -> face = BlockFace.NORTH_NORTH_WEST;
            case WEST -> face = BlockFace.NORTH;
            case WEST_NORTH_WEST -> face = BlockFace.NORTH_NORTH_EAST;
            case NORTH_WEST -> face = BlockFace.NORTH_EAST;
            case NORTH_NORTH_WEST -> face = BlockFace.EAST_NORTH_EAST;
            // UP/DOWN
            default -> face = rail.getRotation();
        }
        rail.setRotation(face);
        return rail;
    }

    private BlockData antiClockwise(BlockData b) {
        Rotatable rail = (Rotatable) b;
        BlockFace face;
        switch (rail.getRotation()) {
            case NORTH -> face = BlockFace.WEST;
            case NORTH_NORTH_EAST -> face = BlockFace.WEST_NORTH_WEST;
            case NORTH_EAST -> face = BlockFace.NORTH_WEST;
            case EAST_NORTH_EAST -> face = BlockFace.NORTH_NORTH_WEST;
            case EAST -> face = BlockFace.NORTH;
            case EAST_SOUTH_EAST -> face = BlockFace.NORTH_NORTH_EAST;
            case SOUTH_EAST -> face = BlockFace.NORTH_EAST;
            case SOUTH_SOUTH_EAST -> face = BlockFace.EAST_NORTH_EAST;
            case SOUTH -> face = BlockFace.EAST;
            case SOUTH_SOUTH_WEST -> face = BlockFace.EAST_SOUTH_EAST;
            case SOUTH_WEST -> face = BlockFace.SOUTH_EAST;
            case WEST_SOUTH_WEST -> face = BlockFace.SOUTH_SOUTH_EAST;
            case WEST -> face = BlockFace.SOUTH;
            case WEST_NORTH_WEST -> face = BlockFace.SOUTH_SOUTH_WEST;
            case NORTH_WEST -> face = BlockFace.SOUTH_WEST;
            case NORTH_NORTH_WEST -> face = BlockFace.WEST_SOUTH_WEST;
            // UP/DOWN
            default -> face = rail.getRotation();
        }
        rail.setRotation(face);
        return rail;
    }

    private BlockData oneEighty(BlockData b) {
        Rotatable rotatable = (Rotatable) b;
        BlockFace face = rotatable.getRotation();
        rotatable.setRotation(face.getOppositeFace());
        return rotatable;
    }
}
