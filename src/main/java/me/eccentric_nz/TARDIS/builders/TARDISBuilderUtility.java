/*
 * Copyright (C) 2023 eccentric_nz
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
package me.eccentric_nz.TARDIS.builders;

import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetDoors;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisModel;
import me.eccentric_nz.TARDIS.enumeration.PRESET;
import org.bukkit.Material;
import org.bukkit.World;

public class TARDISBuilderUtility {

    static void saveDoorLocation(BuildData bd) {
        World world = bd.getLocation().getWorld();
        int x = bd.getLocation().getBlockX();
        int y = bd.getLocation().getBlockY();
        int z = bd.getLocation().getBlockZ();
        // remember the door location
        String doorloc = world.getName() + ":" + x + ":" + y + ":" + z;
        String doorStr = world.getBlockAt(x, y, z).getLocation().toString();
        TARDIS.plugin.getGeneralKeeper().getProtectBlockMap().put(doorStr, bd.getTardisID());
        // should insert the door when tardis is first made, and then update location there after!
        HashMap<String, Object> whered = new HashMap<>();
        whered.put("door_type", 0);
        whered.put("tardis_id", bd.getTardisID());
        ResultSetDoors rsd = new ResultSetDoors(TARDIS.plugin, whered, false);
        HashMap<String, Object> setd = new HashMap<>();
        setd.put("door_location", doorloc);
        setd.put("door_direction", bd.getDirection().toString());
        if (rsd.resultSet()) {
            HashMap<String, Object> whereid = new HashMap<>();
            whereid.put("door_id", rsd.getDoor_id());
            TARDIS.plugin.getQueryFactory().doUpdate("doors", setd, whereid);
        } else {
            setd.put("tardis_id", bd.getTardisID());
            setd.put("door_type", 0);
            TARDIS.plugin.getQueryFactory().doInsert("doors", setd);
        }
    }

    public static Material getMaterialForItemFrame(PRESET preset, int id, boolean isMaterialisation) {
        if (preset.equals(PRESET.ITEM)) {
            ResultSetTardisModel rstm = new ResultSetTardisModel(TARDIS.plugin);
            if (rstm.fromID(id)) {
                String item = (isMaterialisation) ? rstm.getItemPreset() : rstm.getItemDemat();
                return Material.valueOf(TARDIS.plugin.getCustomModelConfig().getString("models." + item + ".item"));
            } else {
                return Material.BLUE_DYE;
            }
        } else if (preset.equals(PRESET.WEEPING_ANGEL)) {
            return Material.GRAY_STAINED_GLASS_PANE;
        } else if (preset.equals(PRESET.POLICE_BOX_TENNANT)) {
            return Material.CYAN_STAINED_GLASS_PANE;
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
        TARDIS.plugin.getQueryFactory().doUpdate("tardis", set, where);
    }
}
