/*
 * Copyright (C) 2022 eccentric_nz
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

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISBuilderInstanceKeeper;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.achievement.TARDISAchievementFactory;
import me.eccentric_nz.TARDIS.api.event.TARDISCreationEvent;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.database.resultset.*;
import me.eccentric_nz.TARDIS.enumeration.Advancement;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.Schematic;
import me.eccentric_nz.TARDIS.enumeration.SpaceTimeThrottle;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import me.eccentric_nz.TARDIS.planets.TARDISAliasResolver;
import me.eccentric_nz.TARDIS.planets.TARDISSpace;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Locale;

/**
 * TARDISes are bioships that are grown from a species of coral presumably indigenous to Gallifrey.
 * <p>
 * The TARDIS had a drawing room, which the Doctor claimed to be his "private study". Inside it were momentos of his
 * many incarnations' travels.
 *
 * @author eccentric_nz
 */
public class TARDISSeedBlockProcessor {

    private final TARDIS plugin;

    public TARDISSeedBlockProcessor(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Turns a seed block, that has been right-clicked by a player into a TARDIS.
     *
     * @param seed   the build data for this seed block
     * @param l      the location of the placed seed block
     * @param player the player who placed the seed block
     * @return true or false
     */
    public boolean processBlock(TARDISBuildData seed, Location l, Player player) {
        if (TARDISPermission.hasPermission(player, "tardis.create")) {
            int max_count = plugin.getConfig().getInt("creation.count");
            int player_count = 0;
            int grace_count = 0;
            boolean has_count = false;
            ResultSetCount rsc = new ResultSetCount(plugin, player.getUniqueId().toString());
            if (rsc.resultSet()) {
                player_count = rsc.getCount();
                grace_count = rsc.getGrace();
                has_count = true;
                if (player_count == max_count && max_count > 0) {
                    TARDISMessage.send(player, "COUNT_QUOTA");
                    return false;
                }
            }
            String playerNameStr = player.getName();
            // check to see if they already have a TARDIS
            ResultSetTardisID rs = new ResultSetTardisID(plugin);
            if (!rs.fromUUID(player.getUniqueId().toString())) {
                if (plugin.getConfig().getBoolean("creation.check_for_home")) {
                    // check it is not another Time Lords home location
                    HashMap<String, Object> where = new HashMap<>();
                    where.put("world", l.getWorld().getName());
                    where.put("x", l.getBlockX());
                    where.put("y", l.getBlockY());
                    where.put("z", l.getBlockZ());
                    ResultSetHomeLocation rsh = new ResultSetHomeLocation(plugin, where);
                    if (rsh.resultSet()) {
                        TARDISMessage.send(player, "TARDIS_NO_HOME");
                        return false;
                    }
                }
                Schematic schm = seed.getSchematic();
                // check perms
                if (!schm.getPermission().equals("budget") && !TARDISPermission.hasPermission(player, "tardis." + schm.getPermission())) {
                    TARDISMessage.send(player, "NO_PERM_TARDIS_TYPE", schm.getPermission().toUpperCase(Locale.ENGLISH));
                    return false;
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
                } else if (plugin.getConfig().getBoolean("creation.default_world") && plugin.getConfig().getBoolean("creation.create_worlds_with_perms") && TARDISPermission.hasPermission(player, "tardis.create_world")) {
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
                        chunkworld = TARDISAliasResolver.getWorldFromAlias(cw);
                        if (chunkworld == null) {
                            TARDISMessage.send(player, "TARDIS_WORLD_NOT_LOADED");
                            return false;
                        }
                        tips = true;
                    } else {
                        chunkworld = chunk.getWorld();
                        cw = chunkworld.getName();
                    }
                    // get this chunk co-ords
                    cx = chunk.getX();
                    cz = chunk.getZ();
                }
                // get player direction
                String d = TARDISStaticUtils.getPlayersDirection(player, false);
                // get TIPs slot
                int slot = -1000001;
                if (schm.getPermission().equals("junk")) {
                    slot = -999;
                } else if (tips) {
                    slot = new TARDISInteriorPostioning(plugin).getFreeSlot();
                    TARDISBuilderInstanceKeeper.getTipsSlots().add(slot);
                }
                // save data to database (tardis table)
                String chun = cw + ":" + cx + ":" + cz;
                HashMap<String, Object> set = new HashMap<>();
                // save the slot
                set.put("tips", slot);
                set.put("uuid", player.getUniqueId().toString());
                set.put("owner", playerNameStr);
                set.put("chunk", chun);
                set.put("size", schm.getPermission().toUpperCase(Locale.ENGLISH));
                HashMap<String, Object> setpp = new HashMap<>();
                Material wall_type = seed.getWallType();
                Material floor_type = seed.getFloorType();
                long now;
                if (TARDISPermission.hasPermission(player, "tardis.prune.bypass")) {
                    now = Long.MAX_VALUE;
                } else {
                    now = System.currentTimeMillis();
                }
                set.put("lastuse", now);
                // set preset if default is not 'FACTORY'
                String preset = plugin.getConfig().getString("police_box.default_preset", "FACTORY").toUpperCase(Locale.ENGLISH);
                set.put("chameleon_preset", preset);
                set.put("chameleon_demat", preset);
                // determine wall block material from HashMap
                setpp.put("wall", wall_type.toString());
                setpp.put("floor", floor_type.toString());
                setpp.put("lanterns_on", (schm.getPermission().equals("eleventh") || schm.getPermission().equals("twelfth")) ? 1 : 0);
                int lastInsertId = plugin.getQueryFactory().doSyncInsert("tardis", set);
                // insert/update player prefs
                ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, player.getUniqueId().toString());
                if (!rsp.resultSet()) {
                    setpp.put("uuid", player.getUniqueId().toString());
                    String key = (plugin.getConfig().getString("storage.database", "sqlite").equals("mysql")) ? "key_item" : "key";
                    String default_key = plugin.getConfig().getString("preferences.key");
                    setpp.put(key, default_key);
                    plugin.getQueryFactory().doSyncInsert("player_prefs", setpp);
                } else {
                    HashMap<String, Object> wherepp = new HashMap<>();
                    wherepp.put("uuid", player.getUniqueId().toString());
                    plugin.getQueryFactory().doUpdate("player_prefs", setpp, wherepp);
                }
                if (plugin.getConfig().getBoolean("allow.mob_farming")) {
                    // insert farming record
                    HashMap<String, Object> setf = new HashMap<>();
                    setf.put("tardis_id", lastInsertId);
                    plugin.getQueryFactory().doInsert("farming", setf);
                }
                // populate home, current, next and back tables
                HashMap<String, Object> setlocs = new HashMap<>();
                setlocs.put("tardis_id", lastInsertId);
                setlocs.put("world", l.getWorld().getName());
                setlocs.put("x", l.getBlockX());
                setlocs.put("y", l.getBlockY());
                setlocs.put("z", l.getBlockZ());
                setlocs.put("direction", d);
                plugin.getQueryFactory().insertLocations(setlocs);
                // turn the block stack into a TARDIS
                BuildData bd = new BuildData(player.getUniqueId().toString());
                bd.setDirection(COMPASS.valueOf(d));
                bd.setLocation(l);
                bd.setMalfunction(false);
                bd.setOutside(true);
                bd.setPlayer(player);
                bd.setRebuild(false);
                bd.setSubmarine(isSub(l));
                bd.setTardisID(lastInsertId);
                bd.setThrottle(SpaceTimeThrottle.NORMAL);
                // police box needs to use chameleon id/data
                if (chunkworld != null) {
                    plugin.getPM().callEvent(new TARDISCreationEvent(player, lastInsertId, l));
                    TARDISBuilderInner builder = new TARDISBuilderInner(plugin, schm, chunkworld, lastInsertId, player, wall_type, floor_type, slot);
                    int task = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, builder, 1L, 3L);
                    builder.setTask(task);
                    // delay building exterior
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                        plugin.getPresetBuilder().buildPreset(bd);
                        l.getBlock().setBlockData(TARDISConstants.AIR);
                    }, schm.getConsoleSize().getDelay());
                    // set achievement completed
                    if (TARDISPermission.hasPermission(player, "tardis.book")) {
                        HashMap<String, Object> seta = new HashMap<>();
                        seta.put("completed", 1);
                        HashMap<String, Object> wherea = new HashMap<>();
                        wherea.put("uuid", player.getUniqueId().toString());
                        wherea.put("name", "tardis");
                        plugin.getQueryFactory().doUpdate("achievements", seta, wherea);
                        // award advancement
                        TARDISAchievementFactory.grantAdvancement(Advancement.TARDIS, player);
                    }
                    if (max_count > 0) {
                        TARDISMessage.send(player, "COUNT", String.format("%d", (player_count + 1)), String.format("%d", max_count));
                    }
                    HashMap<String, Object> setc = new HashMap<>();
                    setc.put("count", player_count + 1);
                    setc.put("grace", grace_count);
                    if (has_count) {
                        // update the player's TARDIS count
                        HashMap<String, Object> wheretc = new HashMap<>();
                        wheretc.put("uuid", player.getUniqueId().toString());
                        plugin.getQueryFactory().doUpdate("t_count", setc, wheretc);
                    } else {
                        // insert new TARDIS count record
                        setc.put("uuid", player.getUniqueId().toString());
                        plugin.getQueryFactory().doInsert("t_count", setc);
                    }
                    return true;
                } else {
                    TARDISMessage.send(player, "TARDIS_WORLD_NOT_LOADED");
                    return false;
                }
            } else {
                HashMap<String, Object> wherecl = new HashMap<>();
                wherecl.put("tardis_id", rs.getTardis_id());
                ResultSetCurrentLocation rscl = new ResultSetCurrentLocation(plugin, wherecl);
                if (rscl.resultSet()) {
                    TARDISMessage.send(player, "TARDIS_HAVE", rscl.getWorld().getName() + " at x:" + rscl.getX() + " y:" + rscl.getY() + " z:" + rscl.getZ());
                } else {
                    TARDISMessage.send(player, "HAVE_TARDIS");
                }
                return false;
            }
        } else {
            TARDISMessage.send(player, "NO_PERM_TARDIS");
            return false;
        }
    }

    private boolean isSub(Location l) {
        return l.getBlock().getRelative(BlockFace.UP).getType().equals(Material.WATER);
    }
}
