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
package me.eccentric_nz.TARDIS.junk;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.api.Parameters;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.control.actions.FindWithJunkAction;
import me.eccentric_nz.TARDIS.database.data.Current;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetControls;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentFromId;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisPreset;
import me.eccentric_nz.TARDIS.destroyers.DestroyData;
import me.eccentric_nz.TARDIS.enumeration.*;
import me.eccentric_nz.TARDIS.planets.TARDISAliasResolver;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.data.type.Comparator;
import org.bukkit.block.data.type.Repeater;
import org.bukkit.block.data.type.Switch;
import org.bukkit.block.sign.Side;
import org.bukkit.block.sign.SignSide;
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

    public static Sign getDestinationSign(int id) {
        Sign sign = null;
        Block b = getControlBlock(id, 9);
        if (b != null && Tag.WALL_SIGNS.isTagged(b.getType())) {
            sign = (Sign) b.getState();
        }
        return sign;
    }

    private static Block getControlBlock(int id, int type) {
        Block b = null;
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        where.put("type", type);
        ResultSetControls rs = new ResultSetControls(TARDIS.plugin, where, false);
        if (rs.resultSet()) {
            Location l = TARDISStaticLocationGetters.getLocationFromBukkitString(rs.getLocation());
            b = l.getBlock();
        }
        return b;
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
                    if (rs.fromID(id) && rs.getPreset().equals(ChameleonPreset.JUNK)) {
                        Player player = event.getPlayer();
                        if (!TARDISPermission.hasPermission(player, "tardis.junk")) {
                            plugin.getMessenger().send(player, TardisModule.TARDIS, "JUNK_NO_PERM");
                            return;
                        }
                        // get the destination
                        getDestination(id, player);
                        if (plugin.getGeneralKeeper().getJunkDestination() != null) {
                            // get the current location
                            ResultSetCurrentFromId rsc = new ResultSetCurrentFromId(plugin, id);
                            if (!rsc.resultSet()) {
                                plugin.getMessenger().send(player, TardisModule.TARDIS, "JUNK_NOT_FOUND");
                                return;
                            }
                            Current current = rsc.getCurrent();
                            Switch lever = (Switch) block.getBlockData();
                            lever.setPowered(!lever.isPowered());
                            block.setBlockData(lever);
                            // destroy junk TARDIS
                            DestroyData dd = new DestroyData();
                            dd.setPlayer(player);
                            dd.setDirection(COMPASS.SOUTH);
                            dd.setLocation(current.location());
                            dd.setHide(false);
                            dd.setOutside(false);
                            dd.setSubmarine(current.submarine());
                            dd.setTardisID(id);
                            dd.setThrottle(SpaceTimeThrottle.JUNK);
                            plugin.getPresetDestroyer().destroyPreset(dd);
                            // fly my pretties
                            plugin.getGeneralKeeper().setJunkTravelling(true);
                        } else {
                            plugin.getMessenger().send(event.getPlayer(), TardisModule.TARDIS, "JUNK_NO_DEST");
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
                    if (rs.fromID(id) && rs.getPreset().equals(ChameleonPreset.JUNK)) {
                        Player player = event.getPlayer();
                        if (!TARDISPermission.hasPermission(player, "tardis.junk")) {
                            plugin.getMessenger().send(player, TardisModule.TARDIS, "JUNK_NO_PERM");
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
                    if (rs.fromID(id) && rs.getPreset().equals(ChameleonPreset.JUNK)) {
                        Player player = event.getPlayer();
                        if (!TARDISPermission.hasPermission(player, "tardis.junk")) {
                            plugin.getMessenger().send(player, TardisModule.TARDIS, "JUNK_NO_PERM");
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
                    if (rs.fromID(id) && rs.getPreset().equals(ChameleonPreset.JUNK)) {
                        Player player = event.getPlayer();
                        if (!TARDISPermission.hasPermission(player, "tardis.junk")) {
                            plugin.getMessenger().send(player, TardisModule.TARDIS, "JUNK_NO_PERM");
                            return;
                        }
                        setSignCoords(id, 3);
                    }
                }
            }
            if (blockType == Material.CRIMSON_BUTTON) {
                // 52
                where.put("type", 52);
                ResultSetControls rsh = new ResultSetControls(plugin, where, false);
                if (rsh.resultSet()) {
                    int id = rsh.getTardis_id();
                    // is it the Junk TARDIS?
                    ResultSetTardisPreset rs = new ResultSetTardisPreset(plugin);
                    if (rs.fromID(id) && rs.getPreset().equals(ChameleonPreset.JUNK)) {
                        new FindWithJunkAction(plugin).getNearbyChunkLocation(id, event.getPlayer());
                    }
                }
            }
        }
    }

    private void getDestination(int id, Player p) {
        // get the destination sign
        Sign s = getDestinationSign(id);
        if (s != null) {
            SignSide front = s.getSide(Side.FRONT);
            String line1 = ComponentUtils.stripColour(front.line(1));
            String line2 = ComponentUtils.stripColour(front.line(2));
            String line3 = ComponentUtils.stripColour(front.line(3));
            if (line1.isEmpty() || line2.isEmpty() || line3.isEmpty()) {
                if (line1.isEmpty() && line2.isEmpty() && line3.isEmpty()) {
                    // check location
                    TARDISJunkLocation tjl = new TARDISJunkLocation(plugin);
                    if (tjl.isNotHome()) {
                        plugin.getGeneralKeeper().setJunkDestination(tjl.getHome());
                        plugin.getMessenger().send(p, TardisModule.TARDIS, "JUNK_RETURN");
                        return;
                    }
                }
                plugin.getMessenger().send(p, TardisModule.TARDIS, "JUNK_LINES");
                return;
            }
            World w;
            if (!plugin.getPlanetsConfig().getBoolean("planets." + line1 + ".enabled") && plugin.getWorldManager().equals(WorldManager.MULTIVERSE)) {
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
            s.getSide(Side.FRONT).line(1, Component.text(worlds.get(pos)));
            s.update();
        }
    }

    private void setSignCoords(int id, int line) {
        // get the destination sign
        Sign s = getDestinationSign(id);
        Block rb = getControlBlock(id, 2);
        // get repeater data
        Repeater r = (rb != null) ? (Repeater) rb.getBlockData() : null;
        // get comparator data
        Block cb = getControlBlock(id, 3);
        Comparator c = (cb != null) ? (Comparator) cb.getBlockData() : null;
        if (s != null && r != null && c != null) {
            String txt = ComponentUtils.stripColour(s.getSide(Side.FRONT).line(line));
            if (txt.isEmpty()) {
                txt = "0";
            }
            int multiplier = repeaterMap.get(r.getDelay());
            int positiveNegative = (c.getMode().equals(Comparator.Mode.COMPARE)) ? 1 : -1;
            // get current coord
            int current = TARDISNumberParsers.parseInt(txt);
            // increment / decrement sign coord value
            int amount = current + (multiplier * positiveNegative);
            s.getSide(Side.FRONT).line(line, Component.text(amount));
            s.update();
        }
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
