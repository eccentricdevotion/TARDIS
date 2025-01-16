package me.eccentric_nz.TARDIS.utility;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.sensor.SensorToggle;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetControls;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetDoors;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetSensors;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Comparator;

import java.util.HashMap;

public class Handbrake {

    private final TARDIS plugin;

    public Handbrake(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean isRelativityDifferentiated(int id) {
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        where.put("type", 47);
        ResultSetControls rsc = new ResultSetControls(plugin, where, false);
        if (rsc.resultSet()) {
            Block rd = TARDISStaticLocationGetters.getLocationFromBukkitString(rsc.getLocation()).getBlock();
            if (rd.getType() == Material.COMPARATOR) {
                Comparator comparator = (Comparator) rd.getBlockData();
                return comparator.getMode().equals(Comparator.Mode.SUBTRACT);
            } else {
                return rsc.getSecondary() == 1;
            }
        }
        return false;
    }

    public boolean isFlightModeExterior(String uuid) {
        ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, uuid);
        return (rsp.resultSet()) && rsp.getFlightMode() == 4;
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

    public void handleSensor(int id) {
        ResultSetSensors rss = new ResultSetSensors(plugin, id);
        if (rss.resultSet()) {
            SensorToggle toggle = new SensorToggle();
            Block block = toggle.getBlock(rss.getSensors().getHandbrake());
            if (block != null) {
                toggle.setState(block);
            }
        }
    }
}
