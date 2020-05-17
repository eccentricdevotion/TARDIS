package me.eccentric_nz.TARDIS.move;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodeldata.TARDISMushroomBlockData;
import me.eccentric_nz.TARDIS.database.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.utility.TARDISSounds;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class TARDISModelChanger {

    private final TARDIS plugin;
    private final Block block;
    private final Player player;
    private final int id;
    private final int doortype;

    public TARDISModelChanger(TARDIS plugin, Block block, Player player, int id, int doortype) {
        this.plugin = plugin;
        this.block = block.getRelative(BlockFace.UP);
        this.player = player;
        this.id = id;
        this.doortype = doortype;
    }

    /**
     * Toggle the door open and closed by swapping the mushroom block.
     */
    public void toggleDoors() {
        UUID uuid = player.getUniqueId();
        HashMap<String, Object> wherecl = new HashMap<>();
        wherecl.put("tardis_id", id);
        ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherecl);
        if (!rsc.resultSet()) {
            return;
        }
        COMPASS direction = rsc.getDirection();
        String data;
        Material material;
        Block mushroom = null;
        if (doortype == 0) {
            material = block.getType();
        } else {
            // get exterior block
            mushroom = new Location(rsc.getWorld(), rsc.getX(), rsc.getY() + 1, rsc.getZ()).getBlock();
            material = mushroom.getType();
        }
        if (material.equals(Material.MUSHROOM_STEM)) {
            new TARDISInnerDoorCloser(plugin, uuid, id).closeDoor();
            switch (direction) {
                case EAST:
                    data = TARDISMushroomBlockData.BROWN_MUSHROOM_DATA.get(3);
                    break;
                case SOUTH:
                    data = TARDISMushroomBlockData.BROWN_MUSHROOM_DATA.get(19);
                    break;
                case WEST:
                    data = TARDISMushroomBlockData.BROWN_MUSHROOM_DATA.get(35);
                    break;
                default:
                    data = TARDISMushroomBlockData.BROWN_MUSHROOM_DATA.get(51);
                    break;
            }
        } else {
            new TARDISInnerDoorOpener(plugin, uuid, id).openDoor();
            switch (direction) {
                case EAST:
                    data = TARDISMushroomBlockData.MUSHROOM_STEM_DATA.get(56);
                    break;
                case SOUTH:
                    data = TARDISMushroomBlockData.MUSHROOM_STEM_DATA.get(57);
                    break;
                case WEST:
                    data = TARDISMushroomBlockData.MUSHROOM_STEM_DATA.get(58);
                    break;
                default:
                    data = TARDISMushroomBlockData.MUSHROOM_STEM_DATA.get(59);
                    break;
            }
        }
        BlockData blockData = plugin.getServer().createBlockData(data);
        if (doortype == 0) {
            block.setBlockData(blockData);
        } else if (mushroom != null) {
            mushroom.setBlockData(blockData);
        }
        playDoorSound(material.equals(Material.MUSHROOM_STEM), block.getLocation());
    }

    /**
     * Plays a door sound when the blue police box oak trapdoor is clicked.
     *
     * @param open which sound to play, open (true), close (false)
     * @param l    a location to play the sound at
     */
    private void playDoorSound(boolean open, Location l) {
        if (open) {
            TARDISSounds.playTARDISSound(l, "tardis_door_close");
        } else {
            TARDISSounds.playTARDISSound(l, "tardis_door_open");
        }
    }
}
