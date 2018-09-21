package me.eccentric_nz.TARDIS.utility.recalculators;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;

public class TARDISBedRecalculator {

    public BlockData recalculate(BlockData data, COMPASS d) {
        Directional bed = (Directional) data;
        switch (bed.getFacing()) {
            case WEST:
                bed.setFacing(BlockFace.valueOf(TARDIS.plugin.getPresetBuilder().getOppositeFace(d).toString()));
                break;
            case EAST:
                bed.setFacing(BlockFace.valueOf(d.toString()));
                break;
            case NORTH:
                // anticlockwise 90°
                bed.setFacing(rotate90Anticlockwise(d));
                break;
            default:
                // clockwise 90°
                bed.setFacing(rotate90Clockwise(d));
                break;
        }
        return bed;
    }

    private BlockFace rotate90Clockwise(COMPASS d) {
        switch (d) {
            case SOUTH:
                return BlockFace.WEST;
            case WEST:
                return BlockFace.NORTH;
            case NORTH:
                return BlockFace.EAST;
            default:
                return BlockFace.SOUTH;
        }
    }

    private BlockFace rotate90Anticlockwise(COMPASS d) {
        switch (d) {
            case SOUTH:
                return BlockFace.EAST;
            case WEST:
                return BlockFace.SOUTH;
            case NORTH:
                return BlockFace.WEST;
            default:
                return BlockFace.NORTH;
        }
    }
}
