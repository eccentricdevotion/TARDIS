/*
 * Copyright (C) 2019 eccentric_nz
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
import me.eccentric_nz.TARDIS.database.*;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.SCHEMATIC;
import me.eccentric_nz.TARDIS.enumeration.WORLD_MANAGER;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import me.eccentric_nz.tardischunkgenerator.TARDISChunkGenerator;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
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
                SCHEMATIC schm = tardis.getSchematic();
                HashMap<String, Object> wherecl = new HashMap<>();
                wherecl.put("tardis_id", id);
                ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherecl);
                if (!rsc.resultSet()) {
                    return false;
                }
                Location bb_loc = new Location(rsc.getWorld(), rsc.getX(), rsc.getY(), rsc.getZ());
                DestroyData dd = new DestroyData(plugin, uuid.toString());
                dd.setDirection(rsc.getDirection());
                dd.setLocation(bb_loc);
                dd.setPlayer(plugin.getServer().getOfflinePlayer(uuid));
                dd.setHide(false);
                dd.setOutside(false);
                dd.setSubmarine(rsc.isSubmarine());
                dd.setTardisID(id);
                dd.setBiome(rsc.getBiome());
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
                Material restore = getRestore(cw);
                if (!cw.getName().toLowerCase(Locale.ENGLISH).contains("TARDIS_WORLD_")) {
                    plugin.getInteriorDestroyer().destroyInner(schm, id, cw, restore, tips);
                }
                cleanWorlds(cw, owner);
                removeZeroRoom(tips, hasZero);
                cleanDatabase(id);
                return true;
            }
        } catch (Exception e) {
            plugin.getConsole().sendMessage(plugin.getPluginName() + "TARDIS exterminate by id error: " + e);
            return false;
        }
        return true;
    }

    /**
     * Deletes a TARDIS.
     *
     * @param player running the command.
     * @param block  the block that represents the Police Box sign
     * @return true or false depending on whether the TARIS could be deleted
     */
    public boolean exterminate(Player player, Block block) {
        int signx = 0, signz = 0;
        Location sign_loc = block.getLocation();
        HashMap<String, Object> where = new HashMap<>();
        ResultSetTardis rs;
        if (player.hasPermission("tardis.delete")) {
            Block blockbehind = null;
            Directional sign = (Directional) block.getBlockData();
            if (sign.getFacing().equals(BlockFace.WEST)) {
                blockbehind = block.getRelative(BlockFace.EAST, 2);
            }
            if (sign.getFacing().equals(BlockFace.EAST)) {
                blockbehind = block.getRelative(BlockFace.WEST, 2);
            }
            if (sign.getFacing().equals(BlockFace.SOUTH)) {
                blockbehind = block.getRelative(BlockFace.NORTH, 2);
            }
            if (sign.getFacing().equals(BlockFace.NORTH)) {
                blockbehind = block.getRelative(BlockFace.SOUTH, 2);
            }
            if (blockbehind != null) {
                Block blockDown = blockbehind.getRelative(BlockFace.DOWN, 2);
                Location bd_loc = blockDown.getLocation();
                HashMap<String, Object> wherecl = new HashMap<>();
                wherecl.put("world", bd_loc.getWorld().getName());
                wherecl.put("x", bd_loc.getBlockX());
                wherecl.put("y", bd_loc.getBlockY());
                wherecl.put("z", bd_loc.getBlockZ());
                ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherecl);
                if (!rsc.resultSet()) {
                    TARDISMessage.send(player, "CURRENT_NOT_FOUND");
                    return false;
                }
                where.put("tardis_id", rsc.getTardis_id());
                rs = new ResultSetTardis(plugin, where, "", false, 2);
            } else {
                TARDISMessage.send(player, "CURRENT_NOT_FOUND");
                return false;
            }
        } else {
            where.put("uuid", player.getUniqueId().toString());
            rs = new ResultSetTardis(plugin, where, "", false, 0);
        }
        if (rs.resultSet()) {
            Tardis tardis = rs.getTardis();
            int id = tardis.getTardis_id();
            String owner = tardis.getOwner();
            String chunkLoc = tardis.getChunk();
            int tips = tardis.getTIPS();
            boolean hasZero = (!tardis.getZero().isEmpty());
            SCHEMATIC schm = tardis.getSchematic();
            // need to check that a player is not currently in the TARDIS
            if (player.hasPermission("tardis.delete")) {
                HashMap<String, Object> travid = new HashMap<>();
                travid.put("tardis_id", id);
                ResultSetTravellers rst = new ResultSetTravellers(plugin, travid, false);
                if (rst.resultSet()) {
                    TARDISMessage.send(player, "TARDIS_NO_DELETE");
                    return false;
                }
            }
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
            switch (d) {
                case EAST:
                    signx = -2;
                    break;
                case SOUTH:
                    signz = -2;
                    break;
                case WEST:
                    signx = 2;
                    break;
                case NORTH:
                    signz = 2;
                    break;
            }
            int signy = -2;
            if (sign_loc.getBlockX() == bb_loc.getBlockX() + signx && sign_loc.getBlockY() + signy == bb_loc.getBlockY() && sign_loc.getBlockZ() == bb_loc.getBlockZ() + signz) {
                // if the sign was on the TARDIS destroy the TARDIS!
                DestroyData dd = new DestroyData(plugin, player.getUniqueId().toString());
                dd.setDirection(d);
                dd.setLocation(bb_loc);
                dd.setPlayer(player);
                dd.setHide(true);
                dd.setOutside(false);
                dd.setSubmarine(rsc.isSubmarine());
                dd.setTardisID(id);
                dd.setBiome(rsc.getBiome());
                plugin.getPM().callEvent(new TARDISDestructionEvent(player, bb_loc, owner));
                if (!tardis.isHidden()) {
                    // remove Police Box
                    plugin.getPresetDestroyer().destroyPreset(dd);
                } else {
                    // restore biome
                    plugin.getUtils().restoreBiome(bb_loc, rsc.getBiome());
                }
                World cw = TARDISStaticLocationGetters.getWorld(chunkLoc);
                if (cw == null) {
                    TARDISMessage.send(player, "WORLD_DELETED");
                    return true;
                }
                Material restore = getRestore(cw);
                if (!cw.getName().toLowerCase(Locale.ENGLISH).contains("TARDIS_WORLD_")) {
                    plugin.getInteriorDestroyer().destroyInner(schm, id, cw, restore, tips);
                }
                cleanWorlds(cw, owner);
                removeZeroRoom(tips, hasZero);
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    cleanDatabase(id);
                    TARDISMessage.send(player, "TARDIS_EXTERMINATED");
                }, 40L);
                return false;
            } else {
                // cancel the event because it's not the player's TARDIS
                TARDISMessage.send(player, "NOT_OWNER");
                return false;
            }
        } else {
            TARDISMessage.send(player, "NO_GRIEF");
            return false;
        }
    }

    private Material getRestore(World w) {
        String dn = plugin.getConfig().getString("creation.default_world_name");
        if (w.getWorldType() == WorldType.FLAT || w.getName().equals(dn) || w.getGenerator() instanceof TARDISChunkGenerator) {
            return Material.AIR;
        }
        switch (w.getEnvironment()) {
            case NETHER:
                return Material.NETHERRACK;
            case THE_END:
                return Material.END_STONE;
            default:
                return Material.STONE;
        }
    }

    public void cleanHashMaps(int id) {
        // remove protected blocks from the HashMap
        HashMap<String, Object> whereb = new HashMap<>();
        whereb.put("tardis_id", id);
        ResultSetBlocks rsb = new ResultSetBlocks(plugin, whereb, true);
        if (rsb.resultSet()) {
            rsb.getData().forEach((rp) -> plugin.getGeneralKeeper().getProtectBlockMap().remove(rp.getStrLocation()));
        }
        // remove gravity well blocks from the HashMap
        HashMap<String, Object> whereg = new HashMap<>();
        whereg.put("tardis_id", id);
        ResultSetGravity rsg = new ResultSetGravity(plugin, whereg, true);
        if (rsg.resultSet()) {
            ArrayList<HashMap<String, String>> gdata = rsg.getData();
            for (HashMap<String, String> gmap : gdata) {
                int direction = TARDISNumberParsers.parseInt(gmap.get("direction"));
                switch (direction) {
                    case 1:
                        plugin.getGeneralKeeper().getGravityUpList().remove(gmap.get("location"));
                        break;
                    case 2:
                        plugin.getGeneralKeeper().getGravityNorthList().remove(gmap.get("location"));
                        break;
                    case 3:
                        plugin.getGeneralKeeper().getGravityWestList().remove(gmap.get("location"));
                        break;
                    case 4:
                        plugin.getGeneralKeeper().getGravitySouthList().remove(gmap.get("location"));
                        break;
                    case 5:
                        plugin.getGeneralKeeper().getGravityEastList().remove(gmap.get("location"));
                        break;
                    default:
                        plugin.getGeneralKeeper().getGravityDownList().remove(gmap.get("location"));
                        break;
                }
            }
        }
    }

    public void cleanDatabase(int id) {
        List<String> tables = Arrays.asList("ars", "back", "blocks", "chameleon", "chunks", "condenser", "controls", "current", "destinations", "dispersed", "doors", "farming", "gravity_well", "homes", "junk", "lamps", "next", "tardis", "thevoid", "travellers", "vaults");
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
        if (w.getName().toLowerCase(Locale.ENGLISH).contains("TARDIS_WORLD_")) {
            String name = w.getName();
            List<Player> players = w.getPlayers();
            Location spawn = plugin.getServer().getWorlds().get(0).getSpawnLocation();
            players.forEach((p) -> {
                TARDISMessage.send(p, "WORLD_RESET");
                p.teleport(spawn);
            });
            if (plugin.getWorldManager().equals(WORLD_MANAGER.MULTIVERSE)) {
                plugin.getServer().dispatchCommand(plugin.getConsole(), "mv remove " + name);
            }
            if (plugin.getWorldManager().equals(WORLD_MANAGER.MULTIWORLD)) {
                plugin.getServer().dispatchCommand(plugin.getConsole(), "mw unload " + name);
                plugin.getServer().dispatchCommand(plugin.getConsole(), "mw delete " + name);
            }
            if (plugin.getWorldManager().equals(WORLD_MANAGER.MYWORLDS)) {
                plugin.getServer().dispatchCommand(plugin.getConsole(), "myworlds unload " + name);
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
