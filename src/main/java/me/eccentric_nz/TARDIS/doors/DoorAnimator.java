package me.eccentric_nz.TARDIS.doors;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.utility.TARDISSounds;
import org.bukkit.Location;
import org.bukkit.Material;
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
    private int open = 1;
    private int closed = 5;

    public DoorAnimator(TARDIS plugin, ItemDisplay display) {
        this.plugin = plugin;
        this.display = display;
    }

    public void open() {
        Location location = display.getLocation();
        // remove the barrier blocks
//        setBlocks(location, Material.AIR, display.getYaw());
        ItemStack is = display.getItemStack();
        DoorAnimationData data = Door.getOpenData(is.getType());
        TARDISSounds.playTARDISSound(location, data.getSound());
        taskID = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            ItemMeta im = is.getItemMeta();
            im.setCustomModelData(10000 + open);
            is.setItemMeta(im);
            display.setItemStack(is);
            if (open > data.getLastFrame()) {
                plugin.getServer().getScheduler().cancelTask(taskID);
                taskID = 0;
            }
            open++;
        }, 2L, data.getTicks());
    }

    public void close() {
        Location location = display.getLocation();
        ItemStack is = display.getItemStack();
        DoorAnimationData data = Door.getCloseData(is.getType());
        closed = data.getLastFrame();
        TARDISSounds.playTARDISSound(location, data.getSound());
        taskID = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            ItemMeta im = is.getItemMeta();
            im.setCustomModelData(10000 + closed);
            is.setItemMeta(im);
            display.setItemStack(is);
            if (closed < 1) {
                plugin.getServer().getScheduler().cancelTask(taskID);
                taskID = 0;
//                // set the barrier blocks to prevent entry
//                setBlocks(location, Material.BARRIER, display.getYaw());
            }
            closed--;
        }, 2L, data.getTicks());
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
