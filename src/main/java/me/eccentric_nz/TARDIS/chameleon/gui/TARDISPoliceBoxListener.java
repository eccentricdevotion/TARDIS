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
package me.eccentric_nz.TARDIS.chameleon.gui;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.advanced.DamageUtility;
import me.eccentric_nz.TARDIS.chameleon.utils.TARDISChameleonFrame;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetControls;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.ChameleonPreset;
import me.eccentric_nz.TARDIS.enumeration.Control;
import me.eccentric_nz.TARDIS.enumeration.DiskCircuit;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author eccentric_nz
 */
public class TARDISPoliceBoxListener extends TARDISMenuListener {

    private final TARDIS plugin;

    public TARDISPoliceBoxListener(TARDIS plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    /**
     * Listens for player clicking inside an inventory. If the inventory is a
     * TARDIS GUI, then the click is processed accordingly.
     *
     * @param event a player clicking an inventory slot
     */
    @EventHandler(ignoreCancelled = true)
    public void onChameleonPoliceBoxClick(InventoryClickEvent event) {
        if (!(event.getInventory().getHolder(false) instanceof TARDISPoliceBoxInventory)) {
            return;
        }
        event.setCancelled(true);
        int slot = event.getRawSlot();
        Player player = (Player) event.getWhoClicked();
        if (slot < 0 || slot > 53) {
            return;
        }
        ItemStack is = event.getView().getItem(slot);
        if (is == null) {
            return;
        }
        // get the TARDIS the player is in
        HashMap<String, Object> wheres = new HashMap<>();
        wheres.put("uuid", player.getUniqueId().toString());
        ResultSetTravellers rst = new ResultSetTravellers(plugin, wheres, false);
        if (!rst.resultSet()) {
            return;
        }
        int id = rst.getTardis_id();
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
        if (!rs.resultSet()) {
            return;
        }
        Tardis tardis = rs.getTardis();
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
            case 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21 -> {
                // armour stand preset
                ChameleonPreset selected = ChameleonPreset.getItemFramePresetBySlot(slot);
                set.put("chameleon_preset", selected.toString());
                if (hasSign) {
                    updateChameleonSign(rsc.getData(), selected.toString(), player);
                }
                if (hasFrame) {
                    new TARDISChameleonFrame().updateChameleonFrame(selected, rsf.getLocation());
                }
                plugin.getMessenger().sendInsertedColour(player, "CHAM_SET", selected.getDisplayName(), plugin);
                if (slot == 21) {
                    // any colour - open the colour picker
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () ->
                            player.openInventory(new TARDISColourPickerGUI(plugin).getInventory()), 2L);
                }
            }
            // go to custom presets
            case 48 -> plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () ->
                    player.openInventory(new TARDISCustomPresetInventory(plugin, player).getInventory()), 2L);
            // go to page one (regular presets)
            case 51 -> plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () ->
                    player.openInventory(new TARDISPresetInventory(plugin, player).getInventory()), 2L);
            // return to Chameleon Circuit GUI
            case 52 -> plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () ->
                    player.openInventory(new TARDISChameleonInventory(plugin, tardis.getAdaption(), tardis.getPreset(), tardis.getItemPreset()).getInventory()), 2L);
            case 53 -> close(player);
            default -> {
                // custom model exterior
                String custom = ComponentUtils.stripColour(is.getItemMeta().displayName());
                set.put("chameleon_preset", "ITEM:" + custom);
                if (hasSign) {
                    updateChameleonSign(rsc.getData(), custom, player);
                }
                if (hasFrame) {
                    new TARDISChameleonFrame().updateChameleonFrame(ChameleonPreset.ITEM, rsf.getLocation());
                }
                plugin.getMessenger().sendInsertedColour(player, "CHAM_SET", custom, plugin);
            }

        }
        if (!set.isEmpty()) {
            set.put("adapti_on", 0);
            HashMap<String, Object> wheret = new HashMap<>();
            wheret.put("tardis_id", id);
            plugin.getQueryFactory().doUpdate("tardis", set, wheret);
            // damage the circuit if configured
            DamageUtility.run(plugin, DiskCircuit.CHAMELEON, id, player);
        }
    }


    private void updateChameleonSign(ArrayList<HashMap<String, String>> map, String preset, Player player) {
        for (HashMap<String, String> entry : map) {
            TARDISStaticUtils.setSign(entry.get("location"), 3, preset, player);
        }
    }
}
