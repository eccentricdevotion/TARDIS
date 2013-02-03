/*
 * Copyright (C) 2013 eccentric_nz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.listeners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

/**
 * Following his disrupted resurrection, the Master was able to offensively use
 * energy - presumably his own artron energy - to strike his enemies with
 * debilitating energy blasts, at the cost of reducing his own life force.
 *
 * @author eccentric_nz
 */
public class TARDISCondenserListener implements Listener {

    private final TARDIS plugin;
    List<Material> condensables = new ArrayList<Material>();

    public TARDISCondenserListener(TARDIS plugin) {
        this.plugin = plugin;
        this.condensables.add(Material.DIRT);
        this.condensables.add(Material.COBBLESTONE);
        this.condensables.add(Material.SAND);
        this.condensables.add(Material.GRAVEL);
        this.condensables.add(Material.ROTTEN_FLESH);
    }

    /**
     * Listens for player interaction with the TARDIS condensor chest. When the
     * chest is closed, any DIRT, SAND, GRAVEL, COBBLESTONE or ROTTEN FLESH is
     * converted to Artron Energy at a ratio of 1:1.
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onChestClose(InventoryCloseEvent event) {
        Inventory inv = event.getInventory();
        InventoryHolder holder = inv.getHolder();
        if (holder instanceof Chest) {
            Chest chest = (Chest) holder;
            Location loc = chest.getLocation();
            String chest_loc = loc.getWorld().getName() + ":" + loc.getBlockX() + ":" + loc.getBlockY() + ":" + loc.getBlockZ();
            HashMap<String, Object> where = new HashMap<String, Object>();
            where.put("condenser", chest_loc);
            ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
            if (rs.resultSet()) {
                int amount = 0;
                // get the stacks in the inventory
                for (ItemStack is : inv.getContents()) {
                    if (is != null && condensables.contains(is.getType())) {
                        int stack_size = is.getAmount();
                        amount += stack_size;
                        inv.remove(is);
                    }
                }
                // halve it cause 1:1 is too much...
                amount = Math.round(amount / 2.0F);
                QueryFactory qf = new QueryFactory(plugin);
                HashMap<String, Object> wheret = new HashMap<String, Object>();
                wheret.put("tardis_id", rs.getTardis_id());
                final Player player = (Player) event.getPlayer();
                qf.alterEnergyLevel("tardis", amount, wheret, player);
                String message;
                if (amount > 0) {
                    message = "You condensed the molecules of the universe itself into " + amount + " artron energy!";
                } else {
                    message = "There were no valid materials to condense!";
                }
                player.sendMessage(plugin.pluginName + message);
            }
        }
    }
}
