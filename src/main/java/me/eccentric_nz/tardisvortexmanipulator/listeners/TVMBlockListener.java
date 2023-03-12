package me.eccentric_nz.tardisvortexmanipulator.listeners;

import me.eccentric_nz.tardisvortexmanipulator.TARDISVortexManipulator;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class TVMBlockListener implements Listener {

    private final TARDISVortexManipulator plugin;

    public TVMBlockListener(TARDISVortexManipulator plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onInteract(BlockBreakEvent event) {
        if (plugin.getBlocks().contains(event.getBlock().getLocation())) {
            event.setCancelled(true);
        }
    }
}
