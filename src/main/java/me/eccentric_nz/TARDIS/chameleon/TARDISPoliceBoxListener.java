/*
 * Copyright (C) 2020 eccentric_nz
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

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.enumeration.PRESET;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

/**
 * @author eccentric_nz
 */
public class TARDISPoliceBoxListener extends TARDISMenuListener implements Listener {

    private final TARDIS plugin;

    public TARDISPoliceBoxListener(TARDIS plugin) {
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
    public void onChameleonPoliceBoxClick(InventoryClickEvent event) {
        InventoryView view = event.getView();
        String name = view.getTitle();
        if (name.equals(ChatColor.DARK_RED + "Chameleon Police Boxes")) {
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
                            Tardis tardis = rs.getTardis();
                            String last_line = TARDISStaticUtils.getLastLine(tardis.getChameleon());
                            String preset = tardis.getPreset().toString();
                            HashMap<String, Object> set = new HashMap<>();
                            HashMap<String, Object> wherec = new HashMap<>();
                            wherec.put("tardis_id", id);
                            TARDISChameleonFrame tcf = new TARDISChameleonFrame(plugin);
                            switch (slot) {
                                case 0:
                                    // blue
                                    if (!last_line.equals("BLUE")) {
                                        set.put("chameleon_preset", "POLICE_BOX_BLUE");
                                        TARDISStaticUtils.setSign(tardis.getChameleon(), 3, "BLUE", player);
                                        tcf.updateChameleonFrame(id, PRESET.POLICE_BOX_BLUE);
                                    }
                                    TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Blue Police Box");
                                    break;
                                case 1:
                                    // white
                                    if (!last_line.equals("WHITE")) {
                                        set.put("chameleon_preset", "POLICE_BOX_WHITE");
                                        TARDISStaticUtils.setSign(tardis.getChameleon(), 3, "WHITE", player);
                                        tcf.updateChameleonFrame(id, PRESET.POLICE_BOX_WHITE);
                                    }
                                    TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "White Police Box");
                                    break;
                                case 2:
                                    // orange
                                    if (!last_line.equals("ORANGE")) {
                                        set.put("chameleon_preset", "POLICE_BOX_ORANGE");
                                        TARDISStaticUtils.setSign(tardis.getChameleon(), 3, "ORANGE", player);
                                        tcf.updateChameleonFrame(id, PRESET.POLICE_BOX_ORANGE);
                                    }
                                    TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Orange Police Box");
                                    break;
                                case 3:
                                    // magenta
                                    if (!last_line.equals("MAGENTA")) {
                                        set.put("chameleon_preset", "POLICE_BOX_MAGENTA");
                                        TARDISStaticUtils.setSign(tardis.getChameleon(), 3, "MAGENTA", player);
                                        tcf.updateChameleonFrame(id, PRESET.POLICE_BOX_MAGENTA);
                                    }
                                    TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Magenta Police Box");
                                    break;
                                case 4:
                                    // light blue
                                    if (!last_line.equals("LIGHTBLUE")) {
                                        set.put("chameleon_preset", "POLICE_BOX_LIGHT_BLUE");
                                        TARDISStaticUtils.setSign(tardis.getChameleon(), 3, "LIGHTBLUE", player);
                                        tcf.updateChameleonFrame(id, PRESET.POLICE_BOX_LIGHT_BLUE);
                                    }
                                    TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Light Blue Police Box");
                                    break;
                                case 5:
                                    // yellow
                                    if (!last_line.equals("YELLOW")) {
                                        set.put("chameleon_preset", "POLICE_BOX_YELLOW");
                                        TARDISStaticUtils.setSign(tardis.getChameleon(), 3, "YELLOW", player);
                                        tcf.updateChameleonFrame(id, PRESET.POLICE_BOX_YELLOW);
                                    }
                                    TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Yellow Police Box");
                                    break;
                                case 6:
                                    // lime
                                    if (!last_line.equals("LIME")) {
                                        set.put("chameleon_preset", "POLICE_BOX_LIME");
                                        TARDISStaticUtils.setSign(tardis.getChameleon(), 3, "LIME", player);
                                        tcf.updateChameleonFrame(id, PRESET.POLICE_BOX_LIME);
                                    }
                                    TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Lime Police Box");
                                    break;
                                case 7:
                                    // pink
                                    if (!last_line.equals("PINK")) {
                                        set.put("chameleon_preset", "POLICE_BOX_PINK");
                                        TARDISStaticUtils.setSign(tardis.getChameleon(), 3, "PINK", player);
                                        tcf.updateChameleonFrame(id, PRESET.POLICE_BOX_PINK);
                                    }
                                    TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Pink Police Box");
                                    break;
                                case 8:
                                    // gray
                                    if (!last_line.equals("GRAY")) {
                                        set.put("chameleon_preset", "POLICE_BOX_GRAY");
                                        TARDISStaticUtils.setSign(tardis.getChameleon(), 3, "GRAY", player);
                                        tcf.updateChameleonFrame(id, PRESET.POLICE_BOX_GRAY);
                                    }
                                    TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Gray Police Box");
                                    break;
                                case 9:
                                    // light gray
                                    if (!last_line.equals("LIGHTGRAY")) {
                                        set.put("chameleon_preset", "POLICE_BOX_LIGHT_GRAY");
                                        TARDISStaticUtils.setSign(tardis.getChameleon(), 3, "LIGHTGRAY", player);
                                        tcf.updateChameleonFrame(id, PRESET.POLICE_BOX_LIGHT_GRAY);
                                    }
                                    TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Light Gray Police Box");
                                    break;
                                case 10:
                                    // cyan
                                    if (!last_line.equals("CYAN")) {
                                        set.put("chameleon_preset", "POLICE_BOX_CYAN");
                                        TARDISStaticUtils.setSign(tardis.getChameleon(), 3, "CYAN", player);
                                        tcf.updateChameleonFrame(id, PRESET.POLICE_BOX_CYAN);
                                    }
                                    TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Cyan Police Box");
                                    break;
                                case 11:
                                    // purple
                                    if (!last_line.equals("PURPLE")) {
                                        set.put("chameleon_preset", "POLICE_BOX_PURPLE");
                                        TARDISStaticUtils.setSign(tardis.getChameleon(), 3, "PURPLE", player);
                                        tcf.updateChameleonFrame(id, PRESET.POLICE_BOX_PURPLE);
                                    }
                                    TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Purple Police Box");
                                    break;
                                case 12:
                                    // brown
                                    if (!last_line.equals("BROWN")) {
                                        set.put("chameleon_preset", "POLICE_BOX_BROWN");
                                        TARDISStaticUtils.setSign(tardis.getChameleon(), 3, "BROWN", player);
                                        tcf.updateChameleonFrame(id, PRESET.POLICE_BOX_BROWN);
                                    }
                                    TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Brown Police Box");
                                    break;
                                case 13:
                                    // green
                                    if (!last_line.equals("GREEN")) {
                                        set.put("chameleon_preset", "POLICE_BOX_GREEN");
                                        TARDISStaticUtils.setSign(tardis.getChameleon(), 3, "GREEN", player);
                                        tcf.updateChameleonFrame(id, PRESET.POLICE_BOX_GREEN);
                                    }
                                    TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Green Police Box");
                                    break;
                                case 14:
                                    // red
                                    if (!last_line.equals("RED")) {
                                        set.put("chameleon_preset", "POLICE_BOX_RED");
                                        TARDISStaticUtils.setSign(tardis.getChameleon(), 3, "RED", player);
                                        tcf.updateChameleonFrame(id, PRESET.POLICE_BOX_RED);
                                    }
                                    TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Red Police Box");
                                    break;
                                case 15:
                                    // black
                                    if (!last_line.equals("BLACK")) {
                                        set.put("chameleon_preset", "POLICE_BOX_BLACK");
                                        TARDISStaticUtils.setSign(tardis.getChameleon(), 3, "BLACK", player);
                                        tcf.updateChameleonFrame(id, PRESET.POLICE_BOX_BLACK);
                                    }
                                    TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Black Police Box");
                                    break;
                                case 24:
                                    // go to page one (regular presets)
                                    close(player);
                                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                                        TARDISPresetInventory tpi = new TARDISPresetInventory(plugin);
                                        ItemStack[] items = tpi.getPresets();
                                        Inventory presetinv = plugin.getServer().createInventory(player, 54, ChatColor.DARK_RED + "Chameleon Presets");
                                        presetinv.setContents(items);
                                        player.openInventory(presetinv);
                                    }, 2L);
                                    break;
                                case 25:
                                    // return to Chameleon Circuit GUI
                                    close(player);
                                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                                        ItemStack[] chameleon = new TARDISChameleonInventory(plugin, tardis.getAdaption(), tardis.getPreset()).getMenu();
                                        Inventory gui = plugin.getServer().createInventory(player, 27, ChatColor.DARK_RED + "Chameleon Circuit");
                                        gui.setContents(chameleon);
                                        player.openInventory(gui);
                                    }, 2L);
                                    break;
                                default:
                                    close(player);
                                    break;
                            }
                            if (set.size() > 0) {
                                set.put("adapti_on", 0);
                                set.put("chameleon_demat", preset);
                                plugin.getQueryFactory().doUpdate("tardis", set, wherec);
                            }
                        }
                    }
                }
            }
        }
    }
}
