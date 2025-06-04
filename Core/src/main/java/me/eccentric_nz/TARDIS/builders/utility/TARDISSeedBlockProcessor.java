/*
 * Copyright (C) 2025 eccentric_nz
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
package me.eccentric_nz.TARDIS.builders.utility;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISBuilderInstanceKeeper;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.achievement.TARDISAchievementFactory;
import me.eccentric_nz.TARDIS.api.event.TARDISCreationEvent;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.builders.exterior.BuildData;
import me.eccentric_nz.TARDIS.builders.interior.TARDISBuildData;
import me.eccentric_nz.TARDIS.builders.interior.TARDISBuilderInner;
import me.eccentric_nz.TARDIS.builders.interior.TARDISInteriorPostioning;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItemUtils;
import me.eccentric_nz.TARDIS.database.data.Current;
import me.eccentric_nz.TARDIS.database.resultset.*;
import me.eccentric_nz.TARDIS.enumeration.*;
import me.eccentric_nz.TARDIS.floodgate.TARDISFloodgate;
import me.eccentric_nz.TARDIS.planets.TARDISAliasResolver;
import me.eccentric_nz.TARDIS.planets.TARDISSpace;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;

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
     * @param seed     the build data for this seed block
     * @param location the location of the placed seed block
     * @param player   the player who placed the seed block
     * @return true or false
     */
    public boolean processBlock(TARDISBuildData seed, Location location, Player player) {
        if (TARDISPermission.hasPermission(player, "tardis.create")) {
            int max_count = plugin.getConfig().getInt("creation.count");
            int player_count = 0;
            int grace_count = 0;
            boolean has_count = false;
            UUID uniqueId = player.getUniqueId();
            String uuid = uniqueId.toString();
            ResultSetCount rsc = new ResultSetCount(plugin, uuid);
            if (rsc.resultSet()) {
                player_count = rsc.getCount();
                grace_count = rsc.getGrace();
                has_count = true;
                if (player_count == max_count && max_count > 0) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "COUNT_QUOTA");
                    return false;
                }
            }
            String playerNameStr = player.getName();
            // check to see if they already have a TARDIS
            ResultSetTardisID rs = new ResultSetTardisID(plugin);
            if (!rs.fromUUID(uuid)) {
                if (plugin.getConfig().getBoolean("creation.check_for_home")) {
                    // check it is not another Time Lords home location
                    HashMap<String, Object> where = new HashMap<>();
                    where.put("world", location.getWorld().getName());
                    where.put("x", location.getBlockX());
                    where.put("y", location.getBlockY());
                    where.put("z", location.getBlockZ());
                    ResultSetHomeLocation rsh = new ResultSetHomeLocation(plugin, where);
                    if (rsh.resultSet()) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "TARDIS_NO_HOME");
                        return false;
                    }
                }
                Schematic schm = seed.getSchematic();
                // check perms
                if (!schm.getPermission().equals("budget") && !TARDISPermission.hasPermission(player, "tardis." + schm.getPermission())) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_PERM_TARDIS_TYPE", schm.getPermission().toUpperCase(Locale.ROOT));
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
                    if (TARDISFloodgate.shouldReplacePrefix(uniqueId)) {
                        cw = TARDISFloodgate.getPlayerWorldName(playerNameStr);
                    } else {
                        cw = "TARDIS_WORLD_" + playerNameStr;
                    }
                    TARDISSpace space = new TARDISSpace(plugin);
                    chunkworld = space.getTardisWorld(cw);
                    cx = 0;
                    cz = 0;
                } else if (plugin.getConfig().getBoolean("creation.default_world") && plugin.getConfig().getBoolean("creation.create_worlds_with_perms") && TARDISPermission.hasPermission(player, "tardis.create_world")) {
                    // create a new world to store this TARDIS
                    if (TARDISFloodgate.shouldReplacePrefix(uniqueId)) {
                        cw = TARDISFloodgate.getPlayerWorldName(playerNameStr);
                    } else {
                        cw = "TARDIS_WORLD_" + playerNameStr;
                    }
                    TARDISSpace space = new TARDISSpace(plugin);
                    chunkworld = space.getTardisWorld(cw);
                    cx = 0;
                    cz = 0;
                } else {
                    Chunk chunk = location.getChunk();
                    // check config to see whether we are using a default world to store TARDISes
                    if (plugin.getConfig().getBoolean("creation.default_world")) {
                        cw = plugin.getConfig().getString("creation.default_world_name");
                        chunkworld = TARDISAliasResolver.getWorldFromAlias(cw);
                        if (chunkworld == null) {
                            plugin.getMessenger().send(player, TardisModule.TARDIS, "TARDIS_WORLD_NOT_LOADED");
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
                if (tips) {
                    slot = new TARDISInteriorPostioning(plugin).getFreeSlot();
                    if (plugin.getConfig().getString("creation.tips_next", "HIGHEST").equalsIgnoreCase("FREE")) {
                        TARDISBuilderInstanceKeeper.getTipsSlots().add(slot);
                    }
                }
                // save data to database (tardis table)
                String chun = cw + ":" + cx + ":" + cz;
                HashMap<String, Object> set = new HashMap<>();
                // save the slot
                set.put("tips", slot);
                set.put("uuid", uuid);
                set.put("owner", playerNameStr);
                set.put("chunk", chun);
                set.put("size", schm.getPermission().toUpperCase(Locale.ROOT));
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
                String preset;
                String tmp = plugin.getConfig().getString("police_box.default_preset", "FACTORY");
                if (!tmp.contains(":")) {
                    preset = tmp.toUpperCase(Locale.ROOT);
                } else {
                    String[] split = tmp.split(":");
                    preset = "ITEM:" + split[1];
                }
                set.put("chameleon_preset", preset);
                set.put("chameleon_demat", preset);
                // determine wall block material from HashMap
                setpp.put("wall", wall_type.toString());
                setpp.put("floor", floor_type.toString());
                int lastInsertId = plugin.getQueryFactory().doSyncInsert("tardis", set);
                // insert/update player prefs
                ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, uuid);
                if (!rsp.resultSet()) {
                    setpp.put("uuid", uuid);
                    String key = (plugin.getConfig().getString("storage.database", "sqlite").equals("mysql")) ? "key_item" : "key";
                    String default_key = plugin.getConfig().getString("preferences.key");
                    setpp.put(key, default_key);
                    plugin.getQueryFactory().doSyncInsert("player_prefs", setpp);
                } else {
                    HashMap<String, Object> wherepp = new HashMap<>();
                    wherepp.put("uuid", uuid);
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
                setlocs.put("world", location.getWorld().getName());
                setlocs.put("x", location.getBlockX());
                setlocs.put("y", location.getBlockY());
                setlocs.put("z", location.getBlockZ());
                setlocs.put("direction", d);
                plugin.getQueryFactory().insertLocations(setlocs);
                // turn the block stack into a TARDIS
                BuildData bd = new BuildData(uuid);
                bd.setDirection(COMPASS.valueOf(d));
                bd.setLocation(location);
                bd.setMalfunction(false);
                bd.setOutside(true);
                bd.setPlayer(player);
                bd.setRebuild(false);
                bd.setSubmarine(isSub(location));
                bd.setTardisID(lastInsertId);
                bd.setThrottle(SpaceTimeThrottle.NORMAL);
                // police box needs to use chameleon id/data
                if (chunkworld != null) {
                    plugin.getPM().callEvent(new TARDISCreationEvent(player, lastInsertId, location));
                    TARDISBuilderInner builder = new TARDISBuilderInner(plugin, schm, chunkworld, lastInsertId, player, wall_type, floor_type, slot);
                    int task = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, builder, 1L, 3L);
                    builder.setTask(task);
                    // delay building exterior
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                        plugin.getPresetBuilder().buildPreset(bd);
                        Block block = location.getBlock();
                        block.setBlockData(TARDISConstants.AIR);
                        TARDISDisplayItemUtils.remove(block);
                    }, schm.getConsoleSize().getDelay());
                    // set achievement completed
                    if (TARDISPermission.hasPermission(player, "tardis.book")) {
                        HashMap<String, Object> seta = new HashMap<>();
                        seta.put("completed", 1);
                        HashMap<String, Object> wherea = new HashMap<>();
                        wherea.put("uuid", uuid);
                        wherea.put("name", "tardis");
                        plugin.getQueryFactory().doUpdate("achievements", seta, wherea);
                        // award advancement
                        TARDISAchievementFactory.grantAdvancement(Advancement.TARDIS, player);
                    }
                    if (max_count > 0) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "COUNT", String.format("%d", (player_count + 1)), String.format("%d", max_count));
                    }
                    HashMap<String, Object> setc = new HashMap<>();
                    setc.put("count", player_count + 1);
                    setc.put("grace", grace_count);
                    if (has_count) {
                        // update the player's TARDIS count
                        HashMap<String, Object> wheretc = new HashMap<>();
                        wheretc.put("uuid", uuid);
                        plugin.getQueryFactory().doUpdate("t_count", setc, wheretc);
                    } else {
                        // insert new TARDIS count record
                        setc.put("uuid", uuid);
                        plugin.getQueryFactory().doInsert("t_count", setc);
                    }
                    // insert an eye record
                    plugin.getQueryFactory().insertEye(lastInsertId);
                    return true;
                } else {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "TARDIS_WORLD_NOT_LOADED");
                    return false;
                }
            } else {
                ResultSetCurrentFromId rscl = new ResultSetCurrentFromId(plugin, rs.getTardisId());
                if (rscl.resultSet()) {
                    Current current = rscl.getCurrent();
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "TARDIS_HAVE", current.location().getWorld().getName() + " at x:" + current.location().getBlockX() + " y:" + current.location().getBlockY() + " z:" + current.location().getBlockZ());
                } else {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "HAVE_TARDIS");
                }
                return false;
            }
        } else {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_PERM_TARDIS");
            return false;
        }
    }

    private boolean isSub(Location l) {
        return l.getBlock().getRelative(BlockFace.UP).getType().equals(Material.WATER);
    }
}
