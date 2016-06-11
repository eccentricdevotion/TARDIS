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
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.builders.BuildData;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.SCHEMATIC;
import me.eccentric_nz.TARDIS.rooms.TARDISWalls;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

/**
 *
 * @author eccentric_nz
 */
public class TARDISJunkCreator {

    private final TARDIS plugin;
    private final Player p;

    public TARDISJunkCreator(TARDIS plugin, Player p) {
        this.plugin = plugin;
        this.p = p;
    }

    public boolean createJunkTARDIS() {
        if (!p.hasPermission("tardis.admin")) {
            TARDISMessage.send(p, "CMD_ADMIN");
            return true;
        }
        if (!plugin.getConfig().getBoolean("junk.enabled") || !plugin.getConfig().getBoolean("creation.default_world")) {
            TARDISMessage.send(p, "JUNK_DISABLED");
            return true;
        }
        // check if there is a junk TARDIS already
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("uuid", "00000000-aaaa-bbbb-cccc-000000000000");
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
        if (rs.resultSet()) {
            TARDISMessage.send(p, "JUNK_EXISTS");
            return true;
        }
        // get player's target block
        Location l = p.getTargetBlock(plugin.getGeneralKeeper().getTransparent(), 16).getLocation().add(0.0d, 1.0d, 0.0d);
        // get the schematic
        SCHEMATIC junk = new SCHEMATIC("AIR", "junk", "Junk TARDIS", true, false, false, false, false);
        // save a tardis record
        String cw = plugin.getConfig().getString("creation.default_world_name");
        final QueryFactory qf = new QueryFactory(plugin);
        HashMap<String, Object> set = new HashMap<String, Object>();
        set.put("uuid", "00000000-aaaa-bbbb-cccc-000000000000");
        set.put("owner", "junk");
        set.put("size", "JUNK");
        set.put("artron_level", Integer.MAX_VALUE);
        set.put("tardis_init", 1);
        set.put("powered_on", 1);
        set.put("chameleon_preset", "JUNK");
        set.put("chameleon_demat", "JUNK");
        set.put("chameleon_id", 35);
        set.put("chameleon_data", 11);
        set.put("lastuse", System.currentTimeMillis());
        final int lastInsertId = qf.doSyncInsert("tardis", set);
        // get wall floor prefs
        Material wall_type = Material.WOOL;
        byte wall_data = 1;
        Material floor_type = Material.WOOL;
        byte floor_data = 7;
        // check if player_prefs record
        HashMap<String, Object> wherepp = new HashMap<String, Object>();
        wherepp.put("uuid", "00000000-aaaa-bbbb-cccc-000000000000");
        ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, wherepp);
        if (rsp.resultSet()) {
            TARDISWalls.Pair fid_data = plugin.getTardisWalls().blocks.get(rsp.getFloor());
            floor_type = fid_data.getType();
            floor_data = fid_data.getData();
            TARDISWalls.Pair wid_data = plugin.getTardisWalls().blocks.get(rsp.getWall());
            wall_type = wid_data.getType();
            wall_data = wid_data.getData();
        } else {
            // create a player_prefs record
            HashMap<String, Object> setpp = new HashMap<String, Object>();
            setpp.put("uuid", "00000000-aaaa-bbbb-cccc-000000000000");
            setpp.put("player", "junk");
            setpp.put("wall", "ORANGE_WOOL");
            setpp.put("floor", "GREY_WOOL");
            setpp.put("lamp", "REDSTONE_LAMP_OFF");
            qf.doInsert("player_prefs", setpp);
        }
        World chunkworld = plugin.getServer().getWorld(cw);
        // populate home, current, next and back tables
        HashMap<String, Object> setlocs = new HashMap<String, Object>();
        setlocs.put("tardis_id", lastInsertId);
        setlocs.put("world", l.getWorld().getName());
        setlocs.put("x", l.getBlockX());
        setlocs.put("y", l.getBlockY());
        setlocs.put("z", l.getBlockZ());
        setlocs.put("direction", "SOUTH");
        qf.insertLocations(setlocs, l.getBlock().getBiome().toString(), lastInsertId);
        // build the TARDIS at the location
        final BuildData bd = new BuildData(plugin, "00000000-aaaa-bbbb-cccc-000000000000");
        bd.setChameleon(false);
        bd.setDirection(COMPASS.SOUTH);
        bd.setLocation(l);
        bd.setMalfunction(false);
        bd.setOutside(true);
        bd.setPlayer(p);
        bd.setRebuild(false);
        bd.setSubmarine(false);
        bd.setTardisID(lastInsertId);
        bd.setBiome(l.getBlock().getBiome());
        // build the TARDIS in the Vortex
        plugin.getInteriorBuilder().buildInner(junk, chunkworld, lastInsertId, p, wall_type, wall_data, floor_type, floor_data, true);
        // build the TARDIS in the world
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                plugin.getPresetBuilder().buildPreset(bd);
            }
        }, 5L);
        return true;
    }
}
