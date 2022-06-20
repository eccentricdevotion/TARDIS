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
package me.eccentric_nz.TARDIS.destroyers;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.api.event.TARDISDestructionEvent;
import me.eccentric_nz.TARDIS.builders.TARDISInteriorPostioning;
import me.eccentric_nz.TARDIS.builders.TARDISTIPSData;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.*;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.Schematic;
import me.eccentric_nz.TARDIS.enumeration.SpaceTimeThrottle;
import me.eccentric_nz.TARDIS.enumeration.WorldManager;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.*;
import java.util.logging.Level;

/**
 * The Daleks were a warlike race who waged war across whole civilisations and races all over the universe. Advance and
 * Attack! Attack and Destroy! Destroy and Rejoice!
 *
 * @author eccentric_nz
 */
public class TARDISExterminator {

    private final TARDIS plugin;

    public TARDISExterminator(TARDIS plugin) {
        this.plugin = plugin;
    }

    public static boolean deleteFolder(File folder) {
        File[] files = folder.listFiles();
        if (files != null) { //some JVMs return null for empty dirs
            for (File f : files) {
                if (f.isDirectory()) {
                    deleteFolder(f);
                } else if (!f.delete()) {
                    TARDIS.plugin.debug("Could not delete file");
                }
            }
        }
        folder.delete();
        return true;
    }

