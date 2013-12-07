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
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import me.eccentric_nz.tardischunkgenerator.TARDISChunkGenerator;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
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

    private final TARDIS plugin;

    public TARDISExterminator(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean exterminate(int id) {
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("tardis_id", id);
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
        try {
            if (rs.resultSet()) {
                boolean hid = rs.isHidden();
                String chunkLoc = rs.getChunk();
                String owner = rs.getOwner();
                TARDISConstants.SCHEMATIC schm = rs.getSchematic();
                HashMap<String, Object> wherecl = new HashMap<String, Object>();
                wherecl.put("tardis_id", id);
                ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherecl);
                if (!rsc.resultSet()) {
                    return false;
                }
                Location bb_loc = new Location(rsc.getWorld(), rsc.getX(), rsc.getY(), rsc.getZ());
                TARDISConstants.COMPASS d = rsc.getDirection();
                if (!hid) {
                    plugin.destroyerP.destroyPreset(bb_loc, d, id, false, false, false, null);
                }
                String[] chunkworld = chunkLoc.split(":");
                World cw = plugin.getServer().getWorld(chunkworld[0]);
                int restore = getRestore(cw);
                if (!cw.getName().contains("TARDIS_WORLD_")) {
                    plugin.destroyerI.destroyInner(schm, id, cw, restore, owner);
                }
                cleanDatabase(id);
                cleanWorlds(cw, owner);
                return true;
            }
        } catch (Exception e) {
            plugin.console.sendMessage(plugin.pluginName + "TARDIS exterminate by id error: " + e);
            return false;
        }
        return true;
    }

    /**
     * Deletes a TARDIS.
     *
     * @param player running the command.
     * @param block the block that represents the Police Box sign
     * @return true or false depending on whether the TARIS could be deleted
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
                HashMap<String, Object> wherecl = new HashMap<String, Object>();
                wherecl.put("world", bd_loc.getWorld().getName());
                wherecl.put("x", bd_loc.getBlockX());
                wherecl.put("y", bd_loc.getBlockY());
                wherecl.put("z", bd_loc.getBlockZ());
                ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherecl);
                if (!rsc.resultSet()) {
                    player.sendMessage(plugin.pluginName + ChatColor.RED + "Could not get TARDIS save location!");
                    return false;
                }
                where.put("tardis_id", rsc.getTardis_id());
            } else {
                player.sendMessage(plugin.pluginName + ChatColor.RED + "Could not get TARDIS save location!");
                return false;
            }
        } else {
            where.put("owner", playerNameStr);
        }
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
        if (rs.resultSet()) {
            int id = rs.getTardis_id();
            String chunkLoc = rs.getChunk();
            TARDISConstants.SCHEMATIC schm = rs.getSchematic();
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
            HashMap<String, Object> wherecl = new HashMap<String, Object>();
            wherecl.put("tardis_id", id);
            ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherecl);
            if (!rsc.resultSet()) {
                player.sendMessage(plugin.pluginName + ChatColor.RED + "Could not get TARDIS location from sign!");
                return false;
            }
            Location bb_loc = new Location(rsc.getWorld(), rsc.getX(), rsc.getY(), rsc.getZ());
            // get TARDIS direction
            TARDISConstants.COMPASS d = rsc.getDirection();
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
                if (!rs.isHidden()) {
                    // remove Police Box
                    plugin.destroyerP.destroyPreset(bb_loc, d, id, false, false, false, null);
                }
                String[] chunkworld = chunkLoc.split(":");
                World cw = plugin.getServer().getWorld(chunkworld[0]);
                int restore = getRestore(cw);
                if (!cw.getName().contains("TARDIS_WORLD_")) {
                    plugin.destroyerI.destroyInner(schm, id, cw, restore, playerNameStr);
                }
                cleanDatabase(id);
                cleanWorlds(cw, playerNameStr);
                player.sendMessage(plugin.pluginName + "The TARDIS was removed from the world and database successfully.");
                return true;
            } else {
                // cancel the event because it's not the player's TARDIS
                player.sendMessage(TARDISConstants.NOT_OWNER);
                return false;
            }
        } else {
            player.sendMessage("Don't grief the TARDIS!");
            return false;
        }
    }

    private int getRestore(World w) {
        World.Environment env = w.getEnvironment();
        if (w.getWorldType() == WorldType.FLAT || w.getName().equals("TARDIS_TimeVortex") || w.getGenerator() instanceof TARDISChunkGenerator) {
            return 0;
        }
        switch (env) {
            case NETHER:
                return 87;
            case THE_END:
                return 121;
            default:
                return 1;
        }
    }

    private void cleanDatabase(int id) {
        QueryFactory qf = new QueryFactory(plugin);
        // remove record from tardis table
        HashMap<String, Object> tid = new HashMap<String, Object>();
        tid.put("tardis_id", id);
        qf.doDelete("tardis", tid);
        // remove blocks from blocks table
        HashMap<String, Object> bid = new HashMap<String, Object>();
        bid.put("tardis_id", id);
        qf.doDelete("blocks", bid);
        // remove lamps from lamps table
        HashMap<String, Object> eid = new HashMap<String, Object>();
        eid.put("tardis_id", id);
        qf.doDelete("lamps", eid);
        // remove ARS data
        HashMap<String, Object> aid = new HashMap<String, Object>();
        aid.put("tardis_id", id);
        qf.doDelete("ars", aid);
        // remove doors from doors table
        HashMap<String, Object> did = new HashMap<String, Object>();
        did.put("tardis_id", id);
        qf.doDelete("doors", did);
        // remove controls from controls table
        HashMap<String, Object> oid = new HashMap<String, Object>();
        oid.put("tardis_id", id);
        qf.doDelete("controls", oid);
        // remove gravity wells
        HashMap<String, Object> gid = new HashMap<String, Object>();
        gid.put("tardis_id", id);
        qf.doDelete("gravity_well", gid);
        // remove saved destinations
        HashMap<String, Object> lid = new HashMap<String, Object>();
        lid.put("tardis_id", id);
        qf.doDelete("destinations", lid);
        // remove home location
        HashMap<String, Object> hid = new HashMap<String, Object>();
        hid.put("tardis_id", id);
        qf.doDelete("homes", hid);
        // remove current location
        HashMap<String, Object> cid = new HashMap<String, Object>();
        cid.put("tardis_id", id);
        qf.doDelete("current", cid);
        // remove next location
        HashMap<String, Object> nid = new HashMap<String, Object>();
        nid.put("tardis_id", id);
        qf.doDelete("next", nid);
        // remove back location
        HashMap<String, Object> fid = new HashMap<String, Object>();
        fid.put("tardis_id", id);
        qf.doDelete("back", fid);
        // remove travellers
        HashMap<String, Object> vid = new HashMap<String, Object>();
        vid.put("tardis_id", id);
        qf.doDelete("travellers", vid);
        // remove chunks
        HashMap<String, Object> uid = new HashMap<String, Object>();
        uid.put("tardis_id", id);
        qf.doDelete("chunks", uid);
    }

    private void cleanWorlds(World w, String owner) {
        // remove world guard region protection
        if (plugin.worldGuardOnServer && plugin.getConfig().getBoolean("use_worldguard")) {
            plugin.wgutils.removeRegion(w, owner);
        }
        // unload and remove the world if it's a TARDIS_WORLD_ world
        if (w.getName().contains("TARDIS_WORLD_")) {
            String name = w.getName();
            List<Player> players = w.getPlayers();
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
                plugin.getServer().dispatchCommand(plugin.console, "mw delete " + name);
            }
            if (plugin.pm.isPluginEnabled("My Worlds")) {
                plugin.getServer().dispatchCommand(plugin.console, "myworlds unload " + name);
            }
            if (plugin.pm.isPluginEnabled("WorldBorder")) {
                // wb <world> clear
                plugin.getServer().dispatchCommand(plugin.console, "wb " + name + " clear");
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
                } else {
                    if (!f.delete()) {
                        TARDIS.plugin.debug("Could not delete file");
                    }
                }
            }
        }
        folder.delete();
        return true;
    }
}
