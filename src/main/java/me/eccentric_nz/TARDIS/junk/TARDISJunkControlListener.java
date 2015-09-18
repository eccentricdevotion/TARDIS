/*
 * Copyright (C) 2015 eccentric_nz
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
package me.eccentric_nz.TARDIS.junk;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.api.Parameters;
import me.eccentric_nz.TARDIS.builders.TARDISMaterialisationData;
import me.eccentric_nz.TARDIS.database.ResultSetControls;
import me.eccentric_nz.TARDIS.database.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.FLAG;
import me.eccentric_nz.TARDIS.enumeration.PRESET;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 *
 * @author eccentric_nz
 */
public class TARDISJunkControlListener implements Listener {

    private final TARDIS plugin;
    private final HashMap<Byte, Integer> repeaterMap = new HashMap<Byte, Integer>();
    private final List<String> worlds;
    private final HashMap<UUID, Integer> worldMap = new HashMap<UUID, Integer>();

    public TARDISJunkControlListener(TARDIS plugin) {
        this.plugin = plugin;
        this.repeaterMap.put((byte) 3, 1);
        this.repeaterMap.put((byte) 7, 10);
        this.repeaterMap.put((byte) 11, 100);
        this.repeaterMap.put((byte) 15, 1000);
        this.worlds = this.plugin.getTardisAPI().getOverWorlds();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJunkBrakeUse(PlayerInteractEvent event) {
        if (plugin.getGeneralKeeper().isJunkTravelling()) {
            return;
        }
        Block block = event.getClickedBlock();
        if (block != null) {
            Material blockType = block.getType();
            final Location controlLoc = block.getLocation();
            String c_loc = controlLoc.toString();
            HashMap<String, Object> where = new HashMap<String, Object>();
            where.put("location", c_loc);
            if (blockType == Material.LEVER) {
                // Checks handbrake location against the database.
                where.put("type", 0);
                ResultSetControls rsh = new ResultSetControls(plugin, where, false);
                if (rsh.resultSet()) {
                    int id = rsh.getTardis_id();
                    // is it the Junk TARDIS?
                    HashMap<String, Object> wherei = new HashMap<String, Object>();
                    wherei.put("tardis_id", id);
                    ResultSetTardis rs = new ResultSetTardis(plugin, wherei, "", false);
                    if (rs.resultSet() && rs.getPreset().equals(PRESET.JUNK)) {
                        final Player player = event.getPlayer();
                        if (!player.hasPermission("tardis.junk")) {
                            TARDISMessage.send(player, "JUNK_NO_PERM");
                            return;
                        }
                        // get the destination
                        getDestination(id, player);
                        if (plugin.getGeneralKeeper().getJunkDestination() != null) {
                            // get the current location
                            Location junkloc = null;
                            Biome biome = null;
                            HashMap<String, Object> wherecl = new HashMap<String, Object>();
                            wherecl.put("tardis_id", id);
                            ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherecl);
                            if (rsc.resultSet()) {
                                junkloc = new Location(rsc.getWorld(), rsc.getX(), rsc.getY(), rsc.getZ());
                                biome = rsc.getBiome();
                            }
                            if (junkloc == null) {
                                TARDISMessage.send(player, "JUNK_NOT_FOUND");
                                return;
                            }
                            // destroy junk TARDIS
                            final TARDISMaterialisationData pdd = new TARDISMaterialisationData();
                            pdd.setPlayer(player);
                            pdd.setChameleon(false);
                            pdd.setDirection(COMPASS.SOUTH);
                            pdd.setLocation(junkloc);
                            pdd.setDematerialise(true);
                            pdd.setHide(false);
                            pdd.setOutside(false);
                            pdd.setSubmarine(rsc.isSubmarine());
                            pdd.setTardisID(id);
                            pdd.setBiome(biome);
                            plugin.getPresetDestroyer().destroyPreset(pdd);
                            // fly my pretties
                            plugin.getGeneralKeeper().setJunkTravelling(true);
                        } else {
                            TARDISMessage.send(event.getPlayer(), "JUNK_NO_DEST");
                            return;
                        }
                    }
                }
            }
            if (blockType == Material.TRIPWIRE_HOOK) {
                // 4
                where.put("type", 4);
                ResultSetControls rst = new ResultSetControls(plugin, where, false);
                if (rst.resultSet()) {
                    int id = rst.getTardis_id();
                    // is it the Junk TARDIS?
                    HashMap<String, Object> wherei = new HashMap<String, Object>();
                    wherei.put("tardis_id", id);
                    ResultSetTardis rs = new ResultSetTardis(plugin, wherei, "", false);
                    if (rs.resultSet() && rs.getPreset().equals(PRESET.JUNK)) {
                        final Player player = event.getPlayer();
                        if (!player.hasPermission("tardis.junk")) {
                            TARDISMessage.send(player, "JUNK_NO_PERM");
                            return;
                        }
                        setSignWorld(id, player.getUniqueId());
                    }
                }
            }
            if (blockType == Material.STONE_BUTTON) {
                // 1
                where.put("type", 1);
                ResultSetControls rsh = new ResultSetControls(plugin, where, false);
                if (rsh.resultSet()) {
                    int id = rsh.getTardis_id();
                    // is it the Junk TARDIS?
                    HashMap<String, Object> wherei = new HashMap<String, Object>();
                    wherei.put("tardis_id", id);
                    ResultSetTardis rs = new ResultSetTardis(plugin, wherei, "", false);
                    if (rs.resultSet() && rs.getPreset().equals(PRESET.JUNK)) {
                        final Player player = event.getPlayer();
                        if (!player.hasPermission("tardis.junk")) {
                            TARDISMessage.send(player, "JUNK_NO_PERM");
                            return;
                        }
                        setSignCoords(id, 2);
                    }
                }
            }
            if (blockType == Material.WOOD_BUTTON) {
                // 6
                where.put("type", 6);
                ResultSetControls rsh = new ResultSetControls(plugin, where, false);
                if (rsh.resultSet()) {
                    int id = rsh.getTardis_id();
                    // is it the Junk TARDIS?
                    HashMap<String, Object> wherei = new HashMap<String, Object>();
                    wherei.put("tardis_id", id);
                    ResultSetTardis rs = new ResultSetTardis(plugin, wherei, "", false);
                    if (rs.resultSet() && rs.getPreset().equals(PRESET.JUNK)) {
                        final Player player = event.getPlayer();
                        if (!player.hasPermission("tardis.junk")) {
                            TARDISMessage.send(player, "JUNK_NO_PERM");
                            return;
                        }
                        setSignCoords(id, 3);
                    }
                }
            }
        }
    }

