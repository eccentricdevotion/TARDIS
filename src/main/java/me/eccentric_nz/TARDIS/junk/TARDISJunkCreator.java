/*
 * Copyright (C) 2021 eccentric_nz
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
package me.eccentric_nz.tardis.junk;

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.blueprints.TardisPermission;
import me.eccentric_nz.tardis.builders.BuildData;
import me.eccentric_nz.tardis.builders.TardisBuilderInner;
import me.eccentric_nz.tardis.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.tardis.database.resultset.ResultSetTardis;
import me.eccentric_nz.tardis.enumeration.CardinalDirection;
import me.eccentric_nz.tardis.enumeration.Consoles;
import me.eccentric_nz.tardis.enumeration.SpaceTimeThrottle;
import me.eccentric_nz.tardis.messaging.TardisMessage;
import me.eccentric_nz.tardis.planets.TardisAliasResolver;
import me.eccentric_nz.tardis.utility.TardisStaticUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Objects;

/**
 * @author eccentric_nz
 */
class TardisJunkCreator {

    private final TardisPlugin plugin;
    private final Player p;

    TardisJunkCreator(TardisPlugin plugin, Player p) {
        this.plugin = plugin;
        this.p = p;
    }

    boolean createJunkTARDIS() {
        if (!TardisPermission.hasPermission(p, "tardis.admin")) {
            TardisMessage.send(p, "CMD_ADMIN");
            return true;
        }
        if (!plugin.getConfig().getBoolean("junk.enabled") || !plugin.getConfig().getBoolean("creation.default_world")) {
            TardisMessage.send(p, "JUNK_DISABLED");
            return true;
        }
        // check if there is a junk tardis already
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", "00000000-aaaa-bbbb-cccc-000000000000");
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 2);
        if (rs.resultSet()) {
            TardisMessage.send(p, "JUNK_EXISTS");
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
        set.put("last_use", System.currentTimeMillis());
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
            plugin.getQueryFactory().doInsert("player_prefs", setpp);
        }
        World chunkworld = TardisAliasResolver.getWorldFromAlias(cw);
        // populate home, current, next and back tables
        HashMap<String, Object> setlocs = new HashMap<>();
        setlocs.put("tardis_id", lastInsertId);
        setlocs.put("world", Objects.requireNonNull(l.getWorld()).getName());
        setlocs.put("x", l.getBlockX());
        setlocs.put("y", l.getBlockY());
        setlocs.put("z", l.getBlockZ());
        setlocs.put("direction", "SOUTH");
        plugin.getQueryFactory().insertLocations(setlocs, TardisStaticUtils.getBiomeAt(l).getKey().toString(), lastInsertId);
        // build the tardis at the location
        BuildData bd = new BuildData(null);
        bd.setDirection(CardinalDirection.SOUTH);
        bd.setLocation(l);
        bd.setMalfunction(false);
        bd.setOutside(true);
        bd.setPlayer(p);
        bd.setRebuild(false);
        bd.setSubmarine(false);
        bd.setTardisId(lastInsertId);
        bd.setThrottle(SpaceTimeThrottle.JUNK);
        // build the tardis in the Vortex
        TardisBuilderInner builder = new TardisBuilderInner(plugin, Consoles.schematicFor("junk"), chunkworld, lastInsertId, p, wall_type, floor_type, true);
        int task = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, builder, 1L, 3L);
        builder.setTask(task);
        // build the tardis in the world
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> plugin.getPresetBuilder().buildPreset(bd), 5L);
        return true;
    }
}
