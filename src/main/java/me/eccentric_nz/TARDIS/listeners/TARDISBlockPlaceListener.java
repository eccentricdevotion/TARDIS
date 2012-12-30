/*
 * Copyright (C) 2012 eccentric_nz
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

import me.eccentric_nz.TARDIS.database.TARDISDatabase;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.utility.TARDISUtils;
import me.eccentric_nz.TARDIS.worldgen.TARDISSpace;
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
 * Listens for player block placing. If the player place a stack of blocks in a
 * certain pattern for example (but not limited to): IRON_BLOCK, LAPIS_BLOCK,
 * RESTONE_TORCH the pattern of blocks is turned into a TARDIS.
 *
 * @author eccentric_nz
 */
public class TARDISBlockPlaceListener implements Listener {

    private TARDIS plugin;
    private TARDISUtils utils;
    TARDISDatabase service = TARDISDatabase.getInstance();
    public static final List<String> MIDDLE_BLOCKS = Arrays.asList(new String[]{"LAPIS_BLOCK", "STONE", "DIRT", "WOOD", "SANDSTONE", "WOOL", "BRICK", "NETHERRACK", "SOUL_SAND", "SMOOTH_BRICK", "HUGE_MUSHROOM_1", "HUGE_MUSHROOM_2", "ENDER_STONE"});

    public TARDISBlockPlaceListener(TARDIS plugin) {
        this.plugin = plugin;
        this.utils = new TARDISUtils(plugin);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerBlockPlace(BlockPlaceEvent event) {
        if (event.isCancelled()) {
            return;
        }
        Block block = event.getBlockPlaced();
        // only listen for redstone torches
        if (block.getType() == Material.REDSTONE_TORCH_ON) {
            Block blockBelow = block.getRelative(BlockFace.DOWN);
            int middle_id = blockBelow.getTypeId();
            byte middle_data = blockBelow.getData();
            Block blockBottom = blockBelow.getRelative(BlockFace.DOWN);
            // only continue if the redstone torch is placed on top of [JUST ABOUT ANY] BLOCK on top of an IRON/GOLD/DIAMOND_BLOCK
            if (MIDDLE_BLOCKS.contains(blockBelow.getType().toString()) && (blockBottom.getType() == Material.IRON_BLOCK || blockBottom.getType() == Material.GOLD_BLOCK || blockBottom.getType() == Material.DIAMOND_BLOCK)) {
                TARDISConstants.SCHEMATIC schm;
                Player player = event.getPlayer();
                switch (blockBottom.getType()) {
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
                    default:
                        schm = TARDISConstants.SCHEMATIC.BUDGET;
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
                        World chunkworld;
                        if (plugin.getConfig().getBoolean("create_worlds")) {
                            // create a new world to store this TARDIS
                            cw = "TARDIS_WORLD_" + playerNameStr;
                            TARDISSpace space = new TARDISSpace(plugin);
                            chunkworld = space.getTardisWorld(cw);
                            cx = 0;
                            cz = 0;
                        } else {
                            // check config to see whether we are using a default world to store TARDISs
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
                        float pyaw = player.getLocation().getYaw();
                        if (pyaw >= 0) {
                            pyaw = (pyaw % 360);
                        } else {
                            pyaw = (360 + (pyaw % 360));
                        }
                        Location block_loc = blockBottom.getLocation();
                        // determine direction player is facing
                        String d = "";
                        if (pyaw >= 315 || pyaw < 45) {
                            d = "SOUTH";
                        }
                        if (pyaw >= 225 && pyaw < 315) {
                            d = "EAST";
                        }
                        if (pyaw >= 135 && pyaw < 225) {
                            d = "NORTH";
                        }
                        if (pyaw >= 45 && pyaw < 135) {
                            d = "WEST";
                        }
                        // save data to database (tardis table)
                        String chun = cw + ":" + cx + ":" + cz;
                        String home = block_loc.getWorld().getName() + ":" + block_loc.getBlockX() + ":" + block_loc.getBlockY() + ":" + block_loc.getBlockZ();
                        String save = block_loc.getWorld().getName() + ":" + block_loc.getBlockX() + ":" + block_loc.getBlockY() + ":" + block_loc.getBlockZ();
                        QueryFactory qf = new QueryFactory(plugin);
                        HashMap<String, Object> set = new HashMap<String, Object>();
                        set.put("owner", playerNameStr);
                        set.put("chunk", chun);
                        set.put("direction", d);
                        set.put("home", home);
                        set.put("save", save);
                        set.put("size", schm.name());
                        int lastInsertId = qf.doInsert("tardis", set);

                        // remove redstone torch
                        block.setTypeId(0);
                        // turn the block stack into a TARDIS
                        plugin.buildPB.buildPoliceBox(lastInsertId, block_loc, TARDISConstants.COMPASS.valueOf(d), false, player, false);
                        plugin.buildI.buildInner(schm, chunkworld, lastInsertId, player, middle_id, middle_data);
                    } else {
                        String leftLoc = rs.getSave();
                        String[] leftData = leftLoc.split(":");
                        player.sendMessage(plugin.pluginName + "You already have a TARDIS, you left it in " + leftData[0] + " at x:" + leftData[1] + " y:" + leftData[2] + " z:" + leftData[3]);
                    }
                } else {
                    player.sendMessage(plugin.pluginName + "You don't have permission to build a TARDIS!");
                }
            }
        }
    }
}