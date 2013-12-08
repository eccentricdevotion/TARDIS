/*
 * Copyright (C) 2013 eccentric_nz
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.rooms.TARDISWallsLookup;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author eccentric_nz
 */
public class TARDISCraftListener implements Listener {

    private final TARDIS plugin;
    private final List<Material> b = new ArrayList<Material>();
    private final List<Integer> c = new ArrayList<Integer>();
    private final List<Integer> l = new ArrayList<Integer>();
    private final HashMap<Material, String> t = new HashMap<Material, String>();
    private final List<Material> hasColour = new ArrayList<Material>();
    private final TARDISWallsLookup twl;

    public TARDISCraftListener(TARDIS plugin) {
        this.plugin = plugin;
        t.put(Material.IRON_BLOCK, "BUDGET"); // budget
        t.put(Material.GOLD_BLOCK, "BIGGER"); // bigger
        t.put(Material.DIAMOND_BLOCK, "DELUXE"); // deluxe
        t.put(Material.EMERALD_BLOCK, "ELEVENTH"); // eleventh
        t.put(Material.REDSTONE_BLOCK, "REDSTONE"); // redstone
        t.put(Material.COAL_BLOCK, "STEAMPUNK"); // steampunk
        t.put(Material.QUARTZ_BLOCK, "ARS"); // ARS
        t.put(Material.LAPIS_BLOCK, "TOM"); // tom baker
        t.put(Material.BOOKSHELF, "PLANK"); // plank
        t.put(Material.valueOf(this.plugin.getConfig().getString("custom_schematic_seed")), "CUSTOM"); // custom
        for (Integer i : plugin.getBlocksConfig().getIntegerList("chameleon_blocks")) {
            c.add(i);
        }
        for (Integer a : plugin.getBlocksConfig().getIntegerList("lamp_blocks")) {
            l.add(a);
        }
        hasColour.add(Material.WOOL);
        hasColour.add(Material.STAINED_CLAY);
        hasColour.add(Material.STAINED_GLASS);
        hasColour.add(Material.WOOD);
        hasColour.add(Material.LOG);
        hasColour.add(Material.LOG_2);
        twl = new TARDISWallsLookup(plugin);
    }

    /**
     * Places a configured TARDIS Seed block in the crafting result slot.
     *
     * @param event the player clicking the crafting result slot.
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onInteract(InventoryClickEvent event) {
        Inventory inv = event.getInventory();
        int slot = event.getRawSlot();
        if (inv.getType().equals(InventoryType.WORKBENCH) && slot == 0 && checkSlots(inv)) {

            // get the materials in crafting slots
            Material m5 = inv.getItem(5).getType(); // lamp
            //Material m6 = inv.getItem(6).getType(); // wall
            Material m7 = inv.getItem(7).getType(); // tardis type
            Material m8 = inv.getItem(8).getType(); // chameleon
            //Material m9 = inv.getItem(9).getType(); // floor
            ItemStack is = new ItemStack(m7, 1);
            ItemMeta im = is.getItemMeta();
            im.setDisplayName("§6TARDIS Seed Block");
            List<String> lore = new ArrayList<String>();
            lore.add(t.get(m7));
            lore.add("Walls: " + twl.wall_lookup.get(inv.getItem(6).getTypeId() + ":" + inv.getItem(6).getData().getData()));
            lore.add("Floors: " + twl.wall_lookup.get(inv.getItem(9).getTypeId() + ":" + inv.getItem(9).getData().getData()));
            // do some funky stuff to get data values for wool/stained glass & clay/wood/log/log_2
            if (hasColour.contains(m8)) {
                switch (m8) {
                    case WOOL:
                    case STAINED_CLAY:
                    case STAINED_GLASS:
                        lore.add("Chameleon block: " + DyeColor.getByWoolData(inv.getItem(8).getData().getData()) + " " + m8.toString());
                        break;
                    default:
                        lore.add("Chameleon block: " + plugin.utils.getWoodType(m8, inv.getItem(8).getData().getData()) + " " + m8.toString());
                }
            } else {
                lore.add("Chameleon block: " + m8.toString());
            }
            lore.add("Lamp: " + m5.toString());
            im.setLore(lore);
            is.setItemMeta(im);
            Player player = (Player) event.getWhoClicked();
            player.sendMessage("Valid seed block :)");
            inv.setItem(0, is);
        }
    }

    /**
     * Checks the craft inventory slots contain the correct materials to craft a
     * TARDIS Seed block.
     *
     * @param inv
     * @return
     */
    @SuppressWarnings("deprecation")
    private boolean checkSlots(Inventory inv) {
        for (int j = 1; j < 10; j++) {
            ItemStack is = inv.getItem(j);
            if (j == 1 || j > 3) {
                if (is == null) {
                    return false;
                }
                Material m = is.getType();
                int id = is.getTypeId();
                switch (j) {
                    case 1:
                        // must be a redstone torch
                        if (!m.equals(Material.REDSTONE_TORCH_ON)) {
                            return false;
                        }
                        break;
                    case 2:
                    case 3:
                        if (!m.equals(Material.AIR)) {
                            return false;
                        }
                        break;
                    case 4:
                        // must be lapis block
                        if (!m.equals(Material.LAPIS_BLOCK)) {
                            return false;
                        }
                        break;
                    case 5:
                        // must be a valid lamp block
                        if (!l.contains(id)) {
                            return false;
                        }
                        break;
                    case 7:
                        // must be a TARDIS block
                        if (!t.containsKey(m)) {
                            return false;
                        }
                        break;
                    case 8:
                        // must be a valid chameleon block
                        if (!c.contains(id)) {
                            return false;
                        }
                        break;
                    default:
                        // must be a valid wall / floor block
                        if (!twl.wall_lookup.containsKey(id + ":" + is.getData().getData())) {
                            return false;
                        }
                        break;
                }
            } else {
                if (is != null) {
                    return false;
                }
            }
        }
        return true;
    }
}
