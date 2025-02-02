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
package me.eccentric_nz.TARDIS.listeners;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.customblocks.LampToggler;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItem;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItemUtils;
import me.eccentric_nz.TARDIS.customblocks.TARDISMushroomBlockData;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.rooms.eye.TARDISSpaceHelmetListener;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;

/**
 * TARDISes are bioships that are grown from a species of coral presumably
 * indigenous to Gallifrey.
 * <p>
 * The TARDIS had a drawing room, which the Doctor claimed to be his "private
 * study". Inside it were momentos of his many incarnations' travels.
 *
 * @author eccentric_nz
 */
public class TARDISBlockPlaceListener implements Listener {

    private final TARDIS plugin;

    public TARDISBlockPlaceListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Listens for a player placing a block. If the player places a mushroom
     * block with a TARDIS namespaced key, then convert it to a new custom
     * TARDIS block Item Display entity.
     *
     * @param event a player placing a block
     */
    @EventHandler(ignoreCancelled = true)
    public void onPlayerBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (plugin.getTrackerKeeper().getZeroRoomOccupants().contains(player.getUniqueId())) {
            event.setCancelled(true);
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NOT_IN_ZERO");
            return;
        }
        Block block = event.getBlockPlaced();
        String blockStr = block.getLocation().toString();
        if (plugin.getGeneralKeeper().getProtectBlockMap().containsKey(blockStr)) {
            event.setCancelled(true);
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_PLACE");
        }
        ItemStack is = event.getItemInHand();
        // if it's a space helmet cancel
        if (TARDISSpaceHelmetListener.isSpaceHelmet(is)) {
            event.setCancelled(true);
            return;
        }
        // convert old custom mushroom blocks
        if ((is.getType().equals(Material.BROWN_MUSHROOM_BLOCK) || is.getType().equals(Material.RED_MUSHROOM_BLOCK) || is.getType().equals(Material.MUSHROOM_STEM))) {
            if (is.hasItemMeta()) {
                ItemMeta im = is.getItemMeta();
                if (im.getPersistentDataContainer().has(plugin.getCustomBlockKey(), PersistentDataType.INTEGER)) {
                    TARDISDisplayItem tdi = TARDISMushroomBlockData.getTARDISBlock(block.getBlockData());
                    if (tdi != null) {
                        if (tdi == TARDISDisplayItem.HEAT_BLOCK) {
                            // remember heat block location
                            plugin.getTrackerKeeper().getHeatBlocks().add(blockStr);
                        }
                        if (tdi.isLight()) {
                            LampToggler.setLightlevel(block, tdi.isLit() ? 15 : 0);
                            // also add interaction entity
                            TARDISDisplayItemUtils.set(block.getLocation(), tdi.getCustomModel().getKey(), false);
                        } else {
                            block.setBlockData(TARDISConstants.BARRIER);
                        }
                        TARDISDisplayItemUtils.set(tdi, block, -1);
                    }
                    return;
                }
            }
        }
        if (!TARDISPermission.hasPermission(player, "tardis.rift")) {
            return;
        }
        if (!is.getType().equals(Material.BEACON) || !is.hasItemMeta()) {
            return;
        }
        ItemMeta im = is.getItemMeta();
        if (im.hasDisplayName() && im.getDisplayName().endsWith("Rift Manipulator")) {
            // make sure they're not inside the TARDIS
            HashMap<String, Object> where = new HashMap<>();
            where.put("uuid", player.getUniqueId().toString());
            ResultSetTravellers rst = new ResultSetTravellers(plugin, where, false);
            if (rst.resultSet()) {
                event.setCancelled(true);
                plugin.getMessenger().send(player, TardisModule.TARDIS, "RIFT_OUTSIDE");
                return;
            }
            // add recharger to to config
            Location l = block.getLocation();
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
            plugin.getMessenger().send(player, TardisModule.TARDIS, "RIFT_SUCCESS");
        }
    }
}
