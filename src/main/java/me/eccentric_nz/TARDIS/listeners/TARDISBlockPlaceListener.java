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
package me.eccentric_nz.TARDIS.listeners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.achievement.TARDISAchievementNotify;
import me.eccentric_nz.TARDIS.builders.TARDISSpace;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetCount;
import me.eccentric_nz.TARDIS.database.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.rooms.TARDISWalls;
import me.eccentric_nz.TARDIS.utility.TARDISUtils;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

/**
 * TARDISes are bioships that are grown from a species of coral presumably
 * indigenous to Gallifrey.
 *
 * The TARDIS had a drawing room, which the Doctor claimed to be his "private
 * study". Inside it were momentos of his many incarnations' travels.
 *
 * @author eccentric_nz
 */
public class TARDISBlockPlaceListener implements Listener {

    private TARDIS plugin;
    private TARDISUtils utils;
    private List<Material> blocks = new ArrayList<Material>();
    private Material custom;

    public TARDISBlockPlaceListener(TARDIS plugin) {
        this.plugin = plugin;
        this.utils = new TARDISUtils(plugin);
        blocks.add(Material.IRON_BLOCK);
        blocks.add(Material.GOLD_BLOCK);
        blocks.add(Material.DIAMOND_BLOCK);
        blocks.add(Material.LAPIS_BLOCK);
        blocks.add(Material.BOOKSHELF);
        if (plugin.bukkitversion.compareTo(plugin.preemeraldversion) >= 0) {
            blocks.add(Material.EMERALD_BLOCK);
        }
        if (plugin.bukkitversion.compareTo(plugin.precomparatorversion) >= 0) {
            blocks.add(Material.REDSTONE_BLOCK);
            blocks.add(Material.QUARTZ_BLOCK);
        }
        if (plugin.bukkitversion.compareTo(plugin.precarpetversion) >= 0) {
            blocks.add(Material.COAL_BLOCK);
        }
        if (plugin.getConfig().getBoolean("custom_schematic")) {
            custom = Material.valueOf(plugin.getConfig().getString("custom_schematic_seed"));
            blocks.add(custom);
        }
    }

