/*
 *  Copyright 2014 eccentric_nz.
 */
package me.eccentric_nz.tardisvortexmanipulator.listeners;

import me.eccentric_nz.tardisvortexmanipulator.TARDISVortexManipulator;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMQueryFactory;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMResultSetManipulator;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import java.util.HashMap;

/**
 * @author eccentric_nz
 */
public class TVMCraftListener implements Listener {

    private final TARDISVortexManipulator plugin;

    public TVMCraftListener(TARDISVortexManipulator plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onCraftManipulator(CraftItemEvent event) {
        Recipe recipe = event.getRecipe();
        ItemStack is = recipe.getResult();
        if (is.getType().equals(Material.CLOCK) && is.hasItemMeta() && is.getItemMeta().hasDisplayName() && is.getItemMeta().getDisplayName().equals("Vortex Manipulator")) {
            Player player = (Player) event.getWhoClicked();
            String uuid = player.getUniqueId().toString();
            // check if they have a manipulator record
            TVMResultSetManipulator rs = new TVMResultSetManipulator(plugin, uuid);
            if (!rs.resultSet()) {
                // make a record
                HashMap<String, Object> set = new HashMap<>();
                set.put("uuid", uuid);
                new TVMQueryFactory(plugin).doInsert("manipulator", set);
            }
        }
    }
}
