package me.eccentric_nz.TARDIS.artron;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.utility.TARDISEffectLibHelper;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Furnace;
import org.bukkit.entity.Player;

public class TARDISArtronFurnaceParticle {

    private final TARDIS plugin;

    public TARDISArtronFurnaceParticle(TARDIS plugin) {
        this.plugin = plugin;
    }

    @SuppressWarnings("deprecation")
    public void addParticles() {

        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                for (Block block : plugin.getGeneralKeeper().getArtronFurnaces()) {
                    if (isArtronFurnace(block)) {
                        TARDISEffectLibHelper.sendWaterParticle(block.getLocation());
                    }
                }
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
        if (b == null || (!b.getType().equals(Material.BURNING_FURNACE) && !b.getType().equals(Material.FURNACE))) {
            return false;
        }
        Furnace furnace = (Furnace) b.getState();
        return (furnace != null && furnace.getInventory().getTitle().equals("TARDIS Artron Furnace"));
    }
}
