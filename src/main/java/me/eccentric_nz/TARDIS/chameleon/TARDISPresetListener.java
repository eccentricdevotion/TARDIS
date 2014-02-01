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
package me.eccentric_nz.TARDIS.chameleon;

import java.util.Arrays;
import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.MESSAGE;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author eccentric_nz
 */
public class TARDISPresetListener implements Listener {

    private final TARDIS plugin;

    public TARDISPresetListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Listens for player clicking inside an inventory. If the inventory is a
     * TARDIS GUI, then the click is processed accordingly.
     *
     * @param event a player clicking an inventory slot
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPresetTerminalClick(InventoryClickEvent event) {
        Inventory inv = event.getInventory();
        String name = inv.getTitle();
        if (name.equals("§4More Presets")) {
            event.setCancelled(true);
            int slot = event.getRawSlot();
            final Player player = (Player) event.getWhoClicked();
            if (slot >= 0 && slot < 54) {
                ItemStack is = inv.getItem(slot);
                if (is != null) {
                    String playerNameStr = player.getName();
                    // get the TARDIS the player is in
                    HashMap<String, Object> wheres = new HashMap<String, Object>();
                    wheres.put("player", playerNameStr);
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
                                case 1:
                                    // toggle chameleon circuit
                                    String onoff;
                                    String engage;
                                    int oo;
                                    if (rs.isChamele_on()) {
                                        onoff = ChatColor.RED + "OFF";
                                        engage = "ON";
                                        oo = 0;
                                    } else {
                                        onoff = ChatColor.GREEN + "ON";
                                        engage = "OFF";
                                        oo = 1;
                                    }
                                    ItemMeta im = is.getItemMeta();
                                    im.setLore(Arrays.asList(new String[]{onoff, "Click to turn " + engage}));
                                    is.setItemMeta(im);
                                    // set sign text
                                    setSign(rs.getChameleon(), 2, onoff, player);
                                    set.put("chamele_on", oo);
                                    break;
                                case 3:
                                    player.performCommand("tardis rebuild");
                                    close(player);
                                    break;
                                case 5:
                                    // toggle biome adaption
                                    String biome;
                                    String to_turn;
                                    int ba;
                                    if (rs.isAdapti_on()) {
                                        biome = ChatColor.RED + "OFF";
                                        to_turn = "ON";
                                        ba = 0;
                                    } else {
                                        biome = ChatColor.GREEN + "ON";
                                        to_turn = "OFF";
                                        ba = 1;
                                    }
                                    ItemMeta bio = is.getItemMeta();
                                    bio.setLore(Arrays.asList(new String[]{biome, "Click to turn " + to_turn}));
                                    is.setItemMeta(bio);
                                    set.put("adapti_on", ba);
                                    break;
                                case 9:
                                    // custom
                                    set.put("chameleon_preset", "CUSTOM");
                                    setSign(rs.getChameleon(), 3, "CUSTOM", player);

                                    player.sendMessage(plugin.pluginName + MESSAGE.CHAMELEON_SET.getText() + ChatColor.AQUA + "Server's Custom");
                                    break;
                                case 11:
                                    // Rubber Duck
                                    set.put("chameleon_preset", "DUCK");
                                    setSign(rs.getChameleon(), 3, "DUCK", player);
                                    player.sendMessage(plugin.pluginName + MESSAGE.CHAMELEON_SET.getText() + ChatColor.AQUA + "Rubber Duck");
                                    break;
                                case 13:
                                    // Mineshaft
                                    set.put("chameleon_preset", "MINESHAFT");
                                    setSign(rs.getChameleon(), 3, "MINESHAFT", player);
                                    player.sendMessage(plugin.pluginName + MESSAGE.CHAMELEON_SET.getText() + ChatColor.AQUA + "Mineshaft");
                                    break;
                                case 15:
                                    // Creepy
                                    set.put("chameleon_preset", "CREEPY");
                                    setSign(rs.getChameleon(), 3, "CREEPY", player);
                                    player.sendMessage(plugin.pluginName + MESSAGE.CHAMELEON_SET.getText() + ChatColor.AQUA + "Creepy");
                                    break;
                                case 17:
                                    // Peanut Butter Jar
                                    set.put("chameleon_preset", "PEANUT");
                                    setSign(rs.getChameleon(), 3, "PEANUT", player);
                                    player.sendMessage(plugin.pluginName + MESSAGE.CHAMELEON_SET.getText() + ChatColor.AQUA + "Peanut Butter Jar");
                                    break;
                                case 19:
                                    // Lamp Post
                                    set.put("chameleon_preset", "LAMP");
                                    setSign(rs.getChameleon(), 3, "LAMP", player);
                                    player.sendMessage(plugin.pluginName + MESSAGE.CHAMELEON_SET.getText() + ChatColor.AQUA + "Lamp Post");
                                    break;
                                case 21:
                                    // Candy Cane
                                    set.put("chameleon_preset", "CANDY");
                                    setSign(rs.getChameleon(), 3, "CANDY", player);
                                    player.sendMessage(plugin.pluginName + MESSAGE.CHAMELEON_SET.getText() + ChatColor.AQUA + "Candy Cane");
                                    break;
                                case 23:
                                    // Toilet
                                    set.put("chameleon_preset", "TOILET");
                                    setSign(rs.getChameleon(), 3, "TOILET", player);
                                    player.sendMessage(plugin.pluginName + MESSAGE.CHAMELEON_SET.getText() + ChatColor.AQUA + "Water Closet");
                                    break;
                                case 25:
                                    // Robot
                                    set.put("chameleon_preset", "ROBOT");
                                    setSign(rs.getChameleon(), 3, "ROBOT", player);
                                    player.sendMessage(plugin.pluginName + MESSAGE.CHAMELEON_SET.getText() + ChatColor.AQUA + "Robot");
                                    break;
                                case 27:
                                    // Flaming Torch
                                    set.put("chameleon_preset", "TORCH");
                                    setSign(rs.getChameleon(), 3, "TORCH", player);
                                    player.sendMessage(plugin.pluginName + MESSAGE.CHAMELEON_SET.getText() + ChatColor.AQUA + "Flaming Torch");
                                    break;
                                case 29:
                                    // Pine Tree
                                    set.put("chameleon_preset", "PINE");
                                    setSign(rs.getChameleon(), 3, "PINE", player);
                                    player.sendMessage(plugin.pluginName + MESSAGE.CHAMELEON_SET.getText() + ChatColor.AQUA + "Pine Tree");
                                    break;
                                case 31:
                                    // Steam Punked
                                    set.put("chameleon_preset", "PUNKED");
                                    setSign(rs.getChameleon(), 3, "PUNKED", player);
                                    player.sendMessage(plugin.pluginName + MESSAGE.CHAMELEON_SET.getText() + ChatColor.AQUA + "Steam Punked");
                                    break;
                                case 33:
                                    // Random Fence
                                    set.put("chameleon_preset", "FENCE");
                                    setSign(rs.getChameleon(), 3, "FENCE", player);
                                    player.sendMessage(plugin.pluginName + MESSAGE.CHAMELEON_SET.getText() + ChatColor.AQUA + "Random Fence");
                                    break;
                                case 35:
                                    // Nether Portal
                                    set.put("chameleon_preset", "PORTAL");
                                    setSign(rs.getChameleon(), 3, "PORTAL", player);
                                    player.sendMessage(plugin.pluginName + MESSAGE.CHAMELEON_SET.getText() + ChatColor.AQUA + "Nether Portal");
                                    break;
                                case 37:
                                    // Gazebo
                                    set.put("chameleon_preset", "GAZEBO");
                                    setSign(rs.getChameleon(), 3, "GAZEBO", player);
                                    player.sendMessage(plugin.pluginName + MESSAGE.CHAMELEON_SET.getText() + ChatColor.AQUA + "Gazebo");
                                    break;
                                case 39:
                                    // Apperture Science
                                    set.put("chameleon_preset", "APPERTURE");
                                    setSign(rs.getChameleon(), 3, "APPERTURE", player);
                                    player.sendMessage(plugin.pluginName + MESSAGE.CHAMELEON_SET.getText() + ChatColor.AQUA + "Apperture Science");
                                    break;
                                case 41:
                                    // Lighthouse
                                    set.put("chameleon_preset", "LIGHTHOUSE");
                                    setSign(rs.getChameleon(), 3, "LIGHTHOUSE", player);
                                    player.sendMessage(plugin.pluginName + MESSAGE.CHAMELEON_SET.getText() + ChatColor.AQUA + "Tiny Lighthouse");
                                    break;
                                case 43:
                                    // Library
                                    set.put("chameleon_preset", "LIBRARY");
                                    setSign(rs.getChameleon(), 3, "LIBRARY", player);
                                    player.sendMessage(plugin.pluginName + MESSAGE.CHAMELEON_SET.getText() + ChatColor.AQUA + "Library");
                                    break;
                                case 45:
                                    // Snowman
                                    set.put("chameleon_preset", "SNOWMAN");
                                    setSign(rs.getChameleon(), 3, "SNOWMAN", player);
                                    player.sendMessage(plugin.pluginName + MESSAGE.CHAMELEON_SET.getText() + ChatColor.AQUA + "Snowman");
                                    break;
                                case 47:
                                    // Jail Cell
                                    set.put("chameleon_preset", "JAIL");
                                    setSign(rs.getChameleon(), 3, "JAIL", player);
                                    player.sendMessage(plugin.pluginName + MESSAGE.CHAMELEON_SET.getText() + ChatColor.AQUA + "Jail Cell");
                                    break;
                                case 49:
                                    // Pandorica
                                    set.put("chameleon_preset", "PANDORICA");
                                    setSign(rs.getChameleon(), 3, "PANDORICA", player);
                                    player.sendMessage(plugin.pluginName + MESSAGE.CHAMELEON_SET.getText() + ChatColor.AQUA + "Pandorica");
                                    break;
                                case 51:
                                    // double helix
                                    set.put("chameleon_preset", "HELIX");
                                    setSign(rs.getChameleon(), 3, "HELIX", player);
                                    player.sendMessage(plugin.pluginName + MESSAGE.CHAMELEON_SET.getText() + ChatColor.AQUA + "Double Helix");
                                    break;
                                case 53:
                                    // page one
                                    close(player);
                                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                                        @Override
                                        public void run() {
                                            TARDISChameleonInventory tci = new TARDISChameleonInventory(bool, adapt);
                                            ItemStack[] items = tci.getTerminal();
                                            Inventory chaminv = plugin.getServer().createInventory(player, 54, "§4Chameleon Circuit");
                                            chaminv.setContents(items);
                                            player.openInventory(chaminv);
                                        }
                                    }, 2L);
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
        Block cc = plugin.utils.getLocationFromDB(loc, 0, 0).getBlock();
        if (cc.getType() == Material.WALL_SIGN || cc.getType() == Material.SIGN_POST) {
            Sign sign = (Sign) cc.getState();
            sign.setLine(line, text);
            sign.update();
        } else {
            p.sendMessage(plugin.pluginName + "Chameleon Circuit " + text);
        }
    }

    /**
     * Closes the inventory.
     *
     * @param p the player using the GUI
     */
    private void close(final Player p) {
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                p.closeInventory();
            }
        }, 1L);
    }
}
