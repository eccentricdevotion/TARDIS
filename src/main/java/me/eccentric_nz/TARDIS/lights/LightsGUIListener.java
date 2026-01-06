/*
 * Copyright (C) 2026 eccentric_nz
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
import me.eccentric_nz.TARDIS.control.actions.LightSwitchAction;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetLightPrefs;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.desktop.WallsInventory;
import me.eccentric_nz.TARDIS.enumeration.TardisLight;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;

public class LightsGUIListener extends TARDISMenuListener {

    private final TARDIS plugin;

    public LightsGUIListener(TARDIS plugin) {
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
        if (!(event.getInventory().getHolder(false) instanceof LightsInventory)) {
            return;
        }
        event.setCancelled(true);
        int slot = event.getRawSlot();
        Player player = (Player) event.getWhoClicked();
        if (slot >= 0 && slot < 54) {
            // get selection
            InventoryView view = event.getView();
            ItemStack is = view.getItem(slot);
            if (is != null) {
                // get TARDIS data
                HashMap<String, Object> where = new HashMap<>();
                where.put("uuid", player.getUniqueId().toString());
                ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
                if (rs.resultSet()) {
                    Tardis tardis = rs.getTardis();
                    switch (slot) {
                        case 0, 27, 29, 34, 41, 43 -> { }
                        // variable block menu
                        case 28 ->
                                player.openInventory(new WallsInventory(plugin, "Variable Light Blocks").getInventory());
                        // change the lights
                        case 35 -> {
                            if (!plugin.getTrackerKeeper().getLightChangers().contains(player.getUniqueId())) {
                                // get light prefs
                                ResultSetLightPrefs rslp = new ResultSetLightPrefs(plugin);
                                if (rslp.fromID(tardis.getTardisId())) {
                                    // update lights in the ARS grid
                                    LightChanger changer = new LightChanger(plugin, rslp.getLight(), tardis.getChunk(), tardis.isLightsOn(), getMaterialFromSlot(view, 29), player);
                                    int task = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, changer, 2L, 100L);
                                    changer.setTaskID(task);
                                }
                            } else {
                                plugin.getMessenger().send(player, TardisModule.TARDIS, "LIGHT_CHANGE");
                            }
                            close(player);
                        }
                        // select light emitting block
                        case 42 -> player.openInventory(new LightEmittingInventory(plugin).getInventory());
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
                                    new LightConverter(plugin).apply(rslp.getLight(), emitting, player, variable);
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
                        case 47 -> player.openInventory(new LightLevelsInventory(plugin, tardis.getTardisId()).getInventory());
                        // play animated light sequence
                        case 49 -> {
                            if (!plugin.getTrackerKeeper().getLightChangers().contains(player.getUniqueId())) {
                                new LightSequence(plugin, tardis.getTardisId(), player.getUniqueId()).play();
                            } else {
                                plugin.getMessenger().send(player, TardisModule.TARDIS, "LIGHT_CHANGE");
                            }
                            close(player);
                        }
                        // edit sequence
                        case 51 -> player.openInventory(new LightSequenceInventory(plugin, tardis.getTardisId()).getInventory());
                        // close the GUI
                        case 53 -> close(player);
                        // select and save light type
                        default -> {
                            ItemMeta im = is.getItemMeta();
                            // save preference
                            String light = TARDISStringUtils.toUnderscoredUppercase(ComponentUtils.stripColour(im.displayName()));
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
                                    if (!iml.displayName().equals(im.displayName())) {
                                        iml.lore(null);
                                    } else {
                                        iml.lore(List.of(Component.text("Current light")));
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
