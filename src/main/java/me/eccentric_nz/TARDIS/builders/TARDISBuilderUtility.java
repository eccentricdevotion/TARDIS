package me.eccentric_nz.TARDIS.builders;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetDoors;
import me.eccentric_nz.TARDIS.enumeration.PRESET;
import org.bukkit.Material;
import org.bukkit.World;

import java.util.HashMap;

public class TARDISBuilderUtility {

    public static void saveDoorLocation(BuildData bd) {
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

    public static Material getDyeMaterial(PRESET preset) {
        String split = preset.toString().replace("POLICE_BOX_", "");
        String dye = split + "_DYE";
        return Material.valueOf(dye);
    }
}