    boolean exterminate(int id) {
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 2);
        try {
            if (rs.resultSet()) {
                Tardis tardis = rs.getTardis();
                boolean hid = tardis.isHidden();
                String chunkLoc = tardis.getChunk();
                String owner = tardis.getOwner();
                UUID uuid = tardis.getUuid();
                int tips = tardis.getTIPS();
                boolean hasZero = (!tardis.getZero().isEmpty());
                Schematic schm = tardis.getSchematic();
                HashMap<String, Object> wherecl = new HashMap<>();
                wherecl.put("tardis_id", id);
                ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherecl);
                if (!rsc.resultSet()) {
                    return false;
                }
                Location bb_loc = new Location(rsc.getWorld(), rsc.getX(), rsc.getY(), rsc.getZ());
                DestroyData dd = new DestroyData();
                dd.setDirection(rsc.getDirection());
                dd.setLocation(bb_loc);
                dd.setPlayer(plugin.getServer().getOfflinePlayer(uuid));
                dd.setHide(false);
                dd.setOutside(false);
                dd.setSubmarine(rsc.isSubmarine());
                dd.setTardisID(id);
                dd.setThrottle(SpaceTimeThrottle.REBUILD);
                if (!hid) {
                    plugin.getPresetDestroyer().destroyPreset(dd);
                }
                // remove the inner door portal
                if (plugin.getConfig().getBoolean("preferences.walk_in_tardis")) {
                    ResultSetDoorBlocks rsdb = new ResultSetDoorBlocks(plugin, id);
                    if (rsdb.resultSet()) {
                        plugin.getTrackerKeeper().getPortals().remove(rsdb.getInnerBlock().getLocation());
                    }
                }
                cleanHashMaps(id);
                World cw = TARDISStaticLocationGetters.getWorld(chunkLoc);
                if (cw == null) {
                    plugin.debug("The server could not find the TARDIS world, has it been deleted?");
                    return false;
                }
                if (!cw.getName().toUpperCase(Locale.ENGLISH).contains("TARDIS_WORLD_")) {
                    plugin.getInteriorDestroyer().destroyInner(schm, id, cw, tips);
                }
                cleanWorlds(cw, owner);
                removeZeroRoom(tips, hasZero);
                cleanDatabase(id);
                return true;
            }
        } catch (Exception e) {
            plugin.getLogger().log(Level.INFO, "TARDIS exterminate by id error: " + e);
            return false;
        }
        return true;
    }

    /**
     * Deletes a TARDIS.
     *
     * @param player running the command.
     * @return true or false depending on whether the TARIS could be deleted
     */
    public boolean exterminate(Player player) {
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", player.getUniqueId().toString());
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
        if (rs.resultSet()) {
            Tardis tardis = rs.getTardis();
            int id = tardis.getTardis_id();
            // check that the player is not currently in their TARDIS
            HashMap<String, Object> travid = new HashMap<>();
            travid.put("tardis_id", id);
            ResultSetTravellers rst = new ResultSetTravellers(plugin, travid, false);
            if (rst.resultSet()) {
                TARDISMessage.send(player, "TARDIS_NO_DELETE");
                return false;
            }
            String owner = tardis.getOwner();
            String chunkLoc = tardis.getChunk();
            int tips = tardis.getTIPS();
            boolean hasZero = (!tardis.getZero().isEmpty());
            Schematic schm = tardis.getSchematic();
            // check the sign location
            HashMap<String, Object> wherecl = new HashMap<>();
            wherecl.put("tardis_id", id);
            ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherecl);
            if (!rsc.resultSet()) {
                TARDISMessage.send(player, "CURRENT_NOT_FOUND");
                return false;
            }
            Location bb_loc = new Location(rsc.getWorld(), rsc.getX(), rsc.getY(), rsc.getZ());
            // get TARDIS direction
            COMPASS d = rsc.getDirection();
            // Destroy the TARDIS!
            DestroyData dd = new DestroyData();
            dd.setDirection(d);
            dd.setLocation(bb_loc);
            dd.setPlayer(player);
            dd.setHide(true);
            dd.setOutside(false);
            dd.setSubmarine(rsc.isSubmarine());
            dd.setTardisID(id);
            dd.setThrottle(SpaceTimeThrottle.REBUILD);
            plugin.getPM().callEvent(new TARDISDestructionEvent(player, bb_loc, owner));
            if (!tardis.isHidden()) {
                // remove Police Box
                plugin.getPresetDestroyer().destroyPreset(dd);
            }
            World cw = TARDISStaticLocationGetters.getWorld(chunkLoc);
            if (cw == null) {
                TARDISMessage.send(player, "WORLD_DELETED");
                return true;
            }
            if (!cw.getName().toUpperCase(Locale.ENGLISH).contains("TARDIS_WORLD_")) {
                plugin.getInteriorDestroyer().destroyInner(schm, id, cw, tips);
            }
            cleanWorlds(cw, owner);
            removeZeroRoom(tips, hasZero);
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                cleanDatabase(id);
                TARDISMessage.send(player, "TARDIS_EXTERMINATED");
            }, 40L);
        } else {
            TARDISMessage.send(player, "EXTERMINATE_NONE");
        }
        return true;
    }

    public void cleanHashMaps(int id) {
        // remove protected blocks from the HashMap
        HashMap<String, Object> whereb = new HashMap<>();
        whereb.put("tardis_id", id);
        ResultSetBlocks rsb = new ResultSetBlocks(plugin, whereb, true);
        rsb.resultSetAsync((hasResult, resultSetBlocks) -> {
            if (hasResult) {
                resultSetBlocks.getData().forEach((rp) -> plugin.getGeneralKeeper().getProtectBlockMap().remove(rp.getStrLocation()));
            }
        });
        // remove gravity well blocks from the HashMap
        HashMap<String, Object> whereg = new HashMap<>();
        whereg.put("tardis_id", id);
        ResultSetGravity rsg = new ResultSetGravity(plugin, whereg, true);
        if (rsg.resultSet()) {
            ArrayList<HashMap<String, String>> gdata = rsg.getData();
            for (HashMap<String, String> gmap : gdata) {
                int direction = TARDISNumberParsers.parseInt(gmap.get("direction"));
                switch (direction) {
                    case 1 -> plugin.getGeneralKeeper().getGravityUpList().remove(gmap.get("location"));
                    case 2 -> plugin.getGeneralKeeper().getGravityNorthList().remove(gmap.get("location"));
                    case 3 -> plugin.getGeneralKeeper().getGravityWestList().remove(gmap.get("location"));
                    case 4 -> plugin.getGeneralKeeper().getGravitySouthList().remove(gmap.get("location"));
                    case 5 -> plugin.getGeneralKeeper().getGravityEastList().remove(gmap.get("location"));
                    default -> plugin.getGeneralKeeper().getGravityDownList().remove(gmap.get("location"));
                }
            }
        }
    }

    public void cleanDatabase(int id) {
        List<String> tables = Arrays.asList("ars", "back", "bind", "chameleon", "chunks", "condenser", "controls", "current", "destinations", "dispersed", "doors", "farming", "gravity_well", "homes", "junk", "lamps", "next", "room_progress", "tardis", "thevoid", "travellers", "vaults");
        // remove record from database tables
        tables.forEach((table) -> {
            HashMap<String, Object> where = new HashMap<>();
            where.put("tardis_id", id);
            plugin.getQueryFactory().doDelete(table, where);
        });
    }

    private void cleanWorlds(World w, String owner) {
        // remove world guard region protection
        if (plugin.isWorldGuardOnServer() && plugin.getConfig().getBoolean("preferences.use_worldguard")) {
            plugin.getWorldGuardUtils().removeRegion(w, owner);
            plugin.getWorldGuardUtils().removeRoomRegion(w, owner, "renderer");
        }
        // unload and remove the world if it's a `TARDIS_WORLD_` world
        if (w.getName().toUpperCase(Locale.ENGLISH).contains("TARDIS_WORLD_")) {
            String name = w.getName();
            List<Player> players = w.getPlayers();
            Location spawn = plugin.getServer().getWorlds().get(0).getSpawnLocation();
            players.forEach((p) -> {
                TARDISMessage.send(p, "WORLD_RESET");
                p.teleport(spawn);
            });
            if (plugin.getWorldManager().equals(WorldManager.MULTIVERSE)) {
                plugin.getServer().dispatchCommand(plugin.getConsole(), "mv remove " + name);
            }
            if (plugin.getPM().isPluginEnabled("WorldBorder")) {
                // wb <world> clear
                plugin.getServer().dispatchCommand(plugin.getConsole(), "wb " + name + " clear");
            }
            plugin.getServer().unloadWorld(w, true);
            File world_folder = new File(plugin.getServer().getWorldContainer() + File.separator + name + File.separator);
            if (!deleteFolder(world_folder)) {
                plugin.debug("Could not delete world <" + name + ">");
            }
        }
    }

    private void removeZeroRoom(int slot, boolean hasZero) {
        if (slot != -1 && plugin.getConfig().getBoolean("allow.zero_room") && hasZero) {
            TARDISInteriorPostioning tips = new TARDISInteriorPostioning(plugin);
            TARDISTIPSData coords = tips.getTIPSData(slot);
            World w = plugin.getServer().getWorld("TARDIS_Zero_Room");
            if (w != null) {
                tips.reclaimZeroChunk(w, coords);
            }
        }
    }
}
