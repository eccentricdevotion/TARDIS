/*
 * Copyright (C) 2020 eccentric_nz
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
import me.eccentric_nz.TARDIS.builders.BuildData;
import me.eccentric_nz.TARDIS.database.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.CONSOLES;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 * @author eccentric_nz
 */
class TARDISJunkCreator {

    private final TARDIS plugin;
    private final Player p;

    TARDISJunkCreator(TARDIS plugin, Player p) {
        this.plugin = plugin;
        this.p = p;
    }

    boolean createJunkTARDIS() {
        if (!p.hasPermission("tardis.admin")) {
            TARDISMessage.send(p, "CMD_ADMIN");
            return true;
        }
        if (!plugin.getConfig().getBoolean("junk.enabled") || !plugin.getConfig().getBoolean("creation.default_world")) {
            TARDISMessage.send(p, "JUNK_DISABLED");
            return true;
        }
        // check if there is a junk TARDIS already
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", "00000000-aaaa-bbbb-cccc-000000000000");
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 2);
        if (rs.resultSet()) {
            TARDISMessage.send(p, "JUNK_EXISTS");
            return true;
        }
        // get player's target block
        Location l = p.getTargetBlock(plugin.getGeneralKeeper().getTransparent(), 16).getLocation().add(0.0d, 1.0d, 0.0d);
        // save a tardis record
        String cw = plugin.getConfig().getString("creation.default_world_name");
        HashMap<String, Object> set = new HashMap<>();
        set.put("uuid", "00000000-aaaa-bbbb-cccc-000000000000");
        set.put("owner", "junk");
        set.put("size", "JUNK");
        set.put("artron_level", Integer.MAX_VALUE);
        set.put("tardis_init", 1);
        set.put("powered_on", 1);
        set.put("chameleon_preset", "JUNK");
        set.put("chameleon_demat", "JUNK");
        set.put("lastuse", System.currentTimeMillis());
        int lastInsertId = plugin.getQueryFactory().doSyncInsert("tardis", set);
        // get wall floor prefs
        Material wall_type = Material.ORANGE_WOOL;
        Material floor_type = Material.GRAY_WOOL;
        // check if player_prefs record
        ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, "00000000-aaaa-bbbb-cccc-000000000000");
        if (rsp.resultSet()) {
            floor_type = Material.valueOf(rsp.getFloor());
            wall_type = Material.valueOf(rsp.getWall());
        } else {
            // create a player_prefs record
            HashMap<String, Object> setpp = new HashMap<>();
            setpp.put("uuid", "00000000-aaaa-bbbb-cccc-000000000000");
            setpp.put("player", "junk");
            setpp.put("wall", "ORANGE_WOOL");
            setpp.put("floor", "LIGHT_GRAY_WOOL");
            setpp.put("lamp", "REDSTONE_LAMP");
            plugin.getQueryFactory().doInsert("player_prefs", setpp);
        }
        World chunkworld = plugin.getServer().getWorld(cw);
        // populate home, current, next and back tables
        HashMap<String, Object> setlocs = new HashMap<>();
        setlocs.put("tardis_id", lastInsertId);
        setlocs.put("world", l.getWorld().getName());
        setlocs.put("x", l.getBlockX());
        setlocs.put("y", l.getBlockY());
        setlocs.put("z", l.getBlockZ());
        setlocs.put("direction", "SOUTH");
        plugin.getQueryFactory().insertLocations(setlocs, l.getBlock().getBiome().toString(), lastInsertId);
        // build the TARDIS at the location
        BuildData bd = new BuildData(plugin, "00000000-aaaa-bbbb-cccc-000000000000");
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
        plugin.getInteriorBuilder().buildInner(CONSOLES.SCHEMATICFor("junk"), chunkworld, lastInsertId, p, wall_type, floor_type, true);
        // build the TARDIS in the world
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> plugin.getPresetBuilder().buildPreset(bd), 5L);
        return true;
    }
}
