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
package me.eccentric_nz.TARDIS.destroyers;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import static me.eccentric_nz.TARDIS.TARDISConstants.COMPASS.EAST;
import static me.eccentric_nz.TARDIS.TARDISConstants.COMPASS.NORTH;
import static me.eccentric_nz.TARDIS.TARDISConstants.COMPASS.SOUTH;
import static me.eccentric_nz.TARDIS.TARDISConstants.COMPASS.WEST;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import static org.bukkit.World.Environment.NETHER;
import static org.bukkit.World.Environment.THE_END;
import org.bukkit.WorldType;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

/**
 * The Daleks were a warlike race who waged war across whole civilisations and
 * races all over the universe. Advance and Attack! Attack and Destroy! Destroy
 * and Rejoice!
 *
 * @author eccentric_nz
 */
public class TARDISExterminator {

    private TARDIS plugin;

    public TARDISExterminator(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Deletes the TARDIS.
     *
     * @param player running the command.
     * @param block the block that represents the Police Box sign
     */
    public boolean exterminate(Player player, Block block) {
        int signx = 0, signz = 0;
        String playerNameStr = player.getName();
        Location sign_loc = block.getLocation();
        HashMap<String, Object> where = new HashMap<String, Object>();
        if (player.hasPermission("tardis.delete")) {
            Block blockbehind = null;
            byte data = block.getData();
            if (data == 4) {
                blockbehind = block.getRelative(BlockFace.EAST, 2);
            }
            if (data == 5) {
                blockbehind = block.getRelative(BlockFace.WEST, 2);
            }
            if (data == 3) {
                blockbehind = block.getRelative(BlockFace.NORTH, 2);
            }
            if (data == 2) {
                blockbehind = block.getRelative(BlockFace.SOUTH, 2);
            }
            if (blockbehind != null) {
                Block blockDown = blockbehind.getRelative(BlockFace.DOWN, 2);
                Location bd_loc = blockDown.getLocation();
                String bd_str = bd_loc.getWorld().getName() + ":" + bd_loc.getBlockX() + ":" + bd_loc.getBlockY() + ":" + bd_loc.getBlockZ();
                where.put("save", bd_str);
            } else {
                player.sendMessage(plugin.pluginName + ChatColor.RED + "Could not get TARDIS save location!");
                return false;
            }
        } else {
            where.put("owner", playerNameStr);
        }
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
        try {
            if (rs.resultSet()) {
                int id = rs.getTardis_id();
                String saveLoc = (plugin.tardisHasDestination.containsKey(id)) ? rs.getCurrent() : rs.getSave();
                String chunkLoc = rs.getChunk();
                String owner = rs.getOwner();
                TARDISConstants.SCHEMATIC schm = rs.getSchematic();
                TARDISConstants.COMPASS d = rs.getDirection();
                // need to check that a player is not currently in the TARDIS
                if (player.hasPermission("tardis.delete")) {
                    HashMap<String, Object> travid = new HashMap<String, Object>();
                    travid.put("tardis_id", id);
                    ResultSetTravellers rst = new ResultSetTravellers(plugin, travid, false);
                    if (rst.resultSet()) {
                        player.sendMessage(plugin.pluginName + ChatColor.RED + "You cannot delete this TARDIS as it is occupied!");
                        return false;
                    }
                }
                // check the sign location
                Location bb_loc = plugin.utils.getLocationFromDB(saveLoc, 0F, 0F);
                // get TARDIS direction
                switch (d) {
                    case EAST:
                        signx = -2;
                        signz = 0;
                        break;
                    case SOUTH:
                        signx = 0;
                        signz = -2;
                        break;
                    case WEST:
                        signx = 2;
                        signz = 0;
                        break;
                    case NORTH:
                        signx = 0;
                        signz = 2;
                        break;
                }
                int signy = -2;
                // if the sign was on the TARDIS destroy the TARDIS!
                if (sign_loc.getBlockX() == bb_loc.getBlockX() + signx && sign_loc.getBlockY() + signy == bb_loc.getBlockY() && sign_loc.getBlockZ() == bb_loc.getBlockZ() + signz) {
                    // clear the torch
                    plugin.destroyPB.destroyTorch(bb_loc);
                    plugin.destroyPB.destroySign(bb_loc, d);
                    // also remove the location of the chunk from chunks table
                    String[] chunkworld = chunkLoc.split(":");
                    World cw = plugin.getServer().getWorld(chunkworld[0]);
                    World.Environment env = cw.getEnvironment();
                    int restore;
                    switch (env) {
                        case NETHER:
                            restore = 87;
                            break;
                        case THE_END:
                            restore = 121;
                            break;
                        default:
                            restore = 1;
                    }
                    QueryFactory qf = new QueryFactory(plugin);
                    if (cw.getWorldType() == WorldType.FLAT) {
                        restore = 0;
                    }
                    if (!cw.getName().contains("TARDIS_WORLD_")) {
                        plugin.destroyI.destroyInner(schm, id, cw, restore, playerNameStr);
                    }
                    plugin.destroyPB.destroyPoliceBox(bb_loc, d, id, false);
                    // remove record from tardis table
                    HashMap<String, Object> tid = new HashMap<String, Object>();
                    tid.put("tardis_id", id);
                    qf.doDelete("tardis", tid);
                    // remove blocks from blocks table
                    HashMap<String, Object> bid = new HashMap<String, Object>();
                    bid.put("tardis_id", id);
                    qf.doDelete("blocks", bid);
                    // remove doors from doors table
                    HashMap<String, Object> did = new HashMap<String, Object>();
                    did.put("tardis_id", id);
                    qf.doDelete("doors", did);
                    // remove gravity wells
                    HashMap<String, Object> gid = new HashMap<String, Object>();
                    gid.put("tardis_id", id);
                    qf.doDelete("gravity_well", gid);
                    // remove saved destinations
                    HashMap<String, Object> lid = new HashMap<String, Object>();
                    lid.put("tardis_id", id);
                    qf.doDelete("destinations", lid);
                    HashMap<String, Object> vid = new HashMap<String, Object>();
                    vid.put("tardis_id", id);
                    qf.doDelete("travellers", vid);
                    HashMap<String, Object> cid = new HashMap<String, Object>();
                    cid.put("tardis_id", id);
                    qf.doDelete("chunks", cid);
                    player.sendMessage(plugin.pluginName + "The TARDIS was removed from the world and database successfully.");
                    // remove world guard region protection
                    if (plugin.worldGuardOnServer && plugin.getConfig().getBoolean("use_worldguard")) {
                        plugin.wgchk.removeRegion(cw, owner);
                    }
                    // unload and remove the world if it's a TARDIS_WORLD_ world
                    if (cw.getName().contains("TARDIS_WORLD_")) {
                        String name = cw.getName();
                        List<Player> players = cw.getPlayers();
                        Location spawn = plugin.getServer().getWorlds().get(0).getSpawnLocation();
                        for (Player p : players) {
                            p.sendMessage(plugin.pluginName + "World scheduled for deletion, teleporting you to spawn!");
                            p.teleport(spawn);
                        }
                        if (plugin.pm.isPluginEnabled("Multiverse-Core")) {
                            plugin.getServer().dispatchCommand(plugin.console, "mv remove " + name);
                        }
                        if (plugin.pm.isPluginEnabled("MultiWorld")) {
                            plugin.getServer().dispatchCommand(plugin.console, "mw unload " + name);
                        }
                        if (plugin.pm.isPluginEnabled("WorldBorder")) {
                            // wb <world> clear
                            plugin.getServer().dispatchCommand(plugin.console, "wb " + name + " clear");
                        }
                        plugin.getServer().unloadWorld(cw, true);
                        File world_folder = new File(plugin.getServer().getWorldContainer() + File.separator + name + File.separator);
                        if (!deleteFolder(world_folder)) {
                            plugin.debug("Could not delete world <" + name + ">");
                        }
                        return true;
                    }
                } else {
                    // cancel the event because it's not the player's TARDIS
                    player.sendMessage(TARDISConstants.NOT_OWNER);
                    return false;
                }
            } else {
                player.sendMessage("Don't grief the TARDIS!");
                return false;
            }
        } catch (Exception e) {
            plugin.console.sendMessage(plugin.pluginName + "Block Break Listener Error: " + e);
        } finally {
            return false;
        }
    }

    public static boolean deleteFolder(File folder) {
        File[] files = folder.listFiles();
        if (files != null) { //some JVMs return null for empty dirs
            for (File f : files) {
                if (f.isDirectory()) {
                    deleteFolder(f);
                } else {
                    f.delete();
                }
            }
        }
        folder.delete();
        return true;
    }
}
