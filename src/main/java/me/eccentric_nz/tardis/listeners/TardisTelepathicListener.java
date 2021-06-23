/*
 * Copyright (C) 2021 eccentric_nz
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
package me.eccentric_nz.tardis.listeners;

import me.eccentric_nz.tardis.TardisConstants;
import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.database.resultset.ResultSetControls;
import me.eccentric_nz.tardis.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.tardis.database.resultset.ResultSetTardis;
import me.eccentric_nz.tardis.messaging.TardisMessage;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class TardisTelepathicListener implements Listener {

    private final TardisPlugin plugin;

    public TardisTelepathicListener(TardisPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onTelepathicCircuit(PlayerInteractEvent event) {
        if (event.getHand() == null || event.getHand().equals(EquipmentSlot.OFF_HAND)) {
            return;
        }
        Block block = event.getClickedBlock();
        assert block != null;
        if (!block.getType().equals(Material.DAYLIGHT_DETECTOR)) {
            return;
        }
        String location = block.getLocation().toString();
        // get tardis from saved location
        HashMap<String, Object> where = new HashMap<>();
        where.put("type", 23);
        where.put("location", location);
        ResultSetControls rsc = new ResultSetControls(plugin, where, false);
        if (rsc.resultSet()) {
            int id = rsc.getTardisId();
            // get the Time Lord of this tardis
            HashMap<String, Object> wheret = new HashMap<>();
            wheret.put("tardis_id", id);
            ResultSetTardis rs = new ResultSetTardis(plugin, wheret, "", false, 0);
            if (rs.resultSet()) {
                UUID o_uuid = rs.getTardis().getUuid();
                String owner = o_uuid.toString();
                // get Time Lord player prefs
                ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, owner);
                if (rsp.resultSet()) {
                    Player player = event.getPlayer();
                    UUID uuid = player.getUniqueId();
                    if (rsp.isTelepathyOn()) {
                        // track player
                        plugin.getTrackerKeeper().getTelepaths().put(uuid, o_uuid);
                        TardisMessage.send(player, "TELEPATHIC_COMMAND");
                    } else {
                        TardisMessage.send(player, "TELEPATHIC_OFF");
                    }
                }
            }
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> block.setBlockData(TardisConstants.DAYLIGHT), 3L);
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
        b.setBlockData(TardisConstants.AIR);
        // drop a custom DAYLIGHT_DETECTOR
        ItemStack is = new ItemStack(Material.DAYLIGHT_DETECTOR, 1);
        ItemMeta im = is.getItemMeta();
        assert im != null;
        im.setDisplayName("tardis Telepathic Circuit");
        im.setLore(Arrays.asList("Allow companions to", "use tardis commands"));
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
        assert im != null;
        if (im.hasDisplayName() && im.getDisplayName().equals("tardis Telepathic Circuit")) {
            UUID uuid = event.getPlayer().getUniqueId();
            String l = event.getBlock().getLocation().toString();
            plugin.getTrackerKeeper().getTelepathicPlacements().put(uuid, l);
        }
    }
}
