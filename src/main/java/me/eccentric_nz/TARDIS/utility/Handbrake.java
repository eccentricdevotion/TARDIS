package me.eccentric_nz.TARDIS.utility;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetControls;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetDoors;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Comparator;

import java.util.HashMap;

public class Handbrake {

    private final TARDIS plugin;

    public Handbrake(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void toggleBeacon(String str, boolean on) {
        Block b = TARDISStaticLocationGetters.getLocationFromDB(str).getBlock();
        b.setBlockData((on) ? TARDISConstants.GLASS : TARDISConstants.POWER);
    }

    public boolean isRelativityDifferentiated(int id) {
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        where.put("type", 47);
        ResultSetControls rsc = new ResultSetControls(plugin, where, false);
        if (rsc.resultSet()) {
            Block rd = TARDISStaticLocationGetters.getLocationFromBukkitString(rsc.getLocation()).getBlock();
            Comparator comparator = (Comparator) rd.getBlockData();
            return comparator.getMode().equals(Comparator.Mode.SUBTRACT);
        }
        return false;
    }

    public boolean isDoorOpen(int id) {
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        where.put("door_type", 1);
        ResultSetDoors rs = new ResultSetDoors(plugin, where, false);
        if (rs.resultSet()) {
            Block door = TARDISStaticLocationGetters.getLocationFromDB(rs.getDoor_location()).getBlock();
            return TARDISStaticUtils.isDoorOpen(door);
        }
        return false;
    }
}
