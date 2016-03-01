package me.eccentric_nz.TARDIS.artron;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.Furnace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

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
                for (Player player : plugin.getServer().getOnlinePlayers()) {
                    Location loc = player.getLocation();
                    loc.subtract(10.0d, 10.0d, 10.0d);
                    for (double y = 0.0d; y < 20.0d; y += 1.0d) {
                        for (double x = 0.0d; x < 20.0d; x += 1.0d) {
                            for (double z = 0.0d; z < 20.0d; z += 1.0d) {
                                loc.add(x, y, z);
                                if (isArtronFurnace(loc.getBlock())) {
                                    player.spawnParticle(Particle.WATER_SPLASH, loc.getBlock().getLocation().add(0.5d, 1.0d, 0.5d), 10);
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
}
