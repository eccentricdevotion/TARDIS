/*
 * Copyright (C) 2022 eccentric_nz
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
package me.eccentric_nz.TARDIS.chameleon;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.builders.TARDISShellBuilder;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetChameleon;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetControls;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.Adaption;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.PRESET;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author eccentric_nz
 */
public class TARDISShellLoaderListener extends TARDISMenuListener implements Listener {

    private final TARDIS plugin;
    private final List<Material> problemBlocks = new ArrayList<>();

    public TARDISShellLoaderListener(TARDIS plugin) {
        super(plugin);
        this.plugin = plugin;
        problemBlocks.add(Material.LEVER);
        problemBlocks.add(Material.REDSTONE_TORCH);
        problemBlocks.add(Material.REDSTONE_WALL_TORCH);
        problemBlocks.add(Material.TORCH);
        problemBlocks.add(Material.WALL_TORCH);
        problemBlocks.addAll(Tag.BUTTONS.getValues());
        problemBlocks.addAll(Tag.DOORS.getValues());
        problemBlocks.addAll(Tag.FLOWERS.getValues());
        problemBlocks.addAll(Tag.TRAPDOORS.getValues());
        problemBlocks.addAll(Tag.WALL_SIGNS.getValues());
    }

    /**
     * Listens for player clicking inside an inventory. If the inventory is a TARDIS GUI, then the click is processed
     * accordingly.
     *
     * @param event a player clicking an inventory slot
     */
    @EventHandler(ignoreCancelled = true)
    public void onShellLoaderClick(InventoryClickEvent event) {
        InventoryView view = event.getView();
        String name = view.getTitle();
        if (name.equals(ChatColor.DARK_RED + "TARDIS Shell Loader")) {
            event.setCancelled(true);
            int slot = event.getRawSlot();
            Player player = (Player) event.getWhoClicked();
            if (slot >= 0 && slot < 54) {
                ItemStack is = view.getItem(slot);
                if (is != null) {
                    // get the TARDIS the player is in
                    HashMap<String, Object> wheres = new HashMap<>();
                    wheres.put("uuid", player.getUniqueId().toString());
                    ResultSetTravellers rst = new ResultSetTravellers(plugin, wheres, false);
                    if (rst.resultSet()) {
                        int id = rst.getTardis_id();
                        HashMap<String, Object> where = new HashMap<>();
                        where.put("tardis_id", id);
                        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
                        if (rs.resultSet()) {
                            if (slot == 53) {
                                // close
                                close(player);
                            } else {
                                TARDISChameleonColumn chameleonColumn = null;
                                PRESET preset;
                                if (slot == 50) {
                                    // load current preset
                                    preset = rs.getTardis().getPreset();
                                    if (!rs.getTardis().getAdaption().equals(Adaption.OFF)) {
                                        // get the actual preset blocks being used
                                        chameleonColumn = TARDISShellScanner.scan(plugin, id, preset);
                                    } else {
                                        chameleonColumn = plugin.getPresets().getColumn(preset, COMPASS.EAST);
                                    }
                                } else if (slot == 51) {
                                    // load shell
                                    preset = PRESET.CONSTRUCT;
                                    // load saved construct
                                    HashMap<String, Object> wherec = new HashMap<>();
                                    wherec.put("tardis_id", id);
                                    ResultSetChameleon rsc = new ResultSetChameleon(plugin, wherec);
                                    if (rsc.resultSet()) {
                                        // convert to String[][] array
                                        String data = rsc.getData().get("blueprintData");
                                        if (data != null) {
                                            JsonArray json = JsonParser.parseString(data).getAsJsonArray();
                                            String[][] strings = new String[10][4];
                                            for (int i = 0; i < 10; i++) {
                                                JsonArray inner = json.get(i).getAsJsonArray();
                                                for (int j = 0; j < 4; j++) {
                                                    String block = inner.get(j).getAsString();
                                                    strings[i][j] = block;
                                                }
                                            }
                                            chameleonColumn = TARDISChameleonPreset.buildTARDISChameleonColumn(COMPASS.EAST, strings);
                                        }
                                    }
                                } else {
                                    // load preset
                                    preset = PRESET.getPresetBySlot(slot);
                                    chameleonColumn = plugin.getPresets().getColumn(preset, COMPASS.EAST);
                                }
                                if (chameleonColumn != null) {
                                    // get the Shell room button
                                    HashMap<String, Object> whereb = new HashMap<>();
                                    whereb.put("tardis_id", id);
                                    whereb.put("type", 25);
                                    ResultSetControls rsc = new ResultSetControls(plugin, whereb, false);
                                    if (rsc.resultSet()) {
                                        // get the start location
                                        Location button = TARDISStaticLocationGetters.getLocationFromBukkitString(rsc.getLocation());
                                        World w = button.getWorld();
                                        int fx = button.getBlockX() + 2;
                                        int fy = button.getBlockY() + 1;
                                        int fz = button.getBlockZ() - 1;
                                        // always clear the platform first
                                        // do problem blocks first
                                        for (int c = 0; c < 10; c++) {
                                            for (int y = fy; y < fy + 4; y++) {
                                                Block block = w.getBlockAt(fx + TARDISShellRoomConstructor.orderx[c], y, fz + TARDISShellRoomConstructor.orderz[c]);
                                                if (problemBlocks.contains(block.getType())) {
                                                    block.setBlockData(TARDISConstants.AIR);
                                                }
                                            }
                                        }
                                        // set to AIR
                                        for (int c = 0; c < 10; c++) {
                                            for (int y = fy; y < fy + 4; y++) {
                                                w.getBlockAt(fx + TARDISShellRoomConstructor.orderx[c], y, fz + TARDISShellRoomConstructor.orderz[c]).setBlockData(TARDISConstants.AIR);
                                            }
                                        }
                                        // build shell in the shell room
                                        Location centre = button.clone().add(3, 1, 0);
                                        new TARDISShellBuilder(plugin, preset, chameleonColumn, centre).buildPreset();
                                        // close
                                        close(player);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
