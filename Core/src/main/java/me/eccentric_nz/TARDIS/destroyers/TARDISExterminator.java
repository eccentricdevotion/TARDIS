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
package me.eccentric_nz.TARDIS.destroyers;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.api.event.TARDISDestructionEvent;
import me.eccentric_nz.TARDIS.builders.interior.TARDISInteriorPostioning;
import me.eccentric_nz.TARDIS.builders.interior.TARDISTIPSData;
import me.eccentric_nz.TARDIS.database.data.Current;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.*;
import me.eccentric_nz.TARDIS.database.tool.Table;
import me.eccentric_nz.TARDIS.enumeration.Schematic;
import me.eccentric_nz.TARDIS.enumeration.SpaceTimeThrottle;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.enumeration.WorldManager;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.*;

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

    boolean pruneExterminate(int id) {
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
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
                ResultSetCurrentFromId rsc = new ResultSetCurrentFromId(plugin, id);
                if (!rsc.resultSet()) {
                    return false;
                }
                Current current = rsc.getCurrent();
                DestroyData dd = new DestroyData();
                dd.setDirection(current.direction());
                dd.setLocation(current.location());
                dd.setPlayer(plugin.getServer().getOfflinePlayer(uuid));
                dd.setHide(false);
                dd.setOutside(false);
                dd.setSubmarine(current.submarine());
                dd.setTardisID(id);
                dd.setThrottle(SpaceTimeThrottle.REBUILD);
                if (!hid) {
                    plugin.getPresetDestroyer().destroyPreset(dd);
                }
                // remove the inner door portal
                ResultSetDoorBlocks rsdb = new ResultSetDoorBlocks(plugin, id);
                if (rsdb.resultSet()) {
                    plugin.getTrackerKeeper().getPortals().remove(rsdb.getInnerBlock().getLocation());
                }
                cleanHashMaps(id);
                World cw = TARDISStaticLocationGetters.getWorldFromSplitString(chunkLoc);
                if (cw == null) {
                    plugin.debug("The server could not find the TARDIS world, has it been deleted?");
                    return false;
                }
                if (!cw.getName().toUpperCase(Locale.ROOT).contains("TARDIS_WORLD_")) {
                    plugin.getInteriorDestroyer().destroyInner(schm, id, cw, tips);
                }
                cleanWorlds(cw, owner);
                removeZeroRoom(tips, hasZero);
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> cleanDatabase(id), 40L);
                return true;
            }
        } catch (Exception e) {
            plugin.getMessenger().message(plugin.getConsole(), TardisModule.TARDIS, "TARDIS exterminate by id error: " + e);
            return false;
        }
        return true;
    }

    /**
     * Deletes a TARDIS.
     *
     * @param player running the command.
     * @return true or false depending on whether the TARDIS could be deleted
     */
    public boolean playerExterminate(Player player) {
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", player.getUniqueId().toString());
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
        if (rs.resultSet()) {
            Tardis tardis = rs.getTardis();
            int id = tardis.getTardisId();
            // check that there are no players in their TARDIS
            HashMap<String, Object> travid = new HashMap<>();
            travid.put("tardis_id", id);
            ResultSetTravellers rst = new ResultSetTravellers(plugin, travid, true);
            if (rst.resultSet()) {
                for (UUID uuid : rst.getData()) {
                    Player p = plugin.getServer().getPlayer(uuid);
                    if (p != null && p.isOnline()) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "TARDIS_NO_DELETE");
                        return false;
                    }
                }
            }
            String owner = tardis.getOwner();
            String chunkLoc = tardis.getChunk();
            int tips = tardis.getTIPS();
            boolean hasZero = (!tardis.getZero().isEmpty());
            Schematic schm = tardis.getSchematic();
            // check the sign location
            ResultSetCurrentFromId rsc = new ResultSetCurrentFromId(plugin, id);
            if (!rsc.resultSet()) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "CURRENT_NOT_FOUND");
                return false;
            }
            Current current = rsc.getCurrent();
            // Destroy the TARDIS!
            DestroyData dd = new DestroyData();
            dd.setDirection(current.direction());
            dd.setLocation(current.location());
            dd.setPlayer(player);
            dd.setHide(false);
            dd.setOutside(false);
            dd.setSubmarine(current.submarine());
            dd.setTardisID(id);
            dd.setThrottle(SpaceTimeThrottle.REBUILD);
            plugin.getPM().callEvent(new TARDISDestructionEvent(player, current.location(), owner));
            if (!tardis.isHidden()) {
                // remove Police Box
                plugin.getPresetDestroyer().destroyPreset(dd);
            }
            World cw = TARDISStaticLocationGetters.getWorldFromSplitString(chunkLoc);
            if (cw == null) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "WORLD_DELETED");
                return true;
            }
            if (!cw.getName().toUpperCase(Locale.ROOT).contains("TARDIS_WORLD_")) {
                plugin.getInteriorDestroyer().destroyInner(schm, id, cw, tips);
            }
            cleanHashMaps(id);
            cleanWorlds(cw, owner);
            removeZeroRoom(tips, hasZero);
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                cleanDatabase(id);
                plugin.getMessenger().send(player, TardisModule.TARDIS, "TARDIS_EXTERMINATED");
            }, 40L);
        } else {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "EXTERMINATE_NONE");
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
        // remove records from database tables
        for (Table table : Table.values()) {
            if (table.shouldClean()) {
                HashMap<String, Object> where = new HashMap<>();
                where.put("tardis_id", id);
                plugin.getQueryFactory().doDelete(table.toString(), where);
            }
        }
    }

    private void cleanWorlds(World w, String owner) {
        // remove world guard region protection
        if (plugin.isWorldGuardOnServer() && plugin.getConfig().getBoolean("preferences.use_worldguard")) {
            plugin.getWorldGuardUtils().removeRegion(w, owner);
            plugin.getWorldGuardUtils().removeRoomRegion(w, owner, "renderer");
        }
        // unload and remove the world if it's a `TARDIS_WORLD_` world
        if (w.getName().toUpperCase(Locale.ROOT).contains("TARDIS_WORLD_")) {
            String name = w.getName();
            List<Player> players = w.getPlayers();
            Location spawn = plugin.getServer().getWorlds().getFirst().getSpawnLocation();
            players.forEach((p) -> {
                plugin.getMessenger().send(p, TardisModule.TARDIS, "WORLD_RESET");
                p.teleport(spawn);
            });
            if (!plugin.getPlanetsConfig().getBoolean("planets." + name + ".enabled") && plugin.getWorldManager().equals(WorldManager.MULTIVERSE)) {
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
        if (slot != -1000001 && plugin.getConfig().getBoolean("allow.zero_room") && hasZero) {
            TARDISInteriorPostioning tips = new TARDISInteriorPostioning(plugin);
            TARDISTIPSData coords = tips.getTIPSData(slot);
            World w = plugin.getServer().getWorld("TARDIS_Zero_Room");
            if (w != null) {
                tips.reclaimZeroChunk(w, coords);
            }
        }
    }
}
