package me.eccentric_nz.plugins.TARDIS;

import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockIgniteEvent;

public class TARDISBlockProtectListener implements Listener {

    private TARDIS plugin;

    public TARDISBlockProtectListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockIgnite(BlockIgniteEvent event) {
        if (plugin.config.getBoolean("protect_blocks") == true) {
            String[] set = {"EAST", "SOUTH", "WEST", "NORTH", "UP", "DOWN"};
            for (String f : set) {
                int id = event.getBlock().getRelative(BlockFace.valueOf(f)).getTypeId();
                byte d = event.getBlock().getRelative(BlockFace.valueOf(f)).getData();
                if (id == 35 && (d == 1 || d == 7 || d == 8)) {
                    event.setCancelled(true);
                    break;
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockBurn(BlockBurnEvent event) {
        if (plugin.config.getBoolean("protect_blocks") == true) {
            String[] set = {"EAST", "SOUTH", "WEST", "NORTH", "UP", "DOWN"};
            for (String f : set) {
                int id = event.getBlock().getRelative(BlockFace.valueOf(f)).getTypeId();
                byte d = event.getBlock().getRelative(BlockFace.valueOf(f)).getData();
                if (id == 35 && (d == 1 || d == 7 || d == 8)) {
                    event.setCancelled(true);
                    break;
                }
            }
        }
    }
}
