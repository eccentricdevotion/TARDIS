package me.eccentric_nz.TARDIS.builders;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.block.Block;

import java.util.List;

public class TARDISMushroomRunnable implements Runnable {

    private final TARDIS plugin;
    private final List<MushroomBlock> mushrooms;
    private int task;
    private int i = 0;

    public TARDISMushroomRunnable(TARDIS plugin, List<MushroomBlock> mushrooms) {
        this.plugin = plugin;
        this.mushrooms = mushrooms;
    }

    @Override
    public void run() {
        Block block = mushrooms.get(i).getBlock();
        block.setBlockData(mushrooms.get(i).getBlockData());
        i++;
        if (i == mushrooms.size()) {
            plugin.getServer().getScheduler().cancelTask(task);
            task = 0;
        }
    }

    public void setTask(int task) {
        this.task = task;
    }
}
