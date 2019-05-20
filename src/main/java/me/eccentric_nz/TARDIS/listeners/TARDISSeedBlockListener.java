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
import me.eccentric_nz.TARDIS.builders.TARDISBuildData;
import me.eccentric_nz.TARDIS.builders.TARDISSeedBlockProcessor;
import me.eccentric_nz.TARDIS.database.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.enumeration.CONSOLES;
import me.eccentric_nz.TARDIS.enumeration.SCHEMATIC;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.*;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * @author eccentric_nz
 */
public class TARDISSeedBlockListener implements Listener {

    private final TARDIS plugin;
    private final HashMap<Location, TARDISBuildData> trackTARDISSeed = new HashMap<>();

    public TARDISSeedBlockListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Store the TARDIS Seed block's values for use when clicked with the TARDIS key to activate growing, or to return
     * the block if broken.
     *
     * @param event The TARDIS Seed block placement event
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onSeedBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        ItemStack is = player.getInventory().getItemInMainHand();
        if (!is.hasItemMeta()) {
            return;
        }
        ItemMeta im = is.getItemMeta();
        if (!im.hasDisplayName() || !im.hasLore()) {
            return;
        }
        if (im.getDisplayName().equals(ChatColor.GOLD + "TARDIS Seed Block")) {
            List<String> lore = im.getLore();
            SCHEMATIC schm = CONSOLES.getBY_NAMES().get(lore.get(0));
            Material wall = Material.valueOf(getValuesFromWallString(lore.get(1)));
            Material floor = Material.valueOf(getValuesFromWallString(lore.get(2)));
            TARDISBuildData seed = new TARDISBuildData();
            seed.setSchematic(schm);
            seed.setWallType(wall);
            seed.setFloorType(floor);
            Location l = event.getBlockPlaced().getLocation();
            trackTARDISSeed.put(l, seed);
            TARDISMessage.send(player, "SEED_PLACE");
            // now the player has to click the block with the TARDIS key
        }
    }

    /**
     * Return the TARDIS seed block to the player after it is broken.
     *
     * @param event a block break event
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)

    public void onSeedBlockBreak(BlockBreakEvent event) {
        Location l = event.getBlock().getLocation();
        Player p = event.getPlayer();
        if (trackTARDISSeed.containsKey(l)) {
            if (!p.getGameMode().equals(GameMode.CREATIVE)) {
                // get the Seed block data
                TARDISBuildData data = trackTARDISSeed.get(l);
                // drop a TARDIS Seed Block
                World w = l.getWorld();
                ItemStack is = new ItemStack(event.getBlock().getType(), 1);
                ItemMeta im = is.getItemMeta();
                if (im == null) {
                    return;
                }
                im.setDisplayName(ChatColor.GOLD + "TARDIS Seed Block");
                List<String> lore = new ArrayList<>();
                lore.add(data.getSchematic().getPermission().toUpperCase(Locale.ENGLISH));
                lore.add("Walls: " + data.getWallType().toString());
                lore.add("Floors: " + data.getFloorType().toString());
                im.setLore(lore);
                is.setItemMeta(im);
                // set the block to AIR
                event.getBlock().setBlockData(TARDISConstants.AIR);
                w.dropItemNaturally(l, is);
            }
            trackTARDISSeed.remove(l);
        }
    }

    /**
     * Process the TARDIS seed block and turn it into a TARDIS!
     *
     * @param event a block interact event
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onSeedInteract(PlayerInteractEvent event) {
        if (event.getHand() == null || event.getHand().equals(EquipmentSlot.OFF_HAND)) {
            return;
        }
        if (event.getClickedBlock() != null) {
            Location l = event.getClickedBlock().getLocation();
            if (trackTARDISSeed.containsKey(l)) {
                Player player = event.getPlayer();
                String key;
                HashMap<String, Object> where = new HashMap<>();
                where.put("uuid", player.getUniqueId().toString());
                ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, where);
                if (rsp.resultSet()) {
                    key = (!rsp.getKey().isEmpty()) ? rsp.getKey() : plugin.getConfig().getString("preferences.key");
                } else {
                    key = plugin.getConfig().getString("preferences.key");
                }
                if (player.getInventory().getItemInMainHand().getType().equals(Material.getMaterial(key))) {
                    if (!plugin.getPlanetsConfig().getBoolean("planets." + l.getWorld().getName() + ".time_travel")) {
                        TARDISMessage.send(player, "WORLD_NO_TARDIS");
                        return;
                    }
                    if (!plugin.getConfig().getString("creation.area").equals("none")) {
                        String area = plugin.getConfig().getString("creation.area");
                        if (plugin.getTardisArea().areaCheckInExile(area, l)) {
                            TARDISMessage.send(player, "TARDIS_ONLY_AREA", area);
                            return;
                        }
                    }
                    // grow a TARDIS
                    TARDISBuildData seed = trackTARDISSeed.get(l);
                    // process seed data
                    if (new TARDISSeedBlockProcessor(plugin).processBlock(seed, l, player)) {
                        // remove seed data
                        trackTARDISSeed.remove(l);
                        // remove seed block
                        event.getClickedBlock().setBlockData(TARDISConstants.AIR);
                    }
                }
            }
        }
    }

    /**
     * Determines the Material type of the block. Values are calculated by converting the string values stored in a
     * TARDIS Seed block.
     *
     * @param str the lore stored in the TARDIS Seed block's Item Meta
     * @return an String representing the Material
     */
    private String getValuesFromWallString(String str) {
        String[] split = str.split(": ");
        return split[1];
    }
}
