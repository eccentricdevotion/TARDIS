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
public class TARDISChameleonListener implements Listener {

    private final TARDIS plugin;

    public TARDISChameleonListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Listens for player clicking inside an inventory. If the inventory is a
     * TARDIS GUI, then the click is processed accordingly.
     *
     * @param event a player clicking an inventory slot
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onChameleonTerminalClick(InventoryClickEvent event) {
        Inventory inv = event.getInventory();
        String name = inv.getTitle();
        if (name.equals("§4Chameleon Circuit")) {
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
                            String preset = rs.getPreset().toString();
                            HashMap<String, Object> set = new HashMap<String, Object>();
                            QueryFactory qf = new QueryFactory(plugin);
                            HashMap<String, Object> wherec = new HashMap<String, Object>();
                            wherec.put("tardis_id", id);
                            switch (slot) {
                                case 0:
                                    // toggle chameleon circuit
                                    String onoff;
                                    int oo;
                                    if (rs.isChamele_on()) {
                                        onoff = ChatColor.RED + "OFF";
                                        oo = 0;
                                    } else {
                                        onoff = ChatColor.GREEN + "ON";
                                        oo = 1;
                                    }
                                    ItemMeta im = is.getItemMeta();
                                    im.setLore(Arrays.asList(new String[]{onoff}));
                                    is.setItemMeta(im);
                                    // set sign text
                                    setSign(rs.getChameleon(), 2, onoff, player);
                                    set.put("chamele_on", oo);
                                    break;
                                case 4:
                                    player.performCommand("tardis rebuild");
                                    close(player);
                                    break;
                                case 18:
                                    // new Police Box
                                    set.put("chameleon_preset", "NEW");
                                    setSign(rs.getChameleon(), 3, "NEW", player);
                                    close(player);
                                    player.sendMessage(plugin.pluginName + "Chameleon Preset set to " + ChatColor.AQUA + "New Police Box");
                                    break;
                                case 20:
                                    // factory
                                    set.put("chameleon_preset", "FACTORY");
                                    setSign(rs.getChameleon(), 3, "FACTORY", player);
                                    close(player);
                                    player.sendMessage(plugin.pluginName + "Chameleon Preset set to " + ChatColor.AQUA + "Factory Fresh");
                                    break;
                                case 22:
                                    // jungle temple
                                    set.put("chameleon_preset", "JUNGLE");
                                    setSign(rs.getChameleon(), 3, "JUNGLE", player);
                                    close(player);
                                    player.sendMessage(plugin.pluginName + "Chameleon Preset set to " + ChatColor.AQUA + "Jungle Temple");
                                    break;
                                case 24:
                                    // nether fortress
                                    set.put("chameleon_preset", "NETHER");
                                    setSign(rs.getChameleon(), 3, "NETHER", player);
                                    close(player);
                                    player.sendMessage(plugin.pluginName + "Chameleon Preset set to " + ChatColor.AQUA + "Nether Fortress");
                                    break;
                                case 26:
                                    // old police box
                                    set.put("chameleon_preset", "OLD");
                                    setSign(rs.getChameleon(), 3, "OLD", player);
                                    close(player);
                                    player.sendMessage(plugin.pluginName + "Chameleon Preset set to " + ChatColor.AQUA + "Old Police Box");
                                    break;
                                case 28:
                                    // swamp hut
                                    set.put("chameleon_preset", "SWAMP");
                                    setSign(rs.getChameleon(), 3, "SWAMP", player);
                                    close(player);
                                    player.sendMessage(plugin.pluginName + "Chameleon Preset set to " + ChatColor.AQUA + "Swamp Hut");
                                    break;
                                case 30:
                                    // party tent
                                    set.put("chameleon_preset", "PARTY");
                                    setSign(rs.getChameleon(), 3, "PARTY", player);
                                    close(player);
                                    player.sendMessage(plugin.pluginName + "Chameleon Preset set to " + ChatColor.AQUA + "Party Tent");
                                    break;
                                case 32:
                                    // village house
                                    set.put("chameleon_preset", "VILLAGE");
                                    setSign(rs.getChameleon(), 3, "VILLAGE", player);
                                    close(player);
                                    player.sendMessage(plugin.pluginName + "Chameleon Preset set to " + ChatColor.AQUA + "Village House");
                                    break;
                                case 34:
                                    // yellow submarine
                                    set.put("chameleon_preset", "YELLOW");
                                    setSign(rs.getChameleon(), 3, "YELLOW", player);
                                    close(player);
                                    player.sendMessage(plugin.pluginName + "Chameleon Preset set to " + ChatColor.AQUA + "Yellow Submarine");
                                    break;
                                case 36:
                                    // telephone
                                    set.put("chameleon_preset", "TELEPHONE");
                                    setSign(rs.getChameleon(), 3, "TELEPHONE", player);
                                    close(player);
                                    player.sendMessage(plugin.pluginName + "Chameleon Preset set to " + ChatColor.AQUA + "Red Telephone Box");
                                    break;
                                case 38:
                                    // submerged
                                    set.put("chameleon_preset", "SUBMERGED");
                                    setSign(rs.getChameleon(), 3, "SUBMERGED", player);
                                    close(player);
                                    player.sendMessage(plugin.pluginName + "Chameleon Preset set to " + ChatColor.AQUA + "Submerged");
                                    break;
                                case 40:
                                    // raised
                                    set.put("chameleon_preset", "RAISED");
                                    setSign(rs.getChameleon(), 3, "RAISED", player);
                                    close(player);
                                    player.sendMessage(plugin.pluginName + "Chameleon Preset set to " + ChatColor.AQUA + "Raised Swamp Hut");
                                    break;
                                case 42:
                                    // flower
                                    set.put("chameleon_preset", "FLOWER");
                                    setSign(rs.getChameleon(), 3, "FLOWER", player);
                                    close(player);
                                    player.sendMessage(plugin.pluginName + "Chameleon Preset set to " + ChatColor.AQUA + "Daisy Flower");
                                    break;
                                case 44:
                                    // chalice
                                    set.put("chameleon_preset", "CHALICE");
                                    setSign(rs.getChameleon(), 3, "CHALICE", player);
                                    close(player);
                                    player.sendMessage(plugin.pluginName + "Chameleon Preset set to " + ChatColor.AQUA + "Quartz Chalice");
                                    break;
                                case 46:
                                    // stone brick column
                                    set.put("chameleon_preset", "STONE");
                                    setSign(rs.getChameleon(), 3, "STONE", player);
                                    close(player);
                                    player.sendMessage(plugin.pluginName + "Chameleon Preset set to " + ChatColor.AQUA + "Stone Brick Column");
                                    break;
                                case 48:
                                    // windmill
                                    set.put("chameleon_preset", "WINDMILL");
                                    setSign(rs.getChameleon(), 3, "WINDMILL", player);
                                    close(player);
                                    player.sendMessage(plugin.pluginName + "Chameleon Preset set to " + ChatColor.AQUA + "Windmill");
                                    break;
                                case 50:
                                    // desert temple
                                    set.put("chameleon_preset", "DESERT");
                                    setSign(rs.getChameleon(), 3, "DESERT", player);
                                    close(player);
                                    player.sendMessage(plugin.pluginName + "Chameleon Preset set to " + ChatColor.AQUA + "Desert Temple");
                                    break;
                                case 52:
                                    // mossy well
                                    set.put("chameleon_preset", "WELL");
                                    setSign(rs.getChameleon(), 3, "WELL", player);
                                    close(player);
                                    player.sendMessage(plugin.pluginName + "Chameleon Preset set to " + ChatColor.AQUA + "Mossy Well");
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
