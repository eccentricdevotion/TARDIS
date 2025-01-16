package me.eccentric_nz.TARDIS.sonic.actions;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

public class SonicConverterRunnable implements Runnable {

    private final Block block;
    private final Material from;
    private final Material to;
    private int taskId;
    private int count = 0;
    public SonicConverterRunnable(Block block, Material from, Material to) {
        this.block = block;
        this.from = from;
        this.to = to;
    }

    @Override
    public void run() {
        Material which = (block.getType() == from) ? to : from;
        block.setType(which);
        count++;
        if (count > 4) {
            block.setType(Material.AIR);
            block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(to, 1));
            Bukkit.getServer().getScheduler().cancelTask(taskId);
        }
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

}
