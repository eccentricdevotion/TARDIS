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
package me.eccentric_nz.TARDIS.builders;

import java.util.HashMap;
import java.util.Map;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetCount;
import me.eccentric_nz.TARDIS.database.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.database.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.SCHEMATIC;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

/**
 * TARDISes are bioships that are grown from a species of coral presumably
 * indigenous to Gallifrey.
 *
 * The TARDIS had a drawing room, which the Doctor claimed to be his "private
 * study". Inside it were momentos of his many incarnations' travels.
 *
 * @author eccentric_nz
 */
public class TARDISSeedBlockProcessor {

    private final TARDIS plugin;

    public TARDISSeedBlockProcessor(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Turns a seed block, that has been right-clicked by a player into a
     * TARDIS.
     *
     * @param seed the build data for this seed block
     * @param l the location of the placed seed block
     * @param player the player who placed the seed block
     * @return true or false
     */
    public boolean processBlock(TARDISBuildData seed, Location l, Player player) {
        if (player.hasPermission("tardis.create")) {
            int max_count = plugin.getConfig().getInt("creation.count");
            int player_count = 0;
            if (max_count > 0) {
                HashMap<String, Object> wherec = new HashMap<String, Object>();
                wherec.put("uuid", player.getUniqueId().toString());
                ResultSetCount rsc = new ResultSetCount(plugin, wherec, false);
                if (rsc.resultSet()) {
                    player_count = rsc.getCount();
                    if (player_count == max_count) {
                        TARDISMessage.send(player, plugin.getPluginName() + "You have used up your quota of TARDISes!");
                        return false;
                    }
                }
            }
            String playerNameStr = player.getName();
            // check to see if they already have a TARDIS
            HashMap<String, Object> where = new HashMap<String, Object>();
            where.put("uuid", player.getUniqueId().toString());
            ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
            if (!rs.resultSet()) {
                SCHEMATIC schm = seed.getSchematic();
                switch (schm) {
                    case CUSTOM:
                        if (!plugin.getConfig().getBoolean("creation.custom_schematic")) {
                            TARDISMessage.send(player, plugin.getPluginName() + "The custom TARDIS schematic is not enabled on this server!");
                            return false;
                        } else if (!player.hasPermission("tardis.custom")) {
                            TARDISMessage.send(player, plugin.getPluginName() + "You don't have permission to create the server's custom' TARDIS!");
                            return false;
                        }
                        break;
                    case BIGGER:
                        if (!player.hasPermission("tardis.bigger")) {
                            TARDISMessage.send(player, plugin.getPluginName() + "You don't have permission to create a 'bigger' TARDIS!");
                            return false;
                        }
                        break;
                    case DELUXE:
                        if (!player.hasPermission("tardis.deluxe")) {
                            TARDISMessage.send(player, plugin.getPluginName() + "You don't have permission to create a 'deluxe' TARDIS!");
                            return false;
                        }
                        break;
                    case ELEVENTH:
                        if (!player.hasPermission("tardis.eleventh")) {
                            TARDISMessage.send(player, plugin.getPluginName() + "You don't have permission to create an 'eleventh Doctor's' TARDIS!");
                            return false;
                        }
                        break;
                    case REDSTONE:
                        if (!player.hasPermission("tardis.redstone")) {
                            TARDISMessage.send(player, plugin.getPluginName() + "You don't have permission to create a 'redstone' TARDIS!");
                            return false;
                        }
                        break;
                    case STEAMPUNK:
                        if (!player.hasPermission("tardis.steampunk")) {
                            TARDISMessage.send(player, plugin.getPluginName() + "You don't have permission to create a 'steampunk' TARDIS!");
                            return false;
                        }
                        break;
                    case TOM:
                        if (!player.hasPermission("tardis.tom")) {
                            TARDISMessage.send(player, plugin.getPluginName() + "You don't have permission to create a '4th Doctor's' TARDIS!");
                            return false;
                        }
                        break;
                    case PLANK:
                        if (!player.hasPermission("tardis.plank")) {
                            TARDISMessage.send(player, plugin.getPluginName() + "You don't have permission to create a 'wood' TARDIS!");
                            return false;
                        }
                        break;
                    case ARS:
                        if (!player.hasPermission("tardis.ars")) {
                            TARDISMessage.send(player, plugin.getPluginName() + "You don't have permission to create an 'ARS' TARDIS!");
                            return false;
                        }
                        break;
                    default:
                        break;
                }
                int cx;
                int cz;
                String cw;
                World chunkworld;
                boolean tips = false;
                // TODO name worlds without player name
                if (plugin.getConfig().getBoolean("creation.create_worlds") && !plugin.getConfig().getBoolean("creation.default_world")) {
                    // create a new world to store this TARDIS
                    cw = "TARDIS_WORLD_" + playerNameStr;
                    TARDISSpace space = new TARDISSpace(plugin);
                    chunkworld = space.getTardisWorld(cw);
                    cx = 0;
                    cz = 0;
                } else if (plugin.getConfig().getBoolean("creation.default_world") && plugin.getConfig().getBoolean("creation.create_worlds_with_perms") && player.hasPermission("tardis.create_world")) {
                    // create a new world to store this TARDIS
                    cw = "TARDIS_WORLD_" + playerNameStr;
                    TARDISSpace space = new TARDISSpace(plugin);
                    chunkworld = space.getTardisWorld(cw);
                    cx = 0;
                    cz = 0;
                } else {
                    Chunk chunk = l.getChunk();
                    // check config to see whether we are using a default world to store TARDISes
                    if (plugin.getConfig().getBoolean("creation.default_world")) {
                        cw = plugin.getConfig().getString("creation.default_world_name");
                        chunkworld = plugin.getServer().getWorld(cw);
                        tips = true;
                    } else {
                        chunkworld = chunk.getWorld();
                        cw = chunkworld.getName();
                    }
                    // get this chunk co-ords
                    cx = chunk.getX();
                    cz = chunk.getZ();
                    if (!plugin.getConfig().getBoolean("creation.default_world") && plugin.getUtils().checkChunk(cw, cx, cz, schm)) {
                        TARDISMessage.send(player, plugin.getPluginName() + "A TARDIS already exists at this location, please try another chunk!");
                        return false;
                    }
                }
                // get player direction
                String d = plugin.getUtils().getPlayersDirection(player, false);
                // save data to database (tardis table)
                String chun = cw + ":" + cx + ":" + cz;
                QueryFactory qf = new QueryFactory(plugin);
                HashMap<String, Object> set = new HashMap<String, Object>();
                set.put("uuid", player.getUniqueId().toString());
                set.put("owner", playerNameStr);
                set.put("chunk", chun);
                set.put("size", schm.name());
                HashMap<String, Object> setpp = new HashMap<String, Object>();
                int middle_id = seed.getWall_id();
                byte middle_data = seed.getWall_data();
                int floor_id = seed.getFloor_id();
                byte floor_data = seed.getFloor_data();
                int c_id = seed.getBox_id();
                byte c_data = seed.getBox_data();
                set.put("middle_id", middle_id);
                set.put("middle_data", middle_data);
                set.put("chameleon_id", c_id);
                set.put("chameleon_data", c_data);
                Long now;
                if (player.hasPermission("tardis.prune.bypass")) {
                    now = Long.MAX_VALUE;
                } else {
                    now = System.currentTimeMillis();
                }
                set.put("lastuse", now);
                // determine wall block material from HashMap
                setpp.put("wall", getWallKey(middle_id, (int) middle_data));
                setpp.put("floor", getWallKey(floor_id, (int) floor_data));
                setpp.put("lamp", seed.getLamp());
                int lastInsertId = qf.doSyncInsert("tardis", set);
                // insert/update  player prefs
                HashMap<String, Object> wherep = new HashMap<String, Object>();
                wherep.put("uuid", player.getUniqueId().toString());
                ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, wherep);
                if (!rsp.resultSet()) {
                    setpp.put("uuid", player.getUniqueId().toString());
                    String key = (plugin.getConfig().getString("storage.database").equals("mysql")) ? "key_item" : "key";
                    String default_key = plugin.getConfig().getString("preferences.key");
                    setpp.put(key, default_key);
                    qf.doSyncInsert("player_prefs", setpp);
                } else {
                    HashMap<String, Object> wherepp = new HashMap<String, Object>();
                    wherepp.put("uuid", player.getUniqueId().toString());
                    qf.doUpdate("player_prefs", setpp, wherepp);
                }
                // populate home, current, next and back tables
                HashMap<String, Object> setlocs = new HashMap<String, Object>();
                setlocs.put("tardis_id", lastInsertId);
                setlocs.put("world", l.getWorld().getName());
                setlocs.put("x", l.getBlockX());
                setlocs.put("y", l.getBlockY());
                setlocs.put("z", l.getBlockZ());
                setlocs.put("direction", d);
                qf.insertLocations(setlocs);
                // set the biome if necessary
                if (plugin.getConfig().getBoolean("police_box.set_biome")) {
                    // remember the current biome
                    qf.saveBiome(lastInsertId, l.getBlock().getBiome().toString());
                }
                // turn the block stack into a TARDIS
                TARDISMaterialisationData pbd = new TARDISMaterialisationData();
                pbd.setChameleon(false);
                pbd.setDirection(COMPASS.valueOf(d));
                pbd.setLocation(l);
                pbd.setMalfunction(false);
                pbd.setOutside(true);
                pbd.setPlayer(player);
                pbd.setRebuild(false);
                pbd.setSubmarine(isSub(l));
                pbd.setTardisID(lastInsertId);
                // police box needs to use chameleon id/data
                plugin.getPresetBuilder().buildPreset(pbd);
                plugin.getInteriorBuilder().buildInner(schm, chunkworld, lastInsertId, player, middle_id, middle_data, floor_id, floor_data, tips);
                // set achievement completed
                if (player.hasPermission("tardis.book")) {
                    HashMap<String, Object> seta = new HashMap<String, Object>();
                    seta.put("completed", 1);
                    HashMap<String, Object> wherea = new HashMap<String, Object>();
                    wherea.put("uuid", player.getUniqueId().toString());
                    wherea.put("name", "tardis");
                    qf.doUpdate("achievements", seta, wherea);
                    TARDISMessage.send(player, ChatColor.YELLOW + "Achievement Get!");
                    TARDISMessage.send(player, ChatColor.WHITE + plugin.getAchievementConfig().getString("tardis.message"));
                }
                if (max_count > 0) {
                    TARDISMessage.send(player, plugin.getPluginName() + "You have used up " + (player_count + 1) + " of " + max_count + " TARDIS builds!");
                    HashMap<String, Object> setc = new HashMap<String, Object>();
                    setc.put("count", player_count + 1);
                    if (player_count > 0) {
                        // update the player's TARDIS count
                        HashMap<String, Object> wheretc = new HashMap<String, Object>();
                        wheretc.put("uuid", player.getUniqueId().toString());
                        qf.doUpdate("t_count", setc, wheretc);
                    } else {
                        // insert new TARDIS count record
                        setc.put("uuid", player.getUniqueId().toString());
                        qf.doInsert("t_count", setc);
                    }
                }
                return true;
            } else {
                HashMap<String, Object> wherecl = new HashMap<String, Object>();
                wherecl.put("tardis_id", rs.getTardis_id());
                ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherecl);
                if (rsc.resultSet()) {
                    TARDISMessage.send(player, plugin.getPluginName() + "You already have a TARDIS, you left it in " + rsc.getWorld().getName() + " at x:" + rsc.getX() + " y:" + rsc.getY() + " z:" + rsc.getZ());
                } else {
                    TARDISMessage.send(player, plugin.getPluginName() + "You already have a TARDIS, but we couldn't find it! Try calling it to you.");
                }
                return false;
            }
        } else {
            TARDISMessage.send(player, plugin.getPluginName() + "You don't have permission to build a TARDIS!");
            return false;
        }
    }

    private String getWallKey(int i, int d) {
        for (Map.Entry<String, int[]> entry : plugin.getTardisWalls().blocks.entrySet()) {
            int[] value = entry.getValue();
            if (value[0] == i && value[1] == d) {
                return entry.getKey();
            }
        }
        return "ORANGE_WOOL";
    }

    private boolean isSub(Location l) {
        switch (l.getBlock().getRelative(BlockFace.UP).getType()) {
            case STATIONARY_WATER:
            case WATER:
                return true;
            default:
                return false;
        }
    }
}
