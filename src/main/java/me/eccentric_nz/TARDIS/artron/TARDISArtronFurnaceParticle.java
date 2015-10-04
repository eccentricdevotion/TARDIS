package me.eccentric_nz.TARDIS.artron;

import java.util.ArrayList;
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.utility.TARDISEffectLibHelper;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Furnace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class TARDISArtronFurnaceParticle {

    private final TARDIS plugin;
    private final List<Block> nullFurnaces = new ArrayList<Block>();
    private int taskID;
    private int errorCount = 0;

    public TARDISArtronFurnaceParticle(TARDIS plugin) {
        this.plugin = plugin;
    }

    @SuppressWarnings("deprecation")
    public void addParticles() {

        taskID = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                for (Block block : plugin.getGeneralKeeper().getArtronFurnaces()) {
                    try {
                        if (block != null && isArtronFurnace(block)) {
                            TARDISEffectLibHelper.sendWaterParticle(block.getLocation());
                        }
                    } catch (NullPointerException e) {
                        errorCount++;
                        nullFurnaces.add(block);
                        if (errorCount > 10) {
                            plugin.getServer().getScheduler().cancelTask(taskID);
                        }
                    }
                }
                plugin.getGeneralKeeper().getArtronFurnaces().removeAll(nullFurnaces);
                nullFurnaces.clear();
                for (Player player : plugin.getServer().getOnlinePlayers()) {
                    Location loc = player.getLocation();
                    loc.subtract(10.0d, 10.0d, 10.0d);
                    for (double y = 0.0d; y < 20.0d; y += 1.0d) {
                        for (double x = 0.0d; x < 20.0d; x += 1.0d) {
                            for (double z = 0.0d; z < 20.0d; z += 1.0d) {
                                loc.add(x, y, z);
                                if (isArtronFurnace(loc.getBlock()) && (!plugin.getGeneralKeeper().getArtronFurnaces().contains(loc.getBlock()))) {
                                    plugin.getGeneralKeeper().getArtronFurnaces().add(loc.getBlock());
                                }
                                loc.subtract(x, y, z);
                            }
                        }
                    }
                }
            }
        }, 60L, 10L);
    }

    private boolean isArtronFurnace(Block b) {
        try {
            if (b == null || (!b.getType().equals(Material.BURNING_FURNACE) && !b.getType().equals(Material.FURNACE))) {
                return false;
            }
            Furnace furnace = (Furnace) b.getState();
            if (furnace != null) {
                Inventory inv = furnace.getInventory();
                if (inv != null) {
                    String title = inv.getTitle();
                    return (title != null && title.equals("TARDIS Artron Furnace"));
                }
            }
        } catch (NullPointerException e) {
            return false;
        }
        return false;
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }
}
