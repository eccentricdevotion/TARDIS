/*
 * Copyright (C) 2024 eccentric_nz
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
package me.eccentric_nz.TARDIS.listeners.controls;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.console.telepathic.TARDISTelepathicInventory;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetControls;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.floodgate.FloodgateTelepathicForm;
import me.eccentric_nz.TARDIS.floodgate.TARDISFloodgate;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class TARDISTelepathicListener implements Listener {

    private final TARDIS plugin;

    public TARDISTelepathicListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onTelepathicCircuit(PlayerInteractEvent event) {
        if (event.getHand() == null || event.getHand().equals(EquipmentSlot.OFF_HAND)) {
            return;
        }
        Block block = event.getClickedBlock();
        if (block != null && block.getType().equals(Material.DAYLIGHT_DETECTOR)) {
            String location = block.getLocation().toString();
            // get tardis from saved location
            HashMap<String, Object> where = new HashMap<>();
            where.put("type", 23);
            where.put("location", location);
            ResultSetControls rsc = new ResultSetControls(plugin, where, false);
            if (rsc.resultSet()) {
                int id = rsc.getTardis_id();
                Player player = event.getPlayer();
                if (player.isSneaking()) {
                    // get the Time Lord of this TARDIS
                    HashMap<String, Object> wheret = new HashMap<>();
                    wheret.put("tardis_id", id);
                    ResultSetTardis rs = new ResultSetTardis(plugin, wheret, "", false, 0);
                    if (rs.resultSet()) {
                        UUID o_uuid = rs.getTardis().getUuid();
                        String owner = o_uuid.toString();
                        // get Time Lord player prefs
                        ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, owner);
                        if (rsp.resultSet()) {
                            UUID uuid = player.getUniqueId();
                            if (rsp.isTelepathyOn()) {
                                // track player
                                plugin.getTrackerKeeper().getTelepaths().put(uuid, o_uuid);
                                plugin.getMessenger().send(player, TardisModule.TARDIS, "TELEPATHIC_COMMAND");
                            } else {
                                plugin.getMessenger().send(player, TardisModule.TARDIS, "TELEPATHIC_OFF");
                            }
                        }
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> block.setBlockData(TARDISConstants.DAYLIGHT), 3L);
                    }
                } else {
                    if (TARDISFloodgate.isFloodgateEnabled() && TARDISFloodgate.isBedrockPlayer(player.getUniqueId())) {
                        new FloodgateTelepathicForm(plugin, player.getUniqueId(), id).send();
                    } else {
                        // open the Telepathic GUI
                        TARDISTelepathicInventory tti = new TARDISTelepathicInventory(plugin, player);
                        ItemStack[] gui = tti.getButtons();
                        Inventory telepathic = plugin.getServer().createInventory(player, 54, ChatColor.DARK_RED + "TARDIS Telepathic Circuit");
                        telepathic.setContents(gui);
                        player.openInventory(telepathic);
                    }
                }
            }
        } else {
            ItemStack is = event.getItem();
            if (is == null) {
                return;
            }
            if (!is.getType().equals(Material.GLOWSTONE_DUST) || !is.hasItemMeta()) {
                return;
            }
            ItemMeta im = is.getItemMeta();
            if (im.hasDisplayName() && im.getDisplayName().equals("TARDIS Telepathic Circuit")) {
                Block up = event.getClickedBlock().getRelative(BlockFace.UP);
                if (!up.getType().isAir()) {
                    return;
                }
                up.setType(Material.DAYLIGHT_DETECTOR);
                int amount = is.getAmount();
                if (amount > 1) {
                    is.setAmount(amount - 1);
                } else {
                    event.getPlayer().getInventory().setItemInMainHand(null);
                }
                UUID uuid = event.getPlayer().getUniqueId();
                String l = up.getLocation().toString();
                plugin.getTrackerKeeper().getTelepathicPlacements().put(uuid, l);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onTelepathicCircuitBreak(BlockBreakEvent event) {
        Block b = event.getBlock();
        if (!b.getType().equals(Material.DAYLIGHT_DETECTOR)) {
            return;
        }
        // check location
        HashMap<String, Object> where = new HashMap<>();
        where.put("type", 23);
        where.put("location", b.getLocation().toString());
        ResultSetControls rsc = new ResultSetControls(plugin, where, false);
        if (!rsc.resultSet()) {
            return;
        }
        event.setCancelled(true);
        // set block to AIR
        b.setBlockData(TARDISConstants.AIR);
        // drop a custom GLOWSTONE_DUST
        ItemStack is = new ItemStack(Material.GLOWSTONE_DUST, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("TARDIS Telepathic Circuit");
        im.setCustomModelData(10001962);
        im.setLore(Arrays.asList("Allow companions to", "use TARDIS commands", "and search for", "travel destinations."));
        is.setItemMeta(im);
        b.getWorld().dropItemNaturally(b.getLocation(), is);
    }

    @EventHandler(ignoreCancelled = true)
    public void onTelepathicCircuitPlace(BlockPlaceEvent event) {
        ItemStack is = event.getItemInHand();
        if (!is.getType().equals(Material.DAYLIGHT_DETECTOR) || !is.hasItemMeta()) {
            return;
        }
        ItemMeta im = is.getItemMeta();
        if (im.hasDisplayName() && im.getDisplayName().equals("TARDIS Telepathic Circuit")) {
            UUID uuid = event.getPlayer().getUniqueId();
            String l = event.getBlock().getLocation().toString();
            plugin.getTrackerKeeper().getTelepathicPlacements().put(uuid, l);
        }
    }
}
