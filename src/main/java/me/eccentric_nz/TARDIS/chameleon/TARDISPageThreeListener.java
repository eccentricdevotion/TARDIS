/*
 * Copyright (C) 2014 eccentric_nz
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

import java.util.Arrays;
import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitChecker;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitDamager;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.DISK_CIRCUIT;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
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
public class TARDISPageThreeListener extends TARDISMenuListener implements Listener {

    private final TARDIS plugin;

    public TARDISPageThreeListener(TARDIS plugin) {
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
    public void onPageThreeTerminalClick(InventoryClickEvent event) {
        Inventory inv = event.getInventory();
        String name = inv.getTitle();
        if (name.equals("§4Even More Presets")) {
            event.setCancelled(true);
            int slot = event.getRawSlot();
            final Player player = (Player) event.getWhoClicked();
            if (slot >= 0 && slot < 54) {
                ItemStack is = inv.getItem(slot);
                if (is != null) {
                    // get the TARDIS the player is in
                    HashMap<String, Object> wheres = new HashMap<String, Object>();
                    wheres.put("uuid", player.getUniqueId().toString());
                    ResultSetTravellers rst = new ResultSetTravellers(plugin, wheres, false);
                    if (rst.resultSet()) {
                        int id = rst.getTardis_id();
                        HashMap<String, Object> where = new HashMap<String, Object>();
                        where.put("tardis_id", id);
                        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
                        if (rs.resultSet()) {
                            final boolean bool = rs.isChamele_on();
                            final boolean adapt = rs.isAdapti_on();
                            String preset = rs.getPreset().toString();
                            HashMap<String, Object> set = new HashMap<String, Object>();
                            QueryFactory qf = new QueryFactory(plugin);
                            HashMap<String, Object> wherec = new HashMap<String, Object>();
                            wherec.put("tardis_id", id);
                            switch (slot) {
                                case 0:
                                    // toggle chameleon circuit
                                    String onoff;
                                    String engage;
                                    int oo;
                                    if (rs.isChamele_on()) {
                                        onoff = ChatColor.RED + plugin.getLanguage().getString("SET_OFF");
                                        engage = plugin.getLanguage().getString("SET_ON");
                                        oo = 0;
                                    } else {
                                        onoff = ChatColor.GREEN + plugin.getLanguage().getString("SET_ON");
                                        engage = plugin.getLanguage().getString("SET_OFF");
                                        oo = 1;
                                    }
                                    ItemMeta im = is.getItemMeta();
                                    im.setLore(Arrays.asList(onoff, String.format(plugin.getLanguage().getString("CHAM_CLICK"), engage)));
                                    is.setItemMeta(im);
                                    // set sign text
                                    setSign(rs.getChameleon(), 2, onoff, player);
                                    set.put("chamele_on", oo);
                                    break;
                                case 2:
                                    player.performCommand("tardis rebuild");
                                    close(player);
                                    break;
                                case 4:
                                    // toggle biome adaption
                                    String biome;
                                    String to_turn;
                                    int ba;
                                    if (rs.isAdapti_on()) {
                                        biome = ChatColor.RED + plugin.getLanguage().getString("SET_OFF");
                                        to_turn = plugin.getLanguage().getString("SET_ON");
                                        ba = 0;
                                    } else {
                                        biome = ChatColor.GREEN + plugin.getLanguage().getString("SET_ON");
                                        to_turn = plugin.getLanguage().getString("SET_OFF");
                                        ba = 1;
                                    }
                                    ItemMeta bio = is.getItemMeta();
                                    bio.setLore(Arrays.asList(biome, String.format(plugin.getLanguage().getString("CHAM_CLICK"), to_turn)));
                                    is.setItemMeta(bio);
                                    set.put("adapti_on", ba);
                                    break;
                                case 8:
                                    // page one
                                    close(player);
                                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                                        @Override
                                        public void run() {
                                            TARDISChameleonInventory tci = new TARDISChameleonInventory(plugin, bool, adapt);
                                            ItemStack[] items = tci.getTerminal();
                                            Inventory chaminv = plugin.getServer().createInventory(player, 54, "§4Chameleon Circuit");
                                            chaminv.setContents(items);
                                            player.openInventory(chaminv);
                                        }
                                    }, 2L);
                                    break;
                                case 18:
                                    // Gazebo
                                    set.put("chameleon_preset", "GAZEBO");
                                    setSign(rs.getChameleon(), 3, "GAZEBO", player);
                                    TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Gazebo");
                                    break;
                                case 20:
                                    // Apperture Science
                                    set.put("chameleon_preset", "APPERTURE");
                                    setSign(rs.getChameleon(), 3, "APPERTURE", player);
                                    TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Apperture Science");
                                    break;
                                case 22:
                                    // Lighthouse
                                    set.put("chameleon_preset", "LIGHTHOUSE");
                                    setSign(rs.getChameleon(), 3, "LIGHTHOUSE", player);
                                    TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Tiny Lighthouse");
                                    break;
                                case 24:
                                    // Library
                                    set.put("chameleon_preset", "LIBRARY");
                                    setSign(rs.getChameleon(), 3, "LIBRARY", player);
                                    TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Library");
                                    break;
                                case 26:
                                    // Snowman
                                    set.put("chameleon_preset", "SNOWMAN");
                                    setSign(rs.getChameleon(), 3, "SNOWMAN", player);
                                    TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Snowman");
                                    break;
                                case 28:
                                    // Jail Cell
                                    set.put("chameleon_preset", "JAIL");
                                    setSign(rs.getChameleon(), 3, "JAIL", player);
                                    TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Jail Cell");
                                    break;
                                case 30:
                                    // Pandorica
                                    set.put("chameleon_preset", "PANDORICA");
                                    setSign(rs.getChameleon(), 3, "PANDORICA", player);
                                    TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Pandorica");
                                    break;
                                case 32:
                                    // double helix
                                    set.put("chameleon_preset", "HELIX");
                                    setSign(rs.getChameleon(), 3, "HELIX", player);
                                    TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Double Helix");
                                    break;
                                case 34:
                                    // Prismarine
                                    set.put("chameleon_preset", "PRISMARINE");
                                    setSign(rs.getChameleon(), 3, "PRISMARINE", player);
                                    TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Guardian Temple");
                                    break;
                                case 38:
                                    // Andesite
                                    set.put("chameleon_preset", "ANDESITE");
                                    setSign(rs.getChameleon(), 3, "ANDESITE", player);
                                    TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Andesite Box");
                                    break;
                                case 40:
                                    // Diorite
                                    set.put("chameleon_preset", "DIORITE");
                                    setSign(rs.getChameleon(), 3, "DIORITE", player);
                                    TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Diorite Box");
                                    break;
                                case 42:
                                    // Granite
                                    set.put("chameleon_preset", "GRANITE");
                                    setSign(rs.getChameleon(), 3, "GRANITE", player);
                                    TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Granite Box");
                                    break;
                                case 49:
                                    // Invisibility
                                    // check they have an Invisibility Circuit
                                    TARDISCircuitChecker tcc = new TARDISCircuitChecker(plugin, id);
                                    tcc.getCircuits();
                                    if (plugin.getConfig().getString("preferences.difficulty").equals("hard")) {
                                        if (!plugin.getUtils().inGracePeriod(player, false) && !tcc.hasInvisibility()) {
                                            close(player);
                                            TARDISMessage.send(player, "INVISIBILITY_MISSING");
                                            break;
                                        }
                                    }
                                    if (plugin.getConfig().getBoolean("circuits.damage") && (plugin.getConfig().getString("preferences.difficulty").equals("hard") || plugin.getConfig().getString("circuits.level").equals("easy")) && plugin.getConfig().getInt("circuits.uses.invisibility") > 0) {
                                        // decrement uses
                                        int uses_left = tcc.getInvisibilityUses();
                                        if (uses_left != -1) {
                                            new TARDISCircuitDamager(plugin, DISK_CIRCUIT.INVISIBILITY, uses_left, id, player).damage();
                                        }
                                    }
                                    set.put("chameleon_preset", "INVISIBLE");
                                    setSign(rs.getChameleon(), 3, "INVISIBLE", player);
                                    TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Invisibility");
                                    break;
                                default:
                                    close(player);
                            }
                            if (set.size() > 0) {
                                set.put("chameleon_demat", preset);
                                qf.doUpdate("tardis", set, wherec);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Sets the Chameleon Sign text or messages the player.
     *
     * @param loc the location string retrieved from the database
     * @param line the line number to set
     * @param text the text to write
     * @param p the player to message (if the Chameleon control is not a sign)
     * @return the destination string
     */
    private void setSign(String loc, int line, String text, Player p) {
        // get sign block so we can update it
        Block cc = plugin.getUtils().getLocationFromDB(loc, 0, 0).getBlock();
        if (cc.getType() == Material.WALL_SIGN || cc.getType() == Material.SIGN_POST) {
            Sign sign = (Sign) cc.getState();
            sign.setLine(line, text);
            sign.update();
        } else {
            TARDISMessage.send(p, "CHAM", " " + text);
        }
    }
}