    /**
     * Listens for player block placing. If the player place a stack of blocks
     * in a certain pattern for example (but not limited to): IRON_BLOCK,
     * LAPIS_BLOCK, RESTONE_TORCH the pattern of blocks is turned into a TARDIS.
     *
     * @param event a player placing a block
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerBlockPlace(BlockPlaceEvent event) {
        Block block = event.getBlockPlaced();
        // only listen for redstone torches
        if (block.getType() == Material.REDSTONE_TORCH_ON) {
            Block blockBelow = block.getRelative(BlockFace.DOWN);
            final int middle_id = blockBelow.getTypeId();
            final byte middle_data = blockBelow.getData();
            Block blockBottom = blockBelow.getRelative(BlockFace.DOWN);
            // only continue if the redstone torch is placed on top of [JUST ABOUT ANY] BLOCK on top of an IRON/GOLD/DIAMOND_BLOCK
            if (plugin.getBlocksConfig().getStringList("tardis_blocks").contains(blockBelow.getType().toString()) && blocks.contains(blockBottom.getType())) {
                final TARDISConstants.SCHEMATIC schm;
                final Player player = event.getPlayer();
                int max_count = plugin.getConfig().getInt("count");
                int player_count = 0;
                if (max_count > 0) {
                    HashMap<String, Object> wherec = new HashMap<String, Object>();
                    wherec.put("player", player.getName());
                    ResultSetCount rsc = new ResultSetCount(plugin, wherec, false);
                    if (rsc.resultSet()) {
                        player_count = rsc.getCount();
                        if (player_count == max_count) {
                            player.sendMessage(plugin.pluginName + "You have used up your quota of TARDISes!");
                            return;
                        }
                    }
                }
                switch (blockBottom.getType()) {
                    case IRON_BLOCK:
                        schm = TARDISConstants.SCHEMATIC.BUDGET;
                        break;
                    case GOLD_BLOCK:
                        if (player.hasPermission("tardis.bigger")) {
                            schm = TARDISConstants.SCHEMATIC.BIGGER;
                        } else {
                            player.sendMessage(plugin.pluginName + "You don't have permission to create a 'bigger' TARDIS!");
                            return;
                        }
                        break;
                    case DIAMOND_BLOCK:
                        if (player.hasPermission("tardis.deluxe")) {
                            schm = TARDISConstants.SCHEMATIC.DELUXE;
                        } else {
                            player.sendMessage(plugin.pluginName + "You don't have permission to create a 'deluxe' TARDIS!");
                            return;
                        }
                        break;
                    case EMERALD_BLOCK:
                        if (player.hasPermission("tardis.eleventh")) {
                            schm = TARDISConstants.SCHEMATIC.ELEVENTH;
                        } else {
                            player.sendMessage(plugin.pluginName + "You don't have permission to create an 'eleventh Doctor's' TARDIS!");
                            return;
                        }
                        break;
                    case REDSTONE_BLOCK:
                        if (player.hasPermission("tardis.redstone")) {
                            schm = TARDISConstants.SCHEMATIC.REDSTONE;
                        } else {
                            player.sendMessage(plugin.pluginName + "You don't have permission to create a 'redstone' TARDIS!");
                            return;
                        }
                        break;
                    case COAL_BLOCK:
                        if (player.hasPermission("tardis.steampunk")) {
                            schm = TARDISConstants.SCHEMATIC.STEAMPUNK;
                        } else {
                            player.sendMessage(plugin.pluginName + "You don't have permission to create a 'steampunk' TARDIS!");
                            return;
                        }
                        break;
                    case QUARTZ_BLOCK:
                        if (player.hasPermission("tardis.tom")) {
                            schm = TARDISConstants.SCHEMATIC.TOM;
                        } else {
                            player.sendMessage(plugin.pluginName + "You don't have permission to create a '4th Doctor's' TARDIS!");
                            return;
                        }
                        break;
                    case BOOKSHELF:
                        if (player.hasPermission("tardis.plank")) {
                            schm = TARDISConstants.SCHEMATIC.PLANK;
                        } else {
                            player.sendMessage(plugin.pluginName + "You don't have permission to create a 'wood' TARDIS!");
                            return;
                        }
                        break;
                    default:
                        if (plugin.getConfig().getBoolean("custom_schematic")) {
                            if (player.hasPermission("tardis.custom") && blockBottom.getType().equals(custom)) {
                                schm = TARDISConstants.SCHEMATIC.CUSTOM;
                            } else {
                                player.sendMessage(plugin.pluginName + "You don't have permission to create the server's custom' TARDIS!");
                                return;
                            }
                        } else {
                            schm = TARDISConstants.SCHEMATIC.BUDGET;
                        }
                        break;
                }
                if (player.hasPermission("tardis.create")) {
                    String playerNameStr = player.getName();
                    // check to see if they already have a TARDIS
                    HashMap<String, Object> where = new HashMap<String, Object>();
                    where.put("owner", playerNameStr);
                    ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
                    if (!rs.resultSet()) {
                        Chunk chunk = blockBottom.getChunk();
                        // get this chunk co-ords
                        int cx;
                        int cz;
                        String cw;
                        final World chunkworld;
                        if (plugin.getConfig().getBoolean("create_worlds") && !plugin.getConfig().getBoolean("default_world")) {
                            // create a new world to store this TARDIS
                            cw = "TARDIS_WORLD_" + playerNameStr;
                            TARDISSpace space = new TARDISSpace(plugin);
                            chunkworld = space.getTardisWorld(cw);
                            cx = 0;
                            cz = 0;
                        } else {
                            // check config to see whether we are using a default world to store TARDISes
                            if (plugin.getConfig().getBoolean("default_world")) {
                                cw = plugin.getConfig().getString("default_world_name");
                                chunkworld = plugin.getServer().getWorld(cw);
                            } else {
                                chunkworld = chunk.getWorld();
                                cw = chunkworld.getName();
                            }
                            cx = chunk.getX();
                            cz = chunk.getZ();
                            if (utils.checkChunk(cw, cx, cz, schm)) {
                                player.sendMessage(plugin.pluginName + "A TARDIS already exists at this location, please try another chunk!");
                                return;
                            }
                        }
                        // get player direction
                        final String d = plugin.utils.getPlayersDirection(player, false);
                        // save data to database (tardis table)
                        final Location block_loc = blockBottom.getLocation();
                        String chun = cw + ":" + cx + ":" + cz;
                        //String home = block_loc.getWorld().getName() + ":" + block_loc.getBlockX() + ":" + block_loc.getBlockY() + ":" + block_loc.getBlockZ() + ":" + d + ":false";
                        String save = block_loc.getWorld().getName() + ":" + block_loc.getBlockX() + ":" + block_loc.getBlockY() + ":" + block_loc.getBlockZ() + ":" + d + ":false";
                        QueryFactory qf = new QueryFactory(plugin);
                        HashMap<String, Object> set = new HashMap<String, Object>();
                        set.put("owner", playerNameStr);
                        set.put("chunk", chun);
                        set.put("direction", d);
                        set.put("home", save);
                        set.put("save", save);
                        set.put("current", save);
                        set.put("size", schm.name());
                        HashMap<String, Object> setpp = new HashMap<String, Object>();
                        if (middle_id == 22) {
                            set.put("middle_id", 35);
                            if (plugin.bukkitversion.compareTo(plugin.preemeraldversion) >= 0 && blockBottom.getType().equals(Material.EMERALD_BLOCK)) {
                                set.put("middle_data", 8);
                                setpp.put("wall", "LIGHT_GREY_WOOL");
                            } else {
                                set.put("middle_data", 1);
                                setpp.put("wall", "ORANGE_WOOL");
                            }
                        } else {
                            set.put("middle_id", middle_id);
                            set.put("middle_data", middle_data);
                            // determine wall block material from HashMap
                            setpp.put("wall", getWallKey(middle_id, (int) middle_data));
                        }
                        final int lastInsertId = qf.doInsert("tardis", set);
                        // insert/update  player prefs
                        HashMap<String, Object> wherep = new HashMap<String, Object>();
                        wherep.put("player", player.getName());
                        ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, wherep);
                        if (!rsp.resultSet()) {
                            setpp.put("player", player.getName());
                            qf.doInsert("player_prefs", setpp);
                        } else {
                            HashMap<String, Object> wherepp = new HashMap<String, Object>();
                            wherepp.put("player", player.getName());
                            qf.doUpdate("player_prefs", setpp, wherepp);
                        }
                        // remove redstone torch/lapis and iron blocks
                        block.setTypeId(0);
                        blockBelow.setTypeId(0);
                        blockBottom.setTypeId(0);
                        // turn the block stack into a TARDIS
                        plugin.buildPB.buildPoliceBox(lastInsertId, block_loc, TARDISConstants.COMPASS.valueOf(d), false, player, false, false);
                        plugin.buildI.buildInner(schm, chunkworld, lastInsertId, player, middle_id, middle_data);
                        // set achievement completed
                        if (player.hasPermission("tardis.book")) {
                            HashMap<String, Object> seta = new HashMap<String, Object>();
                            seta.put("completed", 1);
                            HashMap<String, Object> wherea = new HashMap<String, Object>();
                            wherea.put("player", player.getName());
                            wherea.put("name", "tardis");
                            qf.doUpdate("achievements", seta, wherea);
                            TARDISAchievementNotify tan = new TARDISAchievementNotify(plugin);
                            tan.sendAchievement(player, plugin.getAchivementConfig().getString("tardis.message"), Material.valueOf(plugin.getAchivementConfig().getString("tardis.icon")));
                        }
                        if (max_count > 0) {
                            player.sendMessage(plugin.pluginName + "You have used up " + (player_count + 1) + " of " + max_count + " TARDIS builds!");
                            HashMap<String, Object> setc = new HashMap<String, Object>();
                            setc.put("count", player_count + 1);
                            if (player_count > 0) {
                                // update the player's TARDIS count
                                HashMap<String, Object> wheretc = new HashMap<String, Object>();
                                wheretc.put("player", player.getName());
                                qf.doUpdate("t_count", setc, wheretc);
                            } else {
                                // insert new TARDIS count record
                                setc.put("player", player.getName());
                                qf.doInsert("t_count", setc);
                            }
                        }
                    } else {
                        String leftLoc = rs.getCurrent();
                        String[] leftData = leftLoc.split(":");
                        player.sendMessage(plugin.pluginName + "You already have a TARDIS, you left it in " + leftData[0] + " at x:" + leftData[1] + " y:" + leftData[2] + " z:" + leftData[3]);
                    }
                } else {
                    player.sendMessage(plugin.pluginName + "You don't have permission to build a TARDIS!");
                }
            }
        }
    }

    private static String getWallKey(int i, int d) {
        TARDISWalls tw = new TARDISWalls();
        for (Map.Entry<String, Integer[]> entry : tw.blocks.entrySet()) {
            Integer[] value = entry.getValue();
            if (value[0].equals(Integer.valueOf(i)) && value[1].equals(Integer.valueOf(d))) {
                return entry.getKey();
            }
        }
        return "ORANGE_WOOL";
    }
}
