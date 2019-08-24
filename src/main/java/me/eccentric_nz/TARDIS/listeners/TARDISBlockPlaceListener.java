/*
 * Copyright (C) 2019 eccentric_nz
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
package me.eccentric_nz.TARDIS.listeners;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.MultipleFacing;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;

/**
 * TARDISes are bioships that are grown from a species of coral presumably indigenous to Gallifrey.
 * <p>
 * The TARDIS had a drawing room, which the Doctor claimed to be his "private study". Inside it were momentos of his
 * many incarnations' travels.
 *
 * @author eccentric_nz
 */
public class TARDISBlockPlaceListener implements Listener {

    private final TARDIS plugin;
    private final NamespacedKey nsk;

    public TARDISBlockPlaceListener(TARDIS plugin) {
        this.plugin = plugin;
        nsk = new NamespacedKey(this.plugin, "customBlock");
    }

    /**
     * Listens for a player placing a block. If the player places a brown mushroom block with a TARDIS namespaced key,
     * then convert it to one of the unused brown mushroom block states.
     *
     * @param event a player placing a block
     */

    @EventHandler(ignoreCancelled = true)
    public void onPlayerBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (plugin.getTrackerKeeper().getZeroRoomOccupants().contains(player.getUniqueId())) {
            event.setCancelled(true);
            TARDISMessage.send(player, "NOT_IN_ZERO");
            return;
        }
        String blockStr = event.getBlockPlaced().getLocation().toString();
        if (plugin.getGeneralKeeper().getProtectBlockMap().containsKey(blockStr)) {
            event.setCancelled(true);
            TARDISMessage.send(player, "NO_PLACE");
        }
        ItemStack is = event.getItemInHand();
        if (is.getType().equals(Material.BROWN_MUSHROOM_BLOCK) && is.hasItemMeta()) {
            ItemMeta im = is.getItemMeta();
            if (im.getPersistentDataContainer().has(nsk, PersistentDataType.INTEGER)) {
                int which = im.getPersistentDataContainer().get(nsk, PersistentDataType.INTEGER);
                MultipleFacing multipleFacing = (MultipleFacing) Material.BROWN_MUSHROOM_BLOCK.createBlockData();
                switch (which) {
                    case 1:
                        multipleFacing.setFace(BlockFace.DOWN, false);
                        multipleFacing.setFace(BlockFace.EAST, false);
                        multipleFacing.setFace(BlockFace.NORTH, false);
                        multipleFacing.setFace(BlockFace.SOUTH, false);
                        multipleFacing.setFace(BlockFace.UP, false);
                        multipleFacing.setFace(BlockFace.WEST, true);
                        break;
                    case 2:
                        multipleFacing.setFace(BlockFace.DOWN, false);
                        multipleFacing.setFace(BlockFace.EAST, false);
                        multipleFacing.setFace(BlockFace.NORTH, false);
                        multipleFacing.setFace(BlockFace.SOUTH, true);
                        multipleFacing.setFace(BlockFace.UP, false);
                        multipleFacing.setFace(BlockFace.WEST, false);
                        break;
                    case 3:
                        multipleFacing.setFace(BlockFace.DOWN, false);
                        multipleFacing.setFace(BlockFace.EAST, false);
                        multipleFacing.setFace(BlockFace.NORTH, false);
                        multipleFacing.setFace(BlockFace.SOUTH, true);
                        multipleFacing.setFace(BlockFace.UP, false);
                        multipleFacing.setFace(BlockFace.WEST, true);
                        break;
                    case 4:
                        multipleFacing.setFace(BlockFace.DOWN, false);
                        multipleFacing.setFace(BlockFace.EAST, false);
                        multipleFacing.setFace(BlockFace.NORTH, true);
                        multipleFacing.setFace(BlockFace.SOUTH, false);
                        multipleFacing.setFace(BlockFace.UP, false);
                        multipleFacing.setFace(BlockFace.WEST, false);
                        break;
                    case 5:
                        multipleFacing.setFace(BlockFace.DOWN, false);
                        multipleFacing.setFace(BlockFace.EAST, false);
                        multipleFacing.setFace(BlockFace.NORTH, true);
                        multipleFacing.setFace(BlockFace.SOUTH, false);
                        multipleFacing.setFace(BlockFace.UP, false);
                        multipleFacing.setFace(BlockFace.WEST, true);
                        break;
                    default:
                }
                Block block = event.getBlockPlaced();
                block.setBlockData(multipleFacing);
                return;
            }
        }
        if (!player.hasPermission("tardis.rift")) {
            return;
        }
        if (!is.getType().equals(Material.BEACON) || !is.hasItemMeta()) {
            return;
        }
        ItemMeta im = is.getItemMeta();
        if (im.hasDisplayName() && im.getDisplayName().equals("Rift Manipulator")) {
            // make sure they're not inside the TARDIS
            HashMap<String, Object> where = new HashMap<>();
            where.put("uuid", player.getUniqueId().toString());
            ResultSetTravellers rst = new ResultSetTravellers(plugin, where, false);
            if (rst.resultSet()) {
                event.setCancelled(true);
                TARDISMessage.send(player, "RIFT_OUTSIDE");
                return;
            }
            // add recharger to to config
            Location l = event.getBlockPlaced().getLocation();
            String name = "rift_" + player.getName() + "_" + TARDISConstants.RANDOM.nextInt(Integer.MAX_VALUE);
            while (plugin.getConfig().contains("rechargers." + name)) {
                name = "rift_" + player.getName() + "_" + TARDISConstants.RANDOM.nextInt(Integer.MAX_VALUE);
            }
            plugin.getConfig().set("rechargers." + name + ".world", l.getWorld().getName());
            plugin.getConfig().set("rechargers." + name + ".x", l.getBlockX());
            plugin.getConfig().set("rechargers." + name + ".y", l.getBlockY());
            plugin.getConfig().set("rechargers." + name + ".z", l.getBlockZ());
            plugin.getConfig().set("rechargers." + name + ".uuid", player.getUniqueId().toString());
            plugin.saveConfig();
            TARDISMessage.send(player, "RIFT_SUCCESS");
        }
    }
}
