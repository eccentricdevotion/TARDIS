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
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author eccentric_nz
 */
public class TARDISPresetListener extends TARDISMenuListener implements Listener {

    private final TARDIS plugin;

    public TARDISPresetListener(TARDIS plugin) {
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
    public void onChameleonPresetClick(InventoryClickEvent event) {
        Inventory inv = event.getInventory();
        String name = inv.getTitle();
        if (name.equals("§4Chameleon Presets")) {
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
                        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
                        if (rs.resultSet()) {
                            final Tardis tardis = rs.getTardis();
                            String last_line = TARDISStaticUtils.getLastLine(tardis.getChameleon());
                            String preset = tardis.getPreset().toString();
                            HashMap<String, Object> set = new HashMap<String, Object>();
                            QueryFactory qf = new QueryFactory(plugin);
                            HashMap<String, Object> wherec = new HashMap<String, Object>();
                            wherec.put("tardis_id", id);
                            switch (slot) {
                                case 0:
                                    // new Police Box
                                    if (!last_line.equals("NEW")) {
                                        set.put("chameleon_preset", "NEW");
                                        TARDISStaticUtils.setSign(tardis.getChameleon(), 3, "NEW", player);
                                    }
                                    TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "New Police Box");
                                    break;
                                case 1:
                                    // jungle temple
                                    if (!last_line.equals("JUNGLE")) {
                                        set.put("chameleon_preset", "JUNGLE");
                                        TARDISStaticUtils.setSign(tardis.getChameleon(), 3, "JUNGLE", player);
                                    }
                                    TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Jungle Temple");
                                    break;
                                case 2:
                                    // nether fortress
                                    if (!last_line.equals("NETHER")) {
                                        set.put("chameleon_preset", "NETHER");
                                        TARDISStaticUtils.setSign(tardis.getChameleon(), 3, "NETHER", player);
                                    }
                                    TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Nether Fortress");
                                    break;
                                case 3:
                                    // old police box
                                    if (!last_line.equals("OLD")) {
                                        set.put("chameleon_preset", "OLD");
                                        TARDISStaticUtils.setSign(tardis.getChameleon(), 3, "OLD", player);
                                    }
                                    TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Old Police Box");
                                    break;
                                case 4:
                                    // swamp
                                    if (!last_line.equals("SWAMP")) {
                                        set.put("chameleon_preset", "SWAMP");
                                        TARDISStaticUtils.setSign(tardis.getChameleon(), 3, "SWAMP", player);
                                    }
                                    TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Swamp Hut");
                                    break;
                                case 5:
                                    // party tent
                                    if (!last_line.equals("PARTY")) {
                                        set.put("chameleon_preset", "PARTY");
                                        TARDISStaticUtils.setSign(tardis.getChameleon(), 3, "PARTY", player);
                                    }
                                    TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Party Tent");
                                    break;
                                case 6:
                                    // village house
                                    if (!last_line.equals("VILLAGE")) {
                                        set.put("chameleon_preset", "VILLAGE");
                                        TARDISStaticUtils.setSign(tardis.getChameleon(), 3, "VILLAGE", player);
                                    }
                                    TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Village House");
                                    break;
                                case 7:
                                    // yellow submarine
                                    if (!last_line.equals("YELLOW")) {
                                        set.put("chameleon_preset", "YELLOW");
                                        TARDISStaticUtils.setSign(tardis.getChameleon(), 3, "YELLOW", player);
                                    }
                                    TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Yellow Submarine");
                                    break;
                                case 8:
                                    // telephone
                                    if (!last_line.equals("TELEPHONE")) {
                                        set.put("chameleon_preset", "TELEPHONE");
                                        TARDISStaticUtils.setSign(tardis.getChameleon(), 3, "TELEPHONE", player);
                                    }
                                    TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Red Telephone Box");
                                    break;
                                case 9:
                                    // weeping angel
                                    if (!last_line.equals("ANGEL")) {
                                        set.put("chameleon_preset", "ANGEL");
                                        TARDISStaticUtils.setSign(tardis.getChameleon(), 3, "ANGEL", player);
                                    }
                                    TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Weeping Angel");
                                    break;
                                case 10:
                                    // submerged
                                    if (!last_line.equals("SUBMERGED")) {
                                        set.put("chameleon_preset", "SUBMERGED");
                                        TARDISStaticUtils.setSign(tardis.getChameleon(), 3, "SUBMERGED", player);
                                    }
                                    TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Submerged");
                                    break;
                                case 11:
                                    // flower
                                    if (!last_line.equals("FLOWER")) {
                                        set.put("chameleon_preset", "FLOWER");
                                        TARDISStaticUtils.setSign(tardis.getChameleon(), 3, "FLOWER", player);
                                    }
                                    TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Daisy Flower");
                                    break;
                                case 12:
                                    // stone brick column
                                    if (!last_line.equals("STONE")) {
                                        set.put("chameleon_preset", "STONE");
                                        TARDISStaticUtils.setSign(tardis.getChameleon(), 3, "STONE", player);
                                    }
                                    TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Stone Brick Column");
                                    break;
                                case 13:
                                    // chalice
                                    if (!last_line.equals("CHALICE")) {
                                        set.put("chameleon_preset", "CHALICE");
                                        TARDISStaticUtils.setSign(tardis.getChameleon(), 3, "CHALICE", player);
                                    }
                                    TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Quartz Chalice");
                                    break;
                                case 14:
                                    // desert temple
                                    if (!last_line.equals("DESERT")) {
                                        set.put("chameleon_preset", "DESERT");
                                        TARDISStaticUtils.setSign(tardis.getChameleon(), 3, "DESERT", player);
                                    }
                                    TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Desert Temple");
                                    break;
                                case 15:
                                    // mossy well
                                    if (!last_line.equals("WELL")) {
                                        set.put("chameleon_preset", "WELL");
                                        TARDISStaticUtils.setSign(tardis.getChameleon(), 3, "WELL", player);
                                    }
                                    TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Mossy Well");
                                    break;
                                case 16:
                                    // windmill
                                    if (!last_line.equals("WINDMILL")) {
                                        set.put("chameleon_preset", "WINDMILL");
                                        TARDISStaticUtils.setSign(tardis.getChameleon(), 3, "WINDMILL", player);
                                    }
                                    TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Windmill");
                                    break;
                                case 17:
                                    // Rubber Duck
                                    if (!last_line.equals("DUCK")) {
                                        set.put("chameleon_preset", "DUCK");
                                        TARDISStaticUtils.setSign(tardis.getChameleon(), 3, "DUCK", player);
                                    }
                                    TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Rubber Duck");
                                    break;
                                case 18:
                                    // Mineshaft
                                    if (!last_line.equals("MINESHAFT")) {
                                        set.put("chameleon_preset", "MINESHAFT");
                                        TARDISStaticUtils.setSign(tardis.getChameleon(), 3, "MINESHAFT", player);
                                    }
                                    TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Mineshaft");
                                    break;
                                case 19:
                                    // Creepy
                                    if (!last_line.equals("CREEPY")) {
                                        set.put("chameleon_preset", "CREEPY");
                                        TARDISStaticUtils.setSign(tardis.getChameleon(), 3, "CREEPY", player);
                                    }
                                    TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Creepy");
                                    break;
                                case 20:
                                    // Peanut Butter Jar
                                    if (!last_line.equals("PEANUT")) {
                                        set.put("chameleon_preset", "PEANUT");
                                        TARDISStaticUtils.setSign(tardis.getChameleon(), 3, "PEANUT", player);
                                    }
                                    TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Peanut Butter Jar");
                                    break;
                                case 21:
                                    // Lamp Post
                                    if (!last_line.equals("LAMP")) {
                                        set.put("chameleon_preset", "LAMP");
                                        TARDISStaticUtils.setSign(tardis.getChameleon(), 3, "LAMP", player);
                                    }
                                    TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Lamp Post");
                                    break;
                                case 22:
                                    // Candy Cane
                                    if (!last_line.equals("CANDY")) {
                                        set.put("chameleon_preset", "CANDY");
                                        TARDISStaticUtils.setSign(tardis.getChameleon(), 3, "CANDY", player);
                                    }
                                    TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Candy Cane");
                                    break;
                                case 23:
                                    // Toilet
                                    if (!last_line.equals("TOILET")) {
                                        set.put("chameleon_preset", "TOILET");
                                        TARDISStaticUtils.setSign(tardis.getChameleon(), 3, "TOILET", player);
                                    }
                                    TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Water Closet");
                                    break;
                                case 24:
                                    // Robot
                                    if (!last_line.equals("ROBOT")) {
                                        set.put("chameleon_preset", "ROBOT");
                                        TARDISStaticUtils.setSign(tardis.getChameleon(), 3, "ROBOT", player);
                                    }
                                    TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Robot");
                                    break;
                                case 25:
                                    // Flaming Torch
                                    if (!last_line.equals("TORCH")) {
                                        set.put("chameleon_preset", "TORCH");
                                        TARDISStaticUtils.setSign(tardis.getChameleon(), 3, "TORCH", player);
                                    }
                                    TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Flaming Torch");
                                    break;
                                case 26:
                                    // Pine Tree
                                    if (!last_line.equals("PINE")) {
                                        set.put("chameleon_preset", "PINE");
                                        TARDISStaticUtils.setSign(tardis.getChameleon(), 3, "PINE", player);
                                    }
                                    TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Pine Tree");
                                    break;
                                case 27:
                                    // Steam Punked
                                    if (!last_line.equals("PUNKED")) {
                                        set.put("chameleon_preset", "PUNKED");
                                        TARDISStaticUtils.setSign(tardis.getChameleon(), 3, "PUNKED", player);
                                    }
                                    TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Steam Punked");
                                    break;
                                case 28:
                                    // Nether Portal
                                    if (!last_line.equals("PORTAL")) {
                                        set.put("chameleon_preset", "PORTAL");
                                        TARDISStaticUtils.setSign(tardis.getChameleon(), 3, "PORTAL", player);
                                    }
                                    TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Nether Portal");
                                    break;
                                case 29:
                                    // Cake
                                    if (!last_line.equals("CAKE")) {
                                        set.put("chameleon_preset", "CAKE");
                                        TARDISStaticUtils.setSign(tardis.getChameleon(), 3, "CAKE", player);
                                    }
                                    TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Birthday Cake");
                                    break;
                                case 30:
                                    // Gravestone
                                    if (!last_line.equals("GRAVESTONE")) {
                                        set.put("chameleon_preset", "GRAVESTONE");
                                        TARDISStaticUtils.setSign(tardis.getChameleon(), 3, "GRAVESTONE", player);
                                    }
                                    TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Gravestone");
                                    break;
                                case 31:
                                    // Topsy-turvey
                                    if (!last_line.equals("TOPSYTURVEY")) {
                                        set.put("chameleon_preset", "TOPSYTURVEY");
                                        TARDISStaticUtils.setSign(tardis.getChameleon(), 3, "TOPSYTURVEY", player);
                                    }
                                    TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Topsy-turvey");
                                    break;
                                case 32:
                                    // Mushroom
                                    if (!last_line.equals("SHROOM")) {
                                        set.put("chameleon_preset", "SHROOM");
                                        TARDISStaticUtils.setSign(tardis.getChameleon(), 3, "SHROOM", player);
                                    }
                                    TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Mushroom");
                                    break;
                                case 33:
                                    // Random Fence
                                    if (!last_line.equals("FENCE")) {
                                        set.put("chameleon_preset", "FENCE");
                                        TARDISStaticUtils.setSign(tardis.getChameleon(), 3, "FENCE", player);
                                    }
                                    TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Random Fence");
                                    break;
                                case 34:
                                    // Gazebo
                                    if (!last_line.equals("GAZEBO")) {
                                        set.put("chameleon_preset", "GAZEBO");
                                        TARDISStaticUtils.setSign(tardis.getChameleon(), 3, "GAZEBO", player);
                                    }
                                    TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Gazebo");
                                    break;
                                case 35:
                                    // Apperture Science
                                    if (!last_line.equals("APPERTURE")) {
                                        set.put("chameleon_preset", "APPERTURE");
                                        TARDISStaticUtils.setSign(tardis.getChameleon(), 3, "APPERTURE", player);
                                    }
                                    TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Apperture Science");
                                    break;
                                case 36:
                                    // Lighthouse
                                    if (!last_line.equals("LIGHTHOUSE")) {
                                        set.put("chameleon_preset", "LIGHTHOUSE");
                                        TARDISStaticUtils.setSign(tardis.getChameleon(), 3, "LIGHTHOUSE", player);
                                    }
                                    TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Tiny Lighthouse");
                                    break;
                                case 37:
                                    // Library
                                    if (!last_line.equals("LIBRARY")) {
                                        set.put("chameleon_preset", "LIBRARY");
                                        TARDISStaticUtils.setSign(tardis.getChameleon(), 3, "LIBRARY", player);
                                    }
                                    TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Library");
                                    break;
                                case 38:
                                    // Snowman
                                    if (!last_line.equals("SNOWMAN")) {
                                        set.put("chameleon_preset", "SNOWMAN");
                                        TARDISStaticUtils.setSign(tardis.getChameleon(), 3, "SNOWMAN", player);
                                    }
                                    TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Snowman");
                                    break;
                                case 39:
                                    // Jail Cell
                                    if (!last_line.equals("JAIL")) {
                                        set.put("chameleon_preset", "JAIL");
                                        TARDISStaticUtils.setSign(tardis.getChameleon(), 3, "JAIL", player);
                                    }
                                    TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Jail Cell");
                                    break;
                                case 40:
                                    // Pandorica
                                    if (!last_line.equals("PANDORICA")) {
                                        set.put("chameleon_preset", "PANDORICA");
                                        TARDISStaticUtils.setSign(tardis.getChameleon(), 3, "PANDORICA", player);
                                    }
                                    TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Pandorica");
                                    break;
                                case 41:
                                    // double helix
                                    if (!last_line.equals("HELIX")) {
                                        set.put("chameleon_preset", "HELIX");
                                        TARDISStaticUtils.setSign(tardis.getChameleon(), 3, "HELIX", player);
                                    }
                                    TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Double Helix");
                                    break;
                                case 42:
                                    // Prismarine
                                    if (!last_line.equals("PRISMARINE")) {
                                        set.put("chameleon_preset", "PRISMARINE");
                                        TARDISStaticUtils.setSign(tardis.getChameleon(), 3, "PRISMARINE", player);
                                    }
                                    TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Guardian Temple");
                                    break;
                                case 43:
                                    // Chorus
                                    if (!last_line.equals("CHORUS")) {
                                        set.put("chameleon_preset", "CHORUS");
                                        TARDISStaticUtils.setSign(tardis.getChameleon(), 3, "CHORUS", player);
                                    }
                                    TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Chorus Flower");
                                    break;
                                case 44:
                                    // Andesite
                                    if (!last_line.equals("ANDESITE")) {
                                        set.put("chameleon_preset", "ANDESITE");
                                        TARDISStaticUtils.setSign(tardis.getChameleon(), 3, "ANDESITE", player);
                                    }
                                    TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Andesite Box");
                                    break;
                                case 45:
                                    // Diorite
                                    if (!last_line.equals("DIORITE")) {
                                        set.put("chameleon_preset", "DIORITE");
                                        TARDISStaticUtils.setSign(tardis.getChameleon(), 3, "DIORITE", player);
                                    }
                                    TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Diorite Box");
                                    break;
                                case 46:
                                    // Granite
                                    if (!last_line.equals("GRANITE")) {
                                        set.put("chameleon_preset", "GRANITE");
                                        TARDISStaticUtils.setSign(tardis.getChameleon(), 3, "GRANITE", player);
                                    }
                                    TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Granite Box");
                                    break;
                                case 48:
                                    // custom
                                    if (!last_line.equals("CUSTOM")) {
                                        set.put("chameleon_preset", "CUSTOM");
                                        TARDISStaticUtils.setSign(tardis.getChameleon(), 3, "CUSTOM", player);
                                    }
                                    TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Server's Custom");
                                    break;
                                case 52:
                                    // return to Chameleon Circuit GUI
                                    close(player);
                                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                                        @Override
                                        public void run() {
                                            ItemStack[] chameleon = new TARDISChameleonInventory(plugin, tardis.getAdaption(), tardis.getPreset()).getMenu();
                                            Inventory gui = plugin.getServer().createInventory(player, 27, "§4Chameleon Circuit");
                                            gui.setContents(chameleon);
                                            player.openInventory(gui);
                                        }
                                    }, 2L);
                                    break;
                                default:
                                    close(player);
                                    break;
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
}
