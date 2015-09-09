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
import me.eccentric_nz.TARDIS.builders.TARDISMaterialisationData;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.SCHEMATIC;
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
        final TARDISMaterialisationData pbd = new TARDISMaterialisationData();
        pbd.setChameleon(false);
        pbd.setDirection(COMPASS.SOUTH);
        pbd.setLocation(l);
        pbd.setMalfunction(false);
        pbd.setOutside(true);
        pbd.setPlayer(p);
        pbd.setRebuild(false);
        pbd.setSubmarine(false);
        pbd.setTardisID(lastInsertId);
        pbd.setBiome(l.getBlock().getBiome());
        // build the TARDIS in the Vortex
        plugin.getInteriorBuilder().buildInner(junk, chunkworld, lastInsertId, p, Material.WOOL, (byte) 1, Material.WOOL, (byte) 7, true);
        // build the TARDIS in the world
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                plugin.getPresetBuilder().buildPreset(pbd);
            }
        }, 5L);
        return true;
    }
}
