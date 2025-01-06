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
package me.eccentric_nz.TARDIS.handles;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.custommodels.keys.Whoniverse;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetControls;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;
import java.util.HashMap;

/**
 * @author eccentric_nz
 */
public class TARDISHandlesListener implements Listener {

    private final TARDIS plugin;

    public TARDISHandlesListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onHandlesBreak(BlockBreakEvent event) {
        Block b = event.getBlock();
        if (!b.getType().equals(Material.BIRCH_BUTTON)) {
            return;
        }
        // check location
        HashMap<String, Object> where = new HashMap<>();
        where.put("type", 26);
        where.put("location", b.getLocation().toString());
        ResultSetControls rsc = new ResultSetControls(plugin, where, false);
        if (!rsc.resultSet()) {
            return;
        }
        event.setCancelled(true);
        // set block to AIR
        b.setBlockData(TARDISConstants.AIR);
        // drop a custom BIRCH_BUTTON
        ItemStack is = new ItemStack(Material.BIRCH_BUTTON, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("Handles");
        im.setLore(Arrays.asList("Cyberhead from the", "Maldovar Market"));
        im.setItemModel(Whoniverse.HANDLES_OFF.getKey());
        im.getPersistentDataContainer().set(plugin.getCustomBlockKey(), PersistentDataType.INTEGER, 1);
        is.setItemMeta(im);
        b.getWorld().dropItemNaturally(b.getLocation(), is);
        // remove control record
        HashMap<String, Object> wherec = new HashMap<>();
        wherec.put("c_id", rsc.getC_id());
        plugin.getQueryFactory().doDelete("controls", wherec);
    }

    @EventHandler(ignoreCancelled = true)
    public void onHandlesPlace(BlockPlaceEvent event) {
        ItemStack is = event.getItemInHand();
        if (!is.getType().equals(Material.BIRCH_BUTTON) || !is.hasItemMeta()) {
            return;
        }
        ItemMeta im = is.getItemMeta();
        if (im.hasDisplayName() && im.getDisplayName().endsWith("Handles")) {
            // can only be placed in an item frame
            event.setCancelled(true);
            plugin.getMessenger().send(event.getPlayer(), TardisModule.TARDIS, "HANDLES_FRAME");
        }
    }
}
