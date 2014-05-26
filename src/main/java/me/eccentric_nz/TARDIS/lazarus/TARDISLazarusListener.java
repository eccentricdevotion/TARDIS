/*
 * Copyright (C) 2014 eccentric_nz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.lazarus;

import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetControls;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;

/**
 *
 * @author eccentric_nz
 */
public class TARDISLazarusListener implements Listener {

    private final TARDIS plugin;

    public TARDISLazarusListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onInteract(PlayerInteractEvent event) {
        Action a = event.getAction();
        if (a.equals(Action.PHYSICAL) && event.getClickedBlock().getType().equals(Material.WOOD_PLATE)) {
            final Player player = event.getPlayer();
            if (plugin.getTrackerKeeper().getLazarus().containsKey(player.getUniqueId())) {
                return;
            }
            if (player.hasPermission("tardis.lazarus")) {
                Block b = event.getClickedBlock();
                String l = b.getLocation().toString();
                // is it a lazarus plate?
                HashMap<String, Object> where = new HashMap<String, Object>();
                where.put("location", l);
                where.put("type", 19);
                ResultSetControls rsc = new ResultSetControls(plugin, where, false);
                if (rsc.resultSet()) {
                    // track the block
                    plugin.getTrackerKeeper().getLazarus().put(player.getUniqueId(), b);
                    // close the door
                    b.getRelative(BlockFace.SOUTH).setType(Material.COBBLE_WALL);
                    b.getRelative(BlockFace.SOUTH).getRelative(BlockFace.UP).setType(Material.COBBLE_WALL);
                    // open the GUI
                    Inventory inv = plugin.getServer().createInventory(player, 54, "§4Genetic Manipulator");
                    inv.setContents(new TARDISLazarusInventory(plugin).getTerminal());
                    player.openInventory(inv);
                }
            }
        }
    }
}
