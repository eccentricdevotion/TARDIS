package me.eccentric_nz.TARDIS.doors;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.utility.TARDISSounds;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class DoorAnimator {

    private final TARDIS plugin;
    private final ItemDisplay display;

    private int taskID;
    private int open = 5;
    private int closed = 9;

    public DoorAnimator(TARDIS plugin, ItemDisplay display) {
        this.plugin = plugin;
        this.display = display;
    }

    public void open() {
        // remove the barrier blocks
        Location location = display.getLocation();
        setBlocks(location, Material.AIR, display.getYaw());
        ItemStack is = display.getItemStack();
        String sound = "tardis_door_open";
        long delay = 4L;
        if (Tag.ITEMS_DECORATED_POT_SHERDS.isTagged(is.getType())) {
            DoorAnimationData data = Door.getOpenData(is.getType());
            if (data != null) {
                sound = data.getSound();
                delay = data.getTicks();
            }
        } else {
            sound = "classic_door";
        }
        TARDISSounds.playTARDISSound(location, sound);
        taskID = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            ItemMeta im = is.getItemMeta();
            im.setCustomModelData(10000 + open);
            is.setItemMeta(im);
            display.setItemStack(is);
            if (open > 9) {
                plugin.getServer().getScheduler().cancelTask(taskID);
                taskID = 0;
            }
            open++;
        }, 2L, delay);
    }

    public void close() {
        Location location = display.getLocation();
        ItemStack is = display.getItemStack();
        String sound = "tardis_door_close";
        long delay = 4L;
        if (Tag.ITEMS_DECORATED_POT_SHERDS.isTagged(is.getType())) {
            DoorAnimationData data = Door.getCloseData(is.getType());
            if (data != null) {
                sound = data.getSound();
                delay = data.getTicks();
            }
        } else {
            sound = "classic_door";
        }
        TARDISSounds.playTARDISSound(location, sound);
        taskID = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            ItemMeta im = is.getItemMeta();
            im.setCustomModelData(10000 + closed);
            is.setItemMeta(im);
            display.setItemStack(is);
            if (closed < 5) {
                plugin.getServer().getScheduler().cancelTask(taskID);
                taskID = 0;
                // set the barrier blocks to prevent entry
                setBlocks(location, Material.BARRIER, display.getYaw());
            }
            closed--;
        }, 2L, delay);
    }

    private void setBlocks(Location location, Material material, float yaw) {
        BlockFace face;
        if (yaw == 90) {
            face = BlockFace.SOUTH;
        } else if (yaw == -90) {
            face = BlockFace.NORTH;
        } else if (yaw == -180) {
            face = BlockFace.WEST;
        } else {
            face = BlockFace.EAST;
        }
        Block block = location.getBlock().getRelative(BlockFace.UP);
        Block barrier = block.getRelative(face);
        if (material == Material.END_ROD) {
            Directional left = (Directional) material.createBlockData();
            left.setFacing(BlockFace.EAST);
            Directional right = (Directional) material.createBlockData();
            right.setFacing(BlockFace.WEST);
            block.setBlockData(left);
            barrier.setBlockData(right);
        } else {
            block.setType(material);
            barrier.setType(material);
        }
    }
}
