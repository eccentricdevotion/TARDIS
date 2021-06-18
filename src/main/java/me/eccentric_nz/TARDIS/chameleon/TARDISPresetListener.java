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
package me.eccentric_nz.tardis.chameleon;

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.database.data.Tardis;
import me.eccentric_nz.tardis.database.resultset.ResultSetControls;
import me.eccentric_nz.tardis.database.resultset.ResultSetTardis;
import me.eccentric_nz.tardis.database.resultset.ResultSetTravellers;
import me.eccentric_nz.tardis.enumeration.Control;
import me.eccentric_nz.tardis.enumeration.Preset;
import me.eccentric_nz.tardis.listeners.TardisMenuListener;
import me.eccentric_nz.tardis.messaging.TardisMessage;
import me.eccentric_nz.tardis.utility.TardisStaticUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author eccentric_nz
 */
public class TardisPresetListener extends TardisMenuListener implements Listener {

    private final TardisPlugin plugin;

    public TardisPresetListener(TardisPlugin plugin) {
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
    public void onChameleonPresetClick(InventoryClickEvent event) {
        InventoryView view = event.getView();
        String name = view.getTitle();
        if (name.equals(ChatColor.DARK_RED + "Chameleon Presets")) {
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
                        int id = rst.getTardisId();
                        HashMap<String, Object> where = new HashMap<>();
                        where.put("tardis_id", id);
                        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
                        if (rs.resultSet()) {
                            Tardis tardis = rs.getTardis();
                            // set the Chameleon Circuit sign(s)
                            HashMap<String, Object> wherec = new HashMap<>();
                            wherec.put("tardis_id", id);
                            wherec.put("type", Control.CHAMELEON.getId());
                            ResultSetControls rsc = new ResultSetControls(plugin, wherec, true);
                            boolean hasSign = rsc.resultSet();
                            HashMap<String, Object> wheref = new HashMap<>();
                            wheref.put("tardis_id", id);
                            wheref.put("type", Control.FRAME.getId());
                            ResultSetControls rsf = new ResultSetControls(plugin, wheref, true);
                            boolean hasFrame = rsf.resultSet();
                            // set the Chameleon Circuit sign(s)
                            HashMap<String, Object> set = new HashMap<>();
                            switch (slot) {
                                case 47:
                                case 49:
                                case 50:
                                    // do nothing
                                    break;
                                case 48:
                                    // custom
                                    set.put("chameleon_preset", "CUSTOM");
                                    if (hasSign) {
                                        updateChameleonSign(rsf.getData(), "CUSTOM", player);
                                    }
                                    if (hasFrame) {
                                        new TardisChameleonFrame(plugin).updateChameleonFrame(id, Preset.CUSTOM, rsf.getLocation());
                                    }
                                    TardisMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Server's Custom");
                                    break;
                                case 51:
                                    // return to Chameleon Circuit GUI
                                    close(player);
                                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                                        ItemStack[] stacks = new TardisChameleonInventory(plugin, tardis.getAdaption(), tardis.getPreset()).getMenu();
                                        Inventory gui = plugin.getServer().createInventory(player, 27, ChatColor.DARK_RED + "Chameleon Circuit");
                                        gui.setContents(stacks);
                                        player.openInventory(gui);
                                    }, 2L);
                                    break;
                                case 52:
                                    // go to page two (coloured police boxes)
                                    close(player);
                                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                                        ItemStack[] boxes = new TardisPoliceBoxInventory(plugin, player).getBoxes();
                                        Inventory gui = plugin.getServer().createInventory(player, 27, ChatColor.DARK_RED + "Chameleon Police Boxes");
                                        gui.setContents(boxes);
                                        player.openInventory(gui);
                                    }, 2L);
                                    break;
                                case 53:
                                    close(player);
                                    break;
                                default:
                                    Preset selected = Preset.getPresetBySlot(slot);
                                    set.put("chameleon_preset", selected.toString());
                                    if (hasSign) {
                                        updateChameleonSign(rsf.getData(), selected.toString(), player);
                                    }
                                    if (hasFrame) {
                                        new TardisChameleonFrame(plugin).updateChameleonFrame(id, selected, rsf.getLocation());
                                    }
                                    TardisMessage.send(player, "CHAM_SET", ChatColor.AQUA + selected.getDisplayName());
                                    break;
                            }
                            if (set.size() > 0) {
                                set.put("adapti_on", 0);
                                HashMap<String, Object> wheret = new HashMap<>();
                                wheret.put("tardis_id", id);
                                plugin.getQueryFactory().doUpdate("tardis", set, wheret);
                            }
                        }
                    }
                }
            }
        }
    }

    private void updateChameleonSign(ArrayList<HashMap<String, String>> map, String preset, Player player) {
        for (HashMap<String, String> entry : map) {
            TardisStaticUtils.setSign(entry.get("location"), 3, preset, player);
        }
    }
}
