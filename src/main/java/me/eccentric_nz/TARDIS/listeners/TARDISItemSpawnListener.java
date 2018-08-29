package me.eccentric_nz.TARDIS.listeners;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemSpawnEvent;

import java.util.Arrays;
import java.util.List;

public class TARDISItemSpawnListener implements Listener {

    private final TARDIS plugin;
    private final List<Material> items = Arrays.asList(Material.WHITE_BED, Material.ORANGE_BED, Material.MAGENTA_BED, Material.YELLOW_BED, Material.LIME_BED, Material.PINK_BED, Material.GRAY_BED, Material.LIGHT_GRAY_BED, Material.PURPLE_BED, Material.CYAN_BED, Material.BLUE_BED, Material.GREEN_BED, Material.BROWN_BED, Material.RED_BED, Material.BLACK_BED, Material.LIGHT_BLUE_BED, Material.WHEAT_SEEDS, Material.DANDELION);

    public TARDISItemSpawnListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onBedDrop(ItemSpawnEvent event) {
        if ((plugin.getTrackerKeeper().getMaterialising().size() > 0 || plugin.getTrackerKeeper().getDematerialising().size() > 0) && items.contains(event.getEntity().getItemStack().getType())) {
            event.setCancelled(true);
        }
    }
}
