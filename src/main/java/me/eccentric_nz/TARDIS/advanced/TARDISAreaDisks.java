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
package me.eccentric_nz.TARDIS.advanced;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.database.data.Area;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetAreas;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetDiskStorage;
import me.eccentric_nz.TARDIS.enumeration.Storage;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The visual stabiliser circuit controlled the TARDIS' outward appearance. Its removal rendered the ship invisible.
 *
 * @author eccentric_nz
 */
class TARDISAreaDisks {

    private final TARDIS plugin;

    TARDISAreaDisks(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Makes an array of item stacks containing the default Storage GUI top and any area storage disks the player has
     * permission for.
     *
     * @param p the player to create the array for
     * @return an array of item stacks
     */
    ItemStack[] makeDisks(Player p) {

        List<ItemStack> areas = new ArrayList<>();
        // get the areas this player has access to
        ResultSetAreas rsa = new ResultSetAreas(plugin, null, false, false);
        if (rsa.resultSet()) {
            // cycle through areas
            rsa.getData().forEach((a) -> {
                String name = a.getAreaName();
                if (TARDISPermission.hasPermission(p, "tardis.area." + name) || TARDISPermission.hasPermission(p, "tardis.area.*")) {
                    ItemStack is = new ItemStack(Material.MUSIC_DISC_BLOCKS, 1);
                    ItemMeta im = is.getItemMeta();
                    im.setDisplayName("Area Storage Disk");
                    List<String> lore = new ArrayList<>();
                    lore.add(name);
                    lore.add(a.getWorld());
                    im.setLore(lore);
                    im.setCustomModelData(10000001);
                    is.setItemMeta(im);
                    areas.add(is);
                }
            });
        }
        ItemStack[] stack = new ItemStack[54];
        // set default top slots
        try {
            stack = TARDISSerializeInventory.itemStacksFromString(Storage.AREA.getEmpty());
        } catch (IOException ex) {
            plugin.debug("Could not get make Area Disk Inventory: " + ex);
        }
        // set saved slots
        int i = 27;
        for (ItemStack st : areas) {
            stack[i] = st;
            i++;
        }
        return stack;
    }

    /**
     * Checks the players current area disks and adds any new ones they have permission for.
     *
     * @param p the player to check for
     * @return a serialized String
     */
    String checkDisksForNewAreas(Player p) {
        String serialized = "";
        // get the player's storage record
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", p.getUniqueId().toString());
        ResultSetDiskStorage rs = new ResultSetDiskStorage(plugin, where);
        if (rs.resultSet()) {
            List<String> player_has = new ArrayList<>();
            String serilized_areas = rs.getAreas();
            try {
                // check storage inventory
                ItemStack[] areas = TARDISSerializeInventory.itemStacksFromString(serilized_areas);
                for (ItemStack a : areas) {
                    if (a != null && a.getType().equals(Material.MUSIC_DISC_BLOCKS) && a.hasItemMeta()) {
                        ItemMeta ima = a.getItemMeta();
                        if (ima.hasLore()) {
                            player_has.add(ima.getLore().get(0));
                        }
                    }
                }
                // check console inventory
                ItemStack[] console = TARDISSerializeInventory.itemStacksFromString(rs.getConsole());
                for (ItemStack c : console) {
                    if (c != null && c.getType().equals(Material.MUSIC_DISC_BLOCKS) && c.hasItemMeta()) {
                        ItemMeta imc = c.getItemMeta();
                        if (imc.hasLore()) {
                            player_has.add(imc.getLore().get(0));
                        }
                    }
                }
                // check player inventory
                ItemStack[] player = p.getInventory().getContents();
                for (ItemStack y : player) {
                    if (y != null && y.getType().equals(Material.MUSIC_DISC_BLOCKS) && y.hasItemMeta()) {
                        ItemMeta imy = y.getItemMeta();
                        if (imy.hasLore()) {
                            player_has.add(imy.getLore().get(0));
                        }
                    }
                }
                Inventory inv = plugin.getServer().createInventory(p, 54);
                inv.setContents(areas);
                ResultSetAreas rsa = new ResultSetAreas(plugin, null, true, false);
                int count = 0;
                if (rsa.resultSet()) {
                    // cycle through areas
                    for (Area map : rsa.getData()) {
                        String name = map.getAreaName();
                        if ((!player_has.contains(name) && TARDISPermission.hasPermission(p, "tardis.area." + name)) || (!player_has.contains(name) && TARDISPermission.hasPermission(p, "tardis.area.*"))) {
                            // add new area if there is room
                            int empty = getNextEmptySlot(inv);
                            if (empty != -1) {
                                ItemStack is = new ItemStack(Material.MUSIC_DISC_BLOCKS, 1);
                                ItemMeta im = is.getItemMeta();
                                im.setDisplayName("Area Storage Disk");
                                List<String> lore = new ArrayList<>();
                                lore.add(name);
                                lore.add(map.getWorld());
                                im.setLore(lore);
                                im.setCustomModelData(10000001);
                                is.setItemMeta(im);
                                inv.setItem(empty, is);
                                count++;
                            }
                        }
                    }
                }
                // return the serialized string
                if (count > 0) {
                    return TARDISSerializeInventory.itemStacksToString(inv.getContents());
                } else {
                    return serilized_areas;
                }
            } catch (IOException ex) {
                plugin.debug("Could not get NEW Area Disk Inventory: " + ex);
            }
        }
        return serialized;
    }

    /**
     * Finds the first empty slot greater than 27.
     *
     * @param inv the inventory to search
     * @return the empty slot number or -1 if not found
     */
    int getNextEmptySlot(Inventory inv) {
        for (int i = 27; i < 54; i++) {
            if (inv.getItem(i) == null || inv.getItem(i).getType().isAir()) {
                return i;
            }
        }
        return -1;
    }
}
