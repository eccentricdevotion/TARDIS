package me.eccentric_nz.TARDIS.move;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.utility.TARDISSounds;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.UUID;

public class TARDISCustomModelDataChanger {

    private final TARDIS plugin;
    private final Block block;
    private final Player player;
    private final int id;

    public TARDISCustomModelDataChanger(TARDIS plugin, Block block, Player player, int id) {
        this.plugin = plugin;
        this.block = block;
        this.player = player;
        this.id = id;
    }

    /**
     * Toggle the door open and closed by setting the custom model data.
     */
    public void toggleOuterDoor() {
        UUID uuid = player.getUniqueId();
        HashMap<String, Object> wherecl = new HashMap<>();
        wherecl.put("tardis_id", id);
        ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherecl);
        if (!rsc.resultSet()) {
            return;
        }
        Location outer = new Location(rsc.getWorld(), rsc.getX(), rsc.getY(), rsc.getZ());
        while (!outer.getChunk().isLoaded()) {
            outer.getChunk().load();
        }
        ItemFrame itemFrame = null;
        for (Entity e : outer.getWorld().getNearbyEntities(outer, 1.0d, 1.0d, 1.0d)) {
            if (e instanceof ItemFrame) {
                itemFrame = (ItemFrame) e;
                break;
            }
        }
        if (itemFrame != null) {
            ItemStack is = itemFrame.getItem();
            if (TARDISConstants.DYES.contains(is.getType()) && is.hasItemMeta()) {
                ItemMeta im = is.getItemMeta();
                if (im.hasCustomModelData()) {
                    int cmd = im.getCustomModelData();
                    if (cmd == 1001 || cmd == 1002) {
                        boolean open = (cmd == 1001);
                        int newData;
                        if (open) {
                            new TARDISInnerDoorOpener(plugin, uuid, id).openDoor();
                            newData = 1002;
                        } else {
                            new TARDISInnerDoorCloser(plugin, uuid, id).closeDoor();
                            newData = 1001;
                        }
                        playDoorSound(open, block.getLocation());
                        im.setCustomModelData(newData);
                        is.setItemMeta(im);
                        itemFrame.setItem(is, false);
                    }
                }
            }
        }
    }

    /**
     * Plays a door sound when a police box door is clicked.
     *
     * @param open which sound to play, open (true), close (false)
     * @param l    a location to play the sound at
     */
    private void playDoorSound(boolean open, Location l) {
        if (open) {
            TARDISSounds.playTARDISSound(l, "tardis_door_open");
        } else {
            TARDISSounds.playTARDISSound(l, "tardis_door_close");
        }
    }
}
