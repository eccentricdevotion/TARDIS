/*
 * Copyright (C) 2016 eccentric_nz
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

import java.util.HashMap;
import java.util.Random;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * TARDISes are bioships that are grown from a species of coral presumably
 * indigenous to Gallifrey.
 *
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
     * Listens for player block placing. If the player places a stack of blocks
     * in a certain pattern for example (but not limited to): IRON_BLOCK,
     * LAPIS_BLOCK, RESTONE_TORCH the pattern of blocks is turned into a TARDIS.
     *
     * @param event a player placing a block
     */
    @SuppressWarnings("deprecation")
    @EventHandler(ignoreCancelled = true)
    public void onPlayerBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (plugin.getTrackerKeeper().getZeroRoomOccupants().contains(player.getUniqueId())) {
            event.setCancelled(true);
            TARDISMessage.send(player, "NOT_IN_ZERO");
            return;
        }
        String block = event.getBlockPlaced().getLocation().toString();
        if (plugin.getGeneralKeeper().getProtectBlockMap().containsKey(block)) {
            event.setCancelled(true);
            TARDISMessage.send(player, "NO_PLACE");
        }
        if (!player.hasPermission("tardis.rift")) {
            return;
        }
        ItemStack is = event.getItemInHand();
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
            Random rand = new Random();
            String name = "rift_" + player.getName() + "_" + rand.nextInt(Integer.MAX_VALUE);
            while (plugin.getConfig().contains("rechargers." + name)) {
                name = "rift_" + player.getName() + "_" + rand.nextInt(Integer.MAX_VALUE);
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