    private void getDestination(int id, Player p) {
        // get the destination sign
        Sign s = getDestinationSign(id);
        if (s != null) {
            String line1 = s.getLine(1);
            String line2 = s.getLine(2);
            String line3 = s.getLine(3);
            if (line1.isEmpty() || line2.isEmpty() || line3.isEmpty()) {
                TARDISMessage.send(p, "JUNK_LINES");
                return;
            }
            World w = plugin.getServer().getWorld(line1);
            int x = TARDISNumberParsers.parseInt(line2);
            int z = TARDISNumberParsers.parseInt(line3);
            // load the chunk
            Chunk chunk = w.getChunkAt(x, z);
            while (!chunk.isLoaded()) {
                w.loadChunk(chunk);
            }
            int y = w.getHighestBlockYAt(x, z);
            Location d = new Location(w, x, y, z);
            // TODO check destination
            if (plugin.getPluginRespect().getRespect(d, new Parameters(p, FLAG.getNoMessageFlags()))) {
                while (!chunk.isLoaded()) {
                    chunk.load();
                }
                d.setY(getActualHighestY(d));
                plugin.getGeneralKeeper().setJunkDestination(d);
            }
        }
    }

    private void setSignWorld(int id, UUID uuid) {
        Sign s = getDestinationSign(id);
        if (s != null) {
            int pos = 0;
            if (worldMap.containsKey(uuid)) {
                int v = worldMap.get(uuid);
                pos = (v < worlds.size() - 1) ? v + 1 : 0;
                worldMap.put(uuid, pos);
            } else {
                worldMap.put(uuid, pos);
            }
            s.setLine(1, worlds.get(pos));
            s.update();
        }
    }

    private void setSignCoords(int id, int line) {
        // get the destination sign
        Sign s = getDestinationSign(id);
        // get repeater data
        Block r = getControlBlock(id, 2);
        // get comparator data
        Block c = getControlBlock(id, 3);
        if (s != null && r != null && c != null) {
            String txt = s.getLine(line);
            if (txt.isEmpty()) {
                txt = "0";
            }
            int multiplier = repeaterMap.get(r.getData());
            int positiveNegative = (c.getData() == 3) ? 1 : -1;
            // get current coord
            int current = TARDISNumberParsers.parseInt(txt);
            // increment / decrement sign coord value
            int amount = current + (multiplier * positiveNegative);
            s.setLine(line, "" + amount);
            s.update();
        }
    }

    private Sign getDestinationSign(int id) {
        Sign sign = null;
        Block b = getControlBlock(id, 9);
        if (b != null && b.getType().equals(Material.WALL_SIGN)) {
            sign = (Sign) b.getState();
        }
        return sign;
    }

    private Block getControlBlock(int id, int type) {
        Block b = null;
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("tardis_id", id);
        where.put("type", type);
        ResultSetControls rs = new ResultSetControls(plugin, where, false);
        if (rs.resultSet()) {
            Location l = plugin.getLocationUtils().getLocationFromBukkitString(rs.getLocation());
            b = l.getBlock();
        }
        return b;
    }

    private int getActualHighestY(Location l) {
        int startx = l.getBlockX() - 3, resetx = startx, starty = l.getBlockY(), startz = l.getBlockZ() - 2, resetz = startz, level, row, col;
        for (level = 0; level < 5; level++) {
            boolean found = false;
            for (row = 0; row < 6; row++) {
                for (col = 0; col < 6; col++) {
                    Material mat = l.getWorld().getBlockAt(startx, starty, startz).getType();
                    if (!TARDISConstants.GOOD_MATERIALS.contains(mat)) {
                        found = true;
                    }
                    startx += 1;
                }
                startx = resetx;
                startz += 1;
            }
            if (!found) {
                return starty;
            }
            startz = resetz;
            starty += 1;
        }
        return starty;
    }
}
