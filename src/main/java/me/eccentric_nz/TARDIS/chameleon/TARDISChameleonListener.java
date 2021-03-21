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
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitChecker;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitDamager;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetControls;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.*;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author eccentric_nz
 */
public class TARDISChameleonListener extends TARDISMenuListener implements Listener {

    private final TARDIS plugin;

    public TARDISChameleonListener(TARDIS plugin) {
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
    public void onChameleonMenuClick(InventoryClickEvent event) {
        InventoryView view = event.getView();
        String name = view.getTitle();
        if (name.equals(ChatColor.DARK_RED + "Chameleon Circuit")) {
            event.setCancelled(true);
            int slot = event.getRawSlot();
            Player player = (Player) event.getWhoClicked();
            if (slot >= 0 && slot < 27) {
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
                            Adaption adapt = tardis.getAdaption();
                            HashMap<String, Object> set = new HashMap<>();
                            HashMap<String, Object> wherec = new HashMap<>();
                            wherec.put("tardis_id", id);
                            TARDISChameleonFrame tcf = new TARDISChameleonFrame(plugin);
                            String chameleon = "";
                            // set the Chameleon Circuit sign(s)
                            HashMap<String, Object> whereh = new HashMap<>();
                            whereh.put("tardis_id", id);
                            whereh.put("type", 31);
                            ResultSetControls rsc = new ResultSetControls(plugin, whereh, true);
                            boolean hasChameleonSign = false;
                            if (rsc.resultSet()) {
                                hasChameleonSign = true;
                                for (HashMap<String, String> map : rsc.getData()) {
                                    chameleon = map.get("location");
                                }
                            }
                            switch (slot) {
                                case 0:
                                    player.performCommand("tardis rebuild");
                                    close(player);
                                    // damage the circuit if configured
                                    if (plugin.getConfig().getBoolean("circuits.damage") && !plugin.getDifficulty().equals(Difficulty.EASY) && plugin.getConfig().getInt("circuits.uses.chameleon") > 0) {
                                        TARDISCircuitChecker tcc = new TARDISCircuitChecker(plugin, id);
                                        tcc.getCircuits();
                                        // decrement uses
                                        int uses_left = tcc.getChameleonUses();
                                        new TARDISCircuitDamager(plugin, DiskCircuit.CHAMELEON, uses_left, id, player).damage();
                                    }
                                    break;
                                case 11:
                                    // factory
                                    set.put("adapti_on", 0);
                                    ItemStack frb = view.getItem(20);
                                    ItemMeta fact = frb.getItemMeta();
                                    String ory = fact.getDisplayName();
                                    if (ory.equals(ChatColor.GREEN + plugin.getLanguage().getString("SET_ON"))) {
                                        set.put("chameleon_preset", "FACTORY");
                                        toggleOthers(ChameleonOption.FACTORY, view);
                                        updateChameleonSign(hasChameleonSign, rsc.getData(), "FACTORY", player);
                                        tcf.updateChameleonFrame(id, PRESET.FACTORY);
                                        TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Factory Fresh");
                                    } else {
                                        set.put("chameleon_preset", "NEW");
                                        toggleOthers(ChameleonOption.PRESET, view);
                                        tcf.updateChameleonFrame(id, PRESET.NEW);
                                        setDefault(view, player, chameleon);
                                    }
                                    break;
                                case 12:
                                    // cycle biome adaption
                                    int ca = adapt.ordinal() + 1;
                                    if (ca >= Adaption.values().length) {
                                        ca = 0;
                                    }
                                    Adaption a = Adaption.values()[ca];
                                    if (a.equals(Adaption.OFF)) {
                                        // default to Blue Police Box
                                        set.put("chameleon_preset", "NEW");
                                        toggleOthers(ChameleonOption.PRESET, view);
                                        tcf.updateChameleonFrame(id, PRESET.NEW);
                                        setDefault(view, player, chameleon);
                                    } else {
                                        toggleOthers(ChameleonOption.ADAPTIVE, view);
                                        PRESET adaptive = (tardis.getPreset().equals(PRESET.SUBMERGED)) ? PRESET.SUBMERGED : PRESET.FACTORY;
                                        tcf.updateChameleonFrame(id, adaptive);
                                        set.put("chameleon_preset", adaptive.toString());
                                    }
                                    set.put("adapti_on", ca);
                                    ItemStack arb = view.getItem(21);
                                    ItemMeta bio = arb.getItemMeta();
                                    bio.setDisplayName(a.getColour() + a.toString());
                                    arb.setItemMeta(bio);
                                    break;
                                case 13:
                                    // Invisibility
                                    set.put("adapti_on", 0);
                                    ItemStack irb = view.getItem(22);
                                    ItemMeta invis = irb.getItemMeta();
                                    String ible = invis.getDisplayName();
                                    if (ible.equals(ChatColor.RED + plugin.getLanguage().getString("SET_OFF"))) {
                                        // check they have an Invisibility Circuit
                                        TARDISCircuitChecker tcc = new TARDISCircuitChecker(plugin, id);
                                        tcc.getCircuits();
                                        if (!plugin.getDifficulty().equals(Difficulty.EASY)) {
                                            if (!plugin.getUtils().inGracePeriod(player, false) && !tcc.hasInvisibility()) {
                                                close(player);
                                                TARDISMessage.send(player, "INVISIBILITY_MISSING");
                                                break;
                                            }
                                        }
                                        if (plugin.getConfig().getBoolean("circuits.damage") && !plugin.getDifficulty().equals(Difficulty.EASY) && plugin.getConfig().getInt("circuits.uses.invisibility") > 0) {
                                            // decrement uses
                                            int uses_left = tcc.getInvisibilityUses();
                                            new TARDISCircuitDamager(plugin, DiskCircuit.INVISIBILITY, uses_left, id, player).damage();
                                        }
                                        toggleOthers(ChameleonOption.INVISIBLE, view);
                                        set.put("chameleon_preset", "INVISIBLE");
                                        updateChameleonSign(hasChameleonSign, rsc.getData(), "INVISIBLE", player);
                                        tcf.updateChameleonFrame(id, PRESET.INVISIBLE);
                                        TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Invisibility");
                                    } else {
                                        toggleOthers(ChameleonOption.PRESET, view);
                                        // default to Blue Police Box
                                        set.put("chameleon_preset", "NEW");
                                        tcf.updateChameleonFrame(id, PRESET.NEW);
                                        setDefault(view, player, chameleon);
                                    }
                                    break;
                                case 14:
                                    // presets
                                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                                        TARDISPresetInventory tpi = new TARDISPresetInventory(plugin, player);
                                        ItemStack[] items = tpi.getPresets();
                                        Inventory presetinv = plugin.getServer().createInventory(player, 54, ChatColor.DARK_RED + "Chameleon Presets");
                                        presetinv.setContents(items);
                                        player.openInventory(presetinv);
                                    }, 2L);
                                    break;
                                case 15:
                                    // constructor GUI
                                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                                        TARDISChameleonConstructorGUI tci = new TARDISChameleonConstructorGUI(plugin);
                                        ItemStack[] items = tci.getConstruct();
                                        Inventory chamcon = plugin.getServer().createInventory(player, 54, ChatColor.DARK_RED + "Chameleon Construction");
                                        chamcon.setContents(items);
                                        player.openInventory(chamcon);
                                    }, 2L);
                                    break;
                                case 20:
                                case 21:
                                case 22:
                                case 23:
                                case 24:
                                    break;
                                default:
                                    close(player);
                            }
                            if (set.size() > 0) {
                                //set.put("chameleon_demat", preset);
                                plugin.getQueryFactory().doUpdate("tardis", set, wherec);
                            }
                        }
                    }
                }
            }
        }
    }

    private void setDefault(InventoryView view, Player player, String chameleon) {
        // default to Blue Police Box
        // set preset lore
        ItemStack p = view.getItem(23);
        ItemMeta pim = p.getItemMeta();
        pim.setDisplayName(ChatColor.GREEN + "NEW");
        p.setItemMeta(pim);
        TARDISStaticUtils.setSign(chameleon, 3, "NEW", player);
        TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "New Police Box");
    }

    private void toggleOthers(ChameleonOption c, InventoryView view) {
        for (ChameleonOption co : ChameleonOption.values()) {
            ItemStack is = view.getItem(co.getSlot());
            ItemMeta im = is.getItemMeta();
            String onoff;
            Material m;
            if (!co.equals(c)) {
                onoff = co.getOffColour() + plugin.getLanguage().getString(co.getOff());
                m = Material.LIGHT_GRAY_CARPET;
            } else {
                onoff = co.getOnColour() + plugin.getLanguage().getString(co.getOn());
                m = Material.LIME_WOOL;
            }
            im.setDisplayName(onoff);
            is.setItemMeta(im);
            is.setType(m);
        }
    }

    private void updateChameleonSign(boolean update, ArrayList<HashMap<String, String>> map, String preset, Player player) {
        if (update) {
            for (HashMap<String, String> entry : map) {
                TARDISStaticUtils.setSign(entry.get("location"), 3, preset, player);
            }
        }
    }
}
