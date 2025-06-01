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
package me.eccentric_nz.TARDIS.lights;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISCache;
import me.eccentric_nz.TARDIS.control.actions.LightSwitchAction;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetLightPrefs;
import me.eccentric_nz.TARDIS.desktop.TARDISWallsInventory;
import me.eccentric_nz.TARDIS.enumeration.TardisLight;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;

public class TARDISLightsGUIListener extends TARDISMenuListener {

    private final TARDIS plugin;

    public TARDISLightsGUIListener(TARDIS plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    /**
     * Listens for player clicking inside an inventory. If the inventory is a TARDIS GUI, then the click is processed
     * accordingly.
     *
     * @param event a player clicking an inventory slot
     */
    @EventHandler(ignoreCancelled = true)
    public void onLightMenuClick(InventoryClickEvent event) {
        InventoryView view = event.getView();
        if (!view.getTitle().equals(ChatColor.DARK_RED + "TARDIS Lights")) {
            return;
        }
        event.setCancelled(true);
        int slot = event.getRawSlot();
        Player player = (Player) event.getWhoClicked();
        if (slot >= 0 && slot < 54) {
            // get selection
            ItemStack is = view.getItem(slot);
            if (is != null) {
                // get TARDIS data
//                HashMap<String, Object> where = new HashMap<>();
//                where.put("uuid", player.getUniqueId().toString());
//                ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
//                if (rs.resultSet()) {
//                    Tardis data = rs.getTardis();
                Tardis tardis = TARDISCache.BY_UUID.get(player.getUniqueId());
                if (tardis != null) {
                    switch (slot) {
                        case 0, 27, 29, 34, 41, 43 -> {
                        }
                        // variable block menu
                        case 28 -> {
                            ItemStack[] blocks = new TARDISWallsInventory(plugin).getMenu();
                            Inventory variableBlocks = plugin.getServer().createInventory(player, 54, ChatColor.DARK_RED + "Variable Light Blocks");
                            variableBlocks.setContents(blocks);
                            player.openInventory(variableBlocks);
                        }
                        // change the lights
                        case 35 -> {
                            if (!plugin.getTrackerKeeper().getLightChangers().contains(player.getUniqueId())) {
                                // get light prefs
                                ResultSetLightPrefs rslp = new ResultSetLightPrefs(plugin);
                                if (rslp.fromID(tardis.getTardisId())) {
                                    // update lights in the ARS grid
                                    TARDISLightChanger changer = new TARDISLightChanger(plugin, rslp.getLight(), tardis.getChunk(), tardis.isLightsOn(), getMaterialFromSlot(view, 29), player);
                                    int task = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, changer, 2L, 100L);
                                    changer.setTaskID(task);
                                }
                            } else {
                                plugin.getMessenger().send(player, TardisModule.TARDIS, "LIGHT_CHANGE");
                            }
                            close(player);
                        }
                        // select light emitting block
                        case 42 -> {
                            ItemStack[] emitting = new TARDISLightEmittingInventory(plugin).getGUI();
                            Inventory emittingGUI = plugin.getServer().createInventory(player, 27, ChatColor.DARK_RED + "Light Emitting Blocks");
                            emittingGUI.setContents(emitting);
                            player.openInventory(emittingGUI);
                        }
                        // convert lights
                        case 44 -> {
                            if (!plugin.getTrackerKeeper().getLightChangers().contains(player.getUniqueId())) {
                                // get light prefs
                                ResultSetLightPrefs rslp = new ResultSetLightPrefs(plugin);
                                if (rslp.fromID(tardis.getTardisId())) {
                                    // get variable block
                                    Material variable = getMaterialFromSlot(view, 29);
                                    // get selected block
                                    Material emitting = getMaterialFromSlot(view, 43);
                                    new TARDISLightConverter(plugin).apply(rslp.getLight(), emitting, player, variable);
                                }
                            } else {
                                plugin.getMessenger().send(player, TardisModule.TARDIS, "LIGHT_CHANGE");
                            }
                        }
                        // light switch
                        case 45 -> {
                            if (plugin.getTrackerKeeper().getInSiegeMode().contains(tardis.getTardisId())) {
                                plugin.getMessenger().send(player, TardisModule.TARDIS, "SIEGE_NO_CONTROL");
                                return;
                            }
                            if (!tardis.isLightsOn() && plugin.getConfig().getBoolean("allow.power_down") && !tardis.isPoweredOn()) {
                                plugin.getMessenger().send(player, TardisModule.TARDIS, "POWER_DOWN");
                                return;
                            }
                            close(player);
                            new LightSwitchAction(plugin, tardis.getTardisId(), tardis.isLightsOn(), player, tardis.getSchematic().getLights()).flickSwitch();
                        }
                        // light levels
                        case 47 -> {
                            ItemStack[] levels = new TARDISLightLevelsInventory(plugin, tardis.getTardisId()).getGUI();
                            Inventory levelsGUI = plugin.getServer().createInventory(player, 54, ChatColor.DARK_RED + "TARDIS Light Levels");
                            levelsGUI.setContents(levels);
                            player.openInventory(levelsGUI);
                        }
                        // play animated light sequence
                        case 49 -> {
                            if (!plugin.getTrackerKeeper().getLightChangers().contains(player.getUniqueId())) {
                                new TARDISLightSequence(plugin, tardis.getTardisId(), player.getUniqueId()).play();
                            } else {
                                plugin.getMessenger().send(player, TardisModule.TARDIS, "LIGHT_CHANGE");
                            }
                            close(player);
                        }
                        // edit sequence
                        case 51 -> {
                            ItemStack[] edits = new TARDISLightSequenceInventory(plugin, tardis.getTardisId()).getGUI();
                            Inventory sequence = plugin.getServer().createInventory(player, 45, ChatColor.DARK_RED + "TARDIS Light Sequence");
                            sequence.setContents(edits);
                            player.openInventory(sequence);
                        }
                        // close the GUI
                        case 53 -> close(player);
                        // select and save light type
                        default -> {
                            ItemMeta im = is.getItemMeta();
                            // save preference
                            String light = TARDISStringUtils.toUnderscoredUppercase(im.getDisplayName());
                            HashMap<String, Object> set = new HashMap<>();
                            set.put("light", light);
                            HashMap<String, Object> wheret = new HashMap<>();
                            wheret.put("tardis_id", tardis.getTardisId());
                            plugin.getQueryFactory().doUpdate("light_prefs", set, wheret);
                            // also update the player prefs lights option
                            HashMap<String, Object> setpp = new HashMap<>();
                            setpp.put("lights", light);
                            HashMap<String, Object> wherepp = new HashMap<>();
                            wherepp.put("uuid", player.getUniqueId().toString());
                            plugin.getQueryFactory().doUpdate("player_prefs", setpp, wherepp);
                            // update 'current' lore of light choices
                            for (int l = 1; l <= TardisLight.values().length + 2; l++) {
                                ItemStack isl = view.getItem(l);
                                if (isl != null) {
                                    ItemMeta iml = isl.getItemMeta();
                                    if (!iml.getDisplayName().equals(im.getDisplayName())) {
                                        iml.setLore(null);
                                    } else {
                                        iml.setLore(List.of("Current light"));
                                    }
                                    isl.setItemMeta(iml);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private Material getMaterialFromSlot(InventoryView view, int slot) {
        ItemStack is = view.getItem(slot);
        return is.getType();
    }
}
