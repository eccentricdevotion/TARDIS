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
package me.eccentric_nz.TARDIS.junk;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.api.Parameters;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetControls;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisPreset;
import me.eccentric_nz.TARDIS.destroyers.DestroyData;
import me.eccentric_nz.TARDIS.enumeration.*;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import me.eccentric_nz.TARDIS.planets.TARDISAliasResolver;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.data.type.Comparator;
import org.bukkit.block.data.type.Repeater;
import org.bukkit.block.data.type.Switch;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class TARDISJunkControlListener implements Listener {

    private final TARDIS plugin;
    private final HashMap<Integer, Integer> repeaterMap = new HashMap<>();
    private final List<String> worlds;
    private final HashMap<UUID, Integer> worldMap = new HashMap<>();

    public TARDISJunkControlListener(TARDIS plugin) {
        this.plugin = plugin;
        repeaterMap.put(1, 1);
        repeaterMap.put(2, 10);
        repeaterMap.put(3, 100);
        repeaterMap.put(4, 1000);
        worlds = this.plugin.getTardisAPI().getOverWorlds();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJunkBrakeUse(PlayerInteractEvent event) {
        if (event.getHand() == null || event.getHand().equals(EquipmentSlot.OFF_HAND)) {
            return;
        }
        if (plugin.getGeneralKeeper().isJunkTravelling()) {
            return;
        }
        Block block = event.getClickedBlock();
        if (block != null) {
            Material blockType = block.getType();
            Location controlLoc = block.getLocation();
            String c_loc = controlLoc.toString();
            HashMap<String, Object> where = new HashMap<>();
            where.put("location", c_loc);
            if (blockType == Material.LEVER) {
                // Checks handbrake location against the database.
                where.put("type", 0);
                ResultSetControls rsh = new ResultSetControls(plugin, where, false);
                if (rsh.resultSet()) {
                    int id = rsh.getTardis_id();
                    // is it the Junk TARDIS?
                    ResultSetTardisPreset rs = new ResultSetTardisPreset(plugin);
                    if (rs.fromID(id) && rs.getPreset().equals(PRESET.JUNK)) {
                        Player player = event.getPlayer();
                        if (!TARDISPermission.hasPermission(player, "tardis.junk")) {
                            TARDISMessage.send(player, "JUNK_NO_PERM");
                            return;
                        }
                        // get the destination
                        getDestination(id, player);
                        if (plugin.getGeneralKeeper().getJunkDestination() != null) {
                            // get the current location
                            Location junkloc = null;
                            HashMap<String, Object> wherecl = new HashMap<>();
                            wherecl.put("tardis_id", id);
                            ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherecl);
                            if (rsc.resultSet()) {
                                junkloc = new Location(rsc.getWorld(), rsc.getX(), rsc.getY(), rsc.getZ());
                            }
                            if (junkloc == null) {
                                TARDISMessage.send(player, "JUNK_NOT_FOUND");
                                return;
                            }
                            Switch lever = (Switch) block.getBlockData();
                            lever.setPowered(!lever.isPowered());
                            block.setBlockData(lever);
                            // destroy junk TARDIS
                            DestroyData dd = new DestroyData();
                            dd.setPlayer(player);
                            dd.setDirection(COMPASS.SOUTH);
                            dd.setLocation(junkloc);
                            dd.setHide(false);
                            dd.setOutside(false);
                            dd.setSubmarine(rsc.isSubmarine());
                            dd.setTardisID(id);
                            dd.setThrottle(SpaceTimeThrottle.JUNK);
                            plugin.getPresetDestroyer().destroyPreset(dd);
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
                    ResultSetTardisPreset rs = new ResultSetTardisPreset(plugin);
                    if (rs.fromID(id) && rs.getPreset().equals(PRESET.JUNK)) {
                        Player player = event.getPlayer();
                        if (!TARDISPermission.hasPermission(player, "tardis.junk")) {
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
                    ResultSetTardisPreset rs = new ResultSetTardisPreset(plugin);
                    if (rs.fromID(id) && rs.getPreset().equals(PRESET.JUNK)) {
                        Player player = event.getPlayer();
                        if (!TARDISPermission.hasPermission(player, "tardis.junk")) {
                            TARDISMessage.send(player, "JUNK_NO_PERM");
                            return;
                        }
                        setSignCoords(id, 2);
                    }
                }
            }
            if (blockType == Material.OAK_BUTTON) {
                // 6
                where.put("type", 6);
                ResultSetControls rsh = new ResultSetControls(plugin, where, false);
                if (rsh.resultSet()) {
                    int id = rsh.getTardis_id();
                    // is it the Junk TARDIS?
                    ResultSetTardisPreset rs = new ResultSetTardisPreset(plugin);
                    if (rs.fromID(id) && rs.getPreset().equals(PRESET.JUNK)) {
                        Player player = event.getPlayer();
                        if (!TARDISPermission.hasPermission(player, "tardis.junk")) {
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
                if (line1.isEmpty() && line2.isEmpty() && line3.isEmpty()) {
                    // check location
                    TARDISJunkLocation tjl = new TARDISJunkLocation(plugin);
                    if (tjl.isNotHome()) {
                        plugin.getGeneralKeeper().setJunkDestination(tjl.getHome());
                        TARDISMessage.send(p, "JUNK_RETURN");
                        return;
                    }
                }
                TARDISMessage.send(p, "JUNK_LINES");
                return;
            }
            World w;
            if (plugin.getWorldManager().equals(WorldManager.MULTIVERSE)) {
                w = plugin.getMVHelper().getWorld(line1);
            } else {
                w = TARDISAliasResolver.getWorldFromAlias(line1);
            }
            int x = TARDISNumberParsers.parseInt(line2);
            int z = TARDISNumberParsers.parseInt(line3);
            // load the chunk
            Chunk chunk = w.getChunkAt(x, z);
            while (!chunk.isLoaded()) {
                w.loadChunk(chunk);
            }
            int y = TARDISStaticLocationGetters.getHighestYin3x3(w, x, z);
            Location d = new Location(w, x, y, z);
            // check destination
            if (plugin.getPluginRespect().getRespect(d, new Parameters(p, Flag.getNoMessageFlags()))) {
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
            }
            worldMap.put(uuid, pos);
            s.setLine(1, worlds.get(pos));
            s.update();
        }
    }

    private void setSignCoords(int id, int line) {
        // get the destination sign
        Sign s = getDestinationSign(id);
        // get repeater data
        Repeater r = (Repeater) getControlBlock(id, 2).getBlockData();
        // get comparator data
        Comparator c = (Comparator) getControlBlock(id, 3).getBlockData();
        if (s != null && r != null && c != null) {
            String txt = s.getLine(line);
            if (txt.isEmpty()) {
                txt = "0";
            }
            int multiplier = repeaterMap.get(r.getDelay());
            int positiveNegative = (c.getMode().equals(Comparator.Mode.COMPARE)) ? 1 : -1;
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
        if (b != null && Tag.WALL_SIGNS.isTagged(b.getType())) {
            sign = (Sign) b.getState();
        }
        return sign;
    }

    private Block getControlBlock(int id, int type) {
        Block b = null;
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        where.put("type", type);
        ResultSetControls rs = new ResultSetControls(plugin, where, false);
        if (rs.resultSet()) {
            Location l = TARDISStaticLocationGetters.getLocationFromBukkitString(rs.getLocation());
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
