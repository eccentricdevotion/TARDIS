package me.eccentric_nz.TARDIS.planets;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Locale;

public class TARDISVastialListener implements Listener {

    private final TARDIS plugin;

    public TARDISVastialListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onWhiteConcretePowderMine(BlockBreakEvent event) {
        Location location = event.getBlock().getLocation();
        if (!location.getWorld().getName().equalsIgnoreCase("telos")) {
            return;
        }
        if (!event.getBlock().getType().equals(Material.WHITE_CONCRETE_POWDER)) {
            return;
        }
        if (TARDISConstants.RANDOM.nextInt(100) < plugin.getPlanetsConfig().getInt("planets.telos.vastial.gunpowder_chance")) {
            int size = event.getBlock().getDrops().size();
            event.getBlock().getDrops().clear();
            location.getWorld().dropItemNaturally(location, ItemStack.of(Material.GUNPOWDER, size));
        }
    }
}
