package me.eccentric_nz.TARDIS.customblocks;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.utility.TARDISSounds;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ClassicDoorAnimator {

    private final TARDIS plugin;
    private final ItemDisplay display;

    private int taskID;
    private int open = 5;
    private int closed = 9;

    public ClassicDoorAnimator(TARDIS plugin, ItemDisplay display) {
        this.plugin = plugin;
        this.display = display;
    }

    public void open() {
        TARDISSounds.playTARDISSound(display.getLocation(), "classic_door");
        ItemStack is = display.getItemStack();
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
        }, 2L, 4L);
    }

    public void close() {
        TARDISSounds.playTARDISSound(display.getLocation(), "classic_door");
        ItemStack is = display.getItemStack();
        taskID = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            ItemMeta im = is.getItemMeta();
            im.setCustomModelData(10000 + closed);
            is.setItemMeta(im);
            display.setItemStack(is);
            if (closed < 5) {
                plugin.getServer().getScheduler().cancelTask(taskID);
                taskID = 0;
            }
            closed--;
        }, 2L, 4L);
    }
}
