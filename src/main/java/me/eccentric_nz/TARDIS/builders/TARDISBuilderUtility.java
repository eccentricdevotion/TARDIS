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
 * along with plugin program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.tardis.builders;

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.database.resultset.ResultSetDoors;
import me.eccentric_nz.tardis.enumeration.Preset;
import org.bukkit.Material;
import org.bukkit.World;

import java.util.HashMap;

public class TardisBuilderUtility {

    static void saveDoorLocation(BuildData bd) {
        World world = bd.getLocation().getWorld();
        int x = bd.getLocation().getBlockX();
        int y = bd.getLocation().getBlockY();
        int z = bd.getLocation().getBlockZ();
        // remember the door location
        assert world != null;
        String doorLoc = world.getName() + ":" + x + ":" + y + ":" + z;
        String doorStr = world.getBlockAt(x, y, z).getLocation().toString();
        TardisPlugin.plugin.getGeneralKeeper().getProtectBlockMap().put(doorStr, bd.getTardisId());
        // should insert the door when tardis is first made, and then update location there after!
        HashMap<String, Object> whered = new HashMap<>();
        whered.put("door_type", 0);
        whered.put("tardis_id", bd.getTardisId());
        ResultSetDoors rsd = new ResultSetDoors(TardisPlugin.plugin, whered, false);
        HashMap<String, Object> setd = new HashMap<>();
        setd.put("door_location", doorLoc);
        setd.put("door_direction", bd.getDirection().toString());
        if (rsd.resultSet()) {
            HashMap<String, Object> whereid = new HashMap<>();
            whereid.put("door_id", rsd.getDoorId());
            TardisPlugin.plugin.getQueryFactory().doUpdate("doors", setd, whereid);
        } else {
            setd.put("tardis_id", bd.getTardisId());
            setd.put("door_type", 0);
            TardisPlugin.plugin.getQueryFactory().doInsert("doors", setd);
        }
    }

    public static Material getMaterialForItemFrame(Preset preset) {
        if (preset.equals(Preset.WEEPING_ANGEL)) {
            return Material.GRAY_STAINED_GLASS_PANE;
        } else {
            String split = preset.toString().replace("POLICE_BOX_", "");
            String dye = split + "_DYE";
            return Material.valueOf(dye);
        }
    }

    static void updateChameleonDemat(String preset, int id) {
        // update demat field in database
        HashMap<String, Object> set = new HashMap<>();
        set.put("chameleon_demat", preset);
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        TardisPlugin.plugin.getQueryFactory().doUpdate("tardis", set, where);
    }
}
