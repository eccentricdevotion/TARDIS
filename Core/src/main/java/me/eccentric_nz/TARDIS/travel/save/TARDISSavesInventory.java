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
package me.eccentric_nz.TARDIS.travel.save;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.custommodels.GUISaves;
import me.eccentric_nz.TARDIS.database.data.Destination;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetDestinationsByPlanet;
import me.eccentric_nz.TARDIS.enumeration.WorldManager;
import me.eccentric_nz.TARDIS.planets.TARDISAliasResolver;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * The "Hollywood" sign was among the Earth cultural items the Threshold stole and moved to the town of Wormwood on the
 * Moon. The moon was later destroyed; the sign likely was also.
 *
 * @author eccentric_nz
 */
public class TARDISSavesInventory {

    private final TARDIS plugin;
    private final ItemStack[] saves;
    private final List<Integer> slots = new LinkedList<>(List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44));
    private final int id;
    private final String world;

    public TARDISSavesInventory(TARDIS plugin, int id, String world) {
        this.plugin = plugin;
        this.id = id;
        this.world = world;
        saves = getItemStack();
    }

    /**
     * Constructs an inventory for the Save Sign GUI.
     *
     * @return an Array of itemStacks (an inventory)
     */
    private ItemStack[] getItemStack() {
        ItemStack[] stack = new ItemStack[54];
        HashMap<Integer, ItemStack> dests = new HashMap<>();
        // saved destinations
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        where.put("world", world);
        ResultSetDestinationsByPlanet rsd = new ResultSetDestinationsByPlanet(plugin, where);
        int i = 0;
        int highest = -1;
        List<Destination> data;
        if (rsd.resultSet()) {
            data = rsd.getData();
            int count = 0;
            // cycle through saves
            for (Destination map : data) {
                if (map.getType() == 0) {
                    count++;
                    if (count > 45) {
                        break;
                    }
                    int slot;
                    if (map.getSlot() != -1) {
                        slot = map.getSlot();
                        if (slot > highest) {
                            highest = slot;
                        }
                    } else {
                        slot = slots.getFirst();
                    }
                    slots.remove(Integer.valueOf(slot));
                    if (slot < 45) {
                        Material material;
                        if (map.getIcon().isEmpty()) {
                            material = TARDISConstants.GUI_IDS.get(i);
                        } else {
                            try {
                                material = Material.valueOf(map.getIcon());
                            } catch (IllegalArgumentException e) {
                                material = TARDISConstants.GUI_IDS.get(i);
                            }
                        }
                        ItemStack is = new ItemStack(material, 1);
                        ItemMeta im = is.getItemMeta();
                        im.setDisplayName(map.getDest_name());
                        List<String> lore = new ArrayList<>();
                        String world = (!plugin.getPlanetsConfig().getBoolean("planets." + map.getWorld() + ".enabled") && plugin.getWorldManager().equals(WorldManager.MULTIVERSE)) ? plugin.getMVHelper().getAlias(map.getWorld()) : TARDISAliasResolver.getWorldAlias(map.getWorld());
                        lore.add(world);
                        lore.add("" + map.getX());
                        lore.add("" + map.getY());
                        lore.add("" + map.getZ());
                        lore.add(map.getDirection());
                        lore.add("" + map.isSubmarine());
                        if (!map.getPreset().isEmpty()) {
                            lore.add(map.getPreset());
                        }
                        im.setLore(lore);
                        is.setItemMeta(im);
                        dests.put(slot, is);
                        i++;
                    }
                }
            }
            for (int s = 0; s < 45; s++) {
                stack[s] = dests.getOrDefault(s, null);
            }
        }
        // add button to allow rearranging saves
        ItemStack tool = new ItemStack(GUISaves.REARRANGE_SAVES.material(), 1);
        ItemMeta rearrange = tool.getItemMeta();
        rearrange.setDisplayName("Rearrange saves");
        rearrange.setItemModel(GUISaves.REARRANGE_SAVES.key());
        tool.setItemMeta(rearrange);
        // add button to allow deleting saves
        ItemStack bucket = new ItemStack(GUISaves.DELETE_SAVE.material(), 1);
        ItemMeta delete = bucket.getItemMeta();
        delete.setDisplayName("Delete save");
        delete.setItemModel(GUISaves.DELETE_SAVE.key());
        bucket.setItemMeta(delete);
        // add button to go back to planets
        ItemStack planet = new ItemStack(GUISaves.BACK_TO_PLANETS.material(), 1);
        ItemMeta map = planet.getItemMeta();
        map.setDisplayName("Back to Dimension Map");
        map.setItemModel(GUISaves.BACK_TO_PLANETS.key());
        planet.setItemMeta(map);
        for (int m = 45; m < 54; m++) {
            switch (m) {
                case 45 -> stack[m] = tool;
                case 47 -> stack[m] = bucket;
                case 53 -> stack[m] = planet;
                default -> stack[m] = null;
            }
        }
        return stack;
    }

    public ItemStack[] getSaves() {
        return saves;
    }
}
