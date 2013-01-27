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

import java.io.File;
import me.eccentric_nz.TARDIS.database.TARDISDatabase;
import java.util.HashMap;
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldType;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

/**
 * The Silurians, also known as Earth Reptiles, Eocenes, Homo reptilia and
 * Psionosauropodomorpha, are a species of Earth reptile. Technologically
 * advanced, they live alongside their aquatic cousins, the Sea Devils.
 *
 * @author eccentric_nz
 */
public class TARDISBlockBreakListener implements Listener {

    private TARDIS plugin;
    TARDISDatabase service = TARDISDatabase.getInstance();

    public TARDISBlockBreakListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Listens for the TARDIS Police Box sign being broken. If the sign is
     * broken, then the TARDIS is destroyed and the database records removed.
     */
    @EventHandler(priority = EventPriority.LOW)
    public void onSignBreak(BlockBreakEvent event) {
        int signx = 0, signz = 0;
        Player player = event.getPlayer();
        String playerNameStr = player.getName();
        float yaw = player.getLocation().getYaw();
        float pitch = player.getLocation().getPitch();
        Block block = event.getBlock();
        Material blockType = block.getType();
        if (blockType == Material.WALL_SIGN) {
            // check the text on the sign
            Sign sign = (Sign) block.getState();
            String line1 = sign.getLine(1);
            String line2 = sign.getLine(2);
            Location sign_loc = block.getLocation();
            if (line1.equals(ChatColor.WHITE + "POLICE") && line2.equals(ChatColor.WHITE + "BOX")) {
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
                    Block blockDown = blockbehind.getRelative(BlockFace.DOWN, 2);
                    Location bd_loc = blockDown.getLocation();
                    String bd_str = bd_loc.getWorld().getName() + ":" + bd_loc.getBlockX() + ":" + bd_loc.getBlockY() + ":" + bd_loc.getBlockZ();
                    where.put("save", bd_str);
                } else {
                    where.put("owner", playerNameStr);
                }
                ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
                occupied:
                try {
                    if (rs.resultSet()) {
                        String saveLoc = rs.getSave();
                        String chunkLoc = rs.getChunk();
                        String owner = rs.getOwner();
                        TARDISConstants.SCHEMATIC schm = rs.getSchematic();
                        int id = rs.getTardis_id();
                        TARDISConstants.COMPASS d = rs.getDirection();
                        // need to check that a player is not currently in the TARDIS (if admin delete - maybe always?)
                        if (player.hasPermission("tardis.delete")) {
                            HashMap<String, Object> tid = new HashMap<String, Object>();
                            tid.put("tardis_id", id);
                            ResultSetTravellers rst = new ResultSetTravellers(plugin, tid, false);
                            if (rst.resultSet()) {
                                player.sendMessage(plugin.pluginName + ChatColor.RED + " You cannot delete this TARDIS as it is occupied!");
                                event.setCancelled(true);
                                sign.update();
                                break occupied;
                            }
                        }
                        // check the sign location
                        Location bb_loc = plugin.utils.getLocationFromDB(saveLoc, yaw, pitch);
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
                            int cwx = 0, cwz = 0;
                            // clear the torch
                            plugin.destroyPB.destroyTorch(bb_loc);
                            plugin.destroyPB.destroySign(bb_loc, d);
                            // also remove the location of the chunk from chunks table
                            String[] chunkworld = chunkLoc.split(":");
                            World cw = plugin.getServer().getWorld(chunkworld[0]);
                            Environment env = cw.getEnvironment();
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
                            HashMap<String, Object> cid = new HashMap<String, Object>();
                            cid.put("tardis_id", id);
                            qf.doDelete("chunks", cid);
                            if (cw.getWorldType() == WorldType.FLAT || cw.getName().contains("TARDIS_WORLD_")) {
                                restore = 0;
                            }
                            plugin.destroyI.destroyInner(schm, id, cw, restore, playerNameStr);
                            plugin.destroyPB.destroyPoliceBox(bb_loc, d, id, false);
                            // remove record from tardis table
                            HashMap<String, Object> tid = new HashMap<String, Object>();
                            tid.put("tardis_id", id);
                            qf.doDelete("tardis", tid);
                            // remove doors from doors table
                            HashMap<String, Object> did = new HashMap<String, Object>();
                            did.put("tardis_id", id);
                            qf.doDelete("doors", did);
                            player.sendMessage(plugin.pluginName + "The TARDIS was removed from the world and database successfully.");
                            // remove world guard region protection
                            if (plugin.worldGuardOnServer && plugin.getConfig().getBoolean("use_worldguard")) {
                                plugin.wgchk.removeRegion(cw, owner);
                            }
                            // unload and remove the world if it's a TARDIS_WORLD_ world
                            if (cw.getName().contains("TARDIS_WORLD_")) {
                                String name = cw.getName();
                                List<Player> players = cw.getPlayers();
                                for (Player p : players) {
                                    p.kickPlayer("World scheduled for deletion!");
                                }
                                if (plugin.pm.isPluginEnabled("Multiverse-Core")) {
                                    plugin.getServer().dispatchCommand(plugin.console, "mv remove " + name);
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
                            }
                        } else {
                            // cancel the event because it's not the player's TARDIS
                            event.setCancelled(true);
                            sign.update();
                            player.sendMessage(TARDISConstants.NOT_OWNER);
                        }
                    } else {
                        event.setCancelled(true);
                        sign.update();
                        player.sendMessage("Don't grief the TARDIS!");
                    }
                } catch (Exception e) {
                    plugin.console.sendMessage(plugin.pluginName + "Block Break Listener Error: " + e);
                }
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
                    f.delete();
                }
            }
        }
        folder.delete();
        return true;
    }
}
