/*
 * Copyright (C) 2016 eccentric_nz
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

import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitChecker;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitDamager;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.enumeration.ADAPTION;
import me.eccentric_nz.TARDIS.enumeration.CHAMELEON_OPTION;
import me.eccentric_nz.TARDIS.enumeration.DIFFICULTY;
import me.eccentric_nz.TARDIS.enumeration.DISK_CIRCUIT;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author eccentric_nz
 */
public class TARDISChameleonListener extends TARDISMenuListener implements Listener {

    private final TARDIS plugin;

    public TARDISChameleonListener(TARDIS plugin) {
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
    public void onChameleonMenuClick(InventoryClickEvent event) {
        Inventory inv = event.getInventory();
        String name = inv.getTitle();
        if (name.equals("§4Chameleon Circuit")) {
            event.setCancelled(true);
            int slot = event.getRawSlot();
            final Player player = (Player) event.getWhoClicked();
            if (slot >= 0 && slot < 27) {
                ItemStack is = inv.getItem(slot);
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
                            final ADAPTION adapt = tardis.getAdaption();
//                            String preset = tardis.getPreset().toString();
                            HashMap<String, Object> set = new HashMap<>();
                            HashMap<String, Object> wherec = new HashMap<>();
                            wherec.put("tardis_id", id);
                            switch (slot) {
                                case 0:
                                    player.performCommand("tardis rebuild");
                                    close(player);
                                    // damage the circuit if configured
                                    if (plugin.getConfig().getBoolean("circuits.damage") && !plugin.getDifficulty().equals(DIFFICULTY.EASY) && plugin.getConfig().getInt("circuits.uses.chameleon") > 0) {
                                        TARDISCircuitChecker tcc = new TARDISCircuitChecker(plugin, id);
                                        tcc.getCircuits();
                                        // decrement uses
                                        int uses_left = tcc.getChameleonUses();
                                        new TARDISCircuitDamager(plugin, DISK_CIRCUIT.CHAMELEON, uses_left, id, player).damage();
                                    }
                                    break;
                                case 11:
                                    // factory
                                    set.put("adapti_on", 0);
                                    ItemStack frb = inv.getItem(20);
                                    ItemMeta fact = frb.getItemMeta();
                                    String ory = fact.getDisplayName();
                                    if (ory.equals(ChatColor.GREEN + plugin.getLanguage().getString("SET_ON"))) {
                                        set.put("chameleon_preset", "FACTORY");
                                        toggleOthers(CHAMELEON_OPTION.FACTORY, inv);
                                        TARDISStaticUtils.setSign(tardis.getChameleon(), 3, "FACTORY", player);
                                        TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Factory Fresh");
                                    } else {
                                        set.put("chameleon_preset", "NEW");
                                        toggleOthers(CHAMELEON_OPTION.PRESET, inv);
                                        setDefault(inv, player, tardis.getChameleon());
                                    }
                                    break;
                                case 12:
                                    // cycle biome adaption
                                    int ca = adapt.ordinal() + 1;
                                    if (ca >= ADAPTION.values().length) {
                                        ca = 0;
                                    }
                                    ADAPTION a = ADAPTION.values()[ca];
                                    if (a.equals(ADAPTION.OFF)) {
                                        // default to Blue Police Box
                                        set.put("chameleon_preset", "NEW");
                                        toggleOthers(CHAMELEON_OPTION.PRESET, inv);
                                        setDefault(inv, player, tardis.getChameleon());
                                    } else {
                                        toggleOthers(CHAMELEON_OPTION.ADAPTIVE, inv);
                                        set.put("chameleon_preset", "FACTORY");
                                    }
                                    set.put("adapti_on", ca);
                                    ItemStack arb = inv.getItem(21);
                                    ItemMeta bio = arb.getItemMeta();
                                    bio.setDisplayName(a.getColour() + a.toString());
                                    arb.setItemMeta(bio);
                                    break;
                                case 13:
                                    // Invisibility
                                    set.put("adapti_on", 0);
                                    ItemStack irb = inv.getItem(22);
                                    ItemMeta invis = irb.getItemMeta();
                                    String ible = invis.getDisplayName();
                                    if (ible.equals(ChatColor.RED + plugin.getLanguage().getString("SET_OFF"))) {
                                        // check they have an Invisibility Circuit
                                        TARDISCircuitChecker tcc = new TARDISCircuitChecker(plugin, id);
                                        tcc.getCircuits();
                                        if (!plugin.getDifficulty().equals(DIFFICULTY.EASY)) {
                                            if (!plugin.getUtils().inGracePeriod(player, false) && !tcc.hasInvisibility()) {
                                                close(player);
                                                TARDISMessage.send(player, "INVISIBILITY_MISSING");
                                                break;
                                            }
                                        }
                                        if (plugin.getConfig().getBoolean("circuits.damage") && !plugin.getDifficulty().equals(DIFFICULTY.EASY) && plugin.getConfig().getInt("circuits.uses.invisibility") > 0) {
                                            // decrement uses
                                            int uses_left = tcc.getInvisibilityUses();
                                            new TARDISCircuitDamager(plugin, DISK_CIRCUIT.INVISIBILITY, uses_left, id, player).damage();
                                        }
                                        toggleOthers(CHAMELEON_OPTION.INVISIBLE, inv);
                                        set.put("chameleon_preset", "INVISIBLE");
                                        TARDISStaticUtils.setSign(tardis.getChameleon(), 3, "INVISIBLE", player);
                                        TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Invisibility");
                                    } else {
                                        toggleOthers(CHAMELEON_OPTION.PRESET, inv);
                                        // default to Blue Police Box
                                        set.put("chameleon_preset", "NEW");
                                        setDefault(inv, player, tardis.getChameleon());
                                    }
                                    break;
                                case 14:
                                    // presets
                                    close(player);
                                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                                        TARDISPresetInventory tpi = new TARDISPresetInventory(plugin);
                                        ItemStack[] items = tpi.getPresets();
                                        Inventory presetinv = plugin.getServer().createInventory(player, 54, "§4Chameleon Presets");
                                        presetinv.setContents(items);
                                        player.openInventory(presetinv);
                                    }, 2L);
                                    break;
                                case 15:
                                    // constructor GUI
                                    close(player);
                                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                                        TARDISChameleonConstructorGUI tci = new TARDISChameleonConstructorGUI(plugin);
                                        ItemStack[] items = tci.getConstruct();
                                        Inventory chamcon = plugin.getServer().createInventory(player, 54, "§4Chameleon Construction");
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
                                new QueryFactory(plugin).doUpdate("tardis", set, wherec);
                            }
                        }
                    }
                }
            }
        }
    }

    private void setDefault(Inventory inv, Player player, String chameleon) {
        // default to Blue Police Box
        // set preset lore
        ItemStack p = inv.getItem(23);
        ItemMeta pim = p.getItemMeta();
        pim.setDisplayName(ChatColor.GREEN + "NEW");
        p.setItemMeta(pim);
        TARDISStaticUtils.setSign(chameleon, 3, "NEW", player);
        TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "New Police Box");
    }

    private void toggleOthers(CHAMELEON_OPTION c, Inventory inv) {
        for (CHAMELEON_OPTION co : CHAMELEON_OPTION.values()) {
            ItemStack is = inv.getItem(co.getSlot());
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
}
