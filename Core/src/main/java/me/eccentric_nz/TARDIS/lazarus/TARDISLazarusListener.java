/*
 * Copyright (C) 2025 eccentric_nz
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

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetControls;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisPowered;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.floodgate.FloodgateGeneticManipulatorForm;
import me.eccentric_nz.TARDIS.floodgate.TARDISFloodgate;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class TARDISLazarusListener implements Listener {

    final BlockData WALL = Material.COBBLESTONE_WALL.createBlockData();
    private final TARDIS plugin;

    public TARDISLazarusListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onInteract(PlayerInteractEvent event) {
        Action a = event.getAction();
        if (a.equals(Action.PHYSICAL) && event.getClickedBlock().getType().equals(Material.OAK_PRESSURE_PLATE)) {
            Player player = event.getPlayer();
            UUID uuid = player.getUniqueId();
            if (plugin.getTrackerKeeper().getLazarus().containsKey(uuid)) {
                return;
            }
            if (TARDISPermission.hasPermission(player, "tardis.lazarus")) {
                Block b = event.getClickedBlock();
                String l = b.getLocation().toString();
                // is it a lazarus plate?
                HashMap<String, Object> where = new HashMap<>();
                where.put("location", l);
                where.put("type", 19);
                ResultSetControls rsc = new ResultSetControls(plugin, where, false);
                if (rsc.resultSet()) {
                    // check for power
                    if (plugin.getConfig().getBoolean("allow.power_down")) {
                        ResultSetTardisPowered rs = new ResultSetTardisPowered(plugin);
                        if (rs.fromID(rsc.getTardis_id()) && !rs.isPowered()) {
                            plugin.getMessenger().send(player, TardisModule.TARDIS, "POWER_DOWN");
                            return;
                        }
                    }
                    // track the block
                    plugin.getTrackerKeeper().getLazarus().put(uuid, b);
                    // close the door
                    b.getRelative(BlockFace.SOUTH).setBlockData(WALL);
                    b.getRelative(BlockFace.SOUTH).getRelative(BlockFace.UP).setBlockData(WALL);
                    // open the GUI
                    if (TARDISFloodgate.isFloodgateEnabled() && TARDISFloodgate.isBedrockPlayer(uuid)) {
                        new FloodgateGeneticManipulatorForm(plugin, uuid, b).send();
                    } else {
                        Inventory inv = plugin.getServer().createInventory(player, 54, ChatColor.DARK_RED + "Genetic Manipulator");
                        inv.setContents(new TARDISLazarusInventory(plugin).getPageOne());
                        player.openInventory(inv);
                    }
                }
            }
        }
    }
}
