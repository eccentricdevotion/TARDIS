package me.eccentric_nz.TARDIS.doors;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.utility.TARDISSounds;
import org.bukkit.Location;
import org.bukkit.Material;
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
        ItemStack is = display.getItemStack();
        Material material = is.getType();
        DoorAnimationData data = Door.getOpenData(material);
        if (material == Material.IRON_DOOR && !plugin.getConfig().getBoolean("police_box.animated_door")) {
            open = data.getLastFrame() + 1;
        }
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
        Material material = is.getType();
        DoorAnimationData data = Door.getCloseData(material);
        if (material == Material.IRON_DOOR && !plugin.getConfig().getBoolean("police_box.animated_door")) {
            closed = 0;
        } else {
            closed = data.getLastFrame();
        }
        TARDISSounds.playTARDISSound(location, data.getSound());
        taskID = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            ItemMeta im = is.getItemMeta();
            im.setCustomModelData(10000 + closed);
            is.setItemMeta(im);
            display.setItemStack(is);
            if (closed < 1) {
                plugin.getServer().getScheduler().cancelTask(taskID);
                taskID = 0;
            }
            closed--;
        }, 2L, data.getTicks());
    }
}
