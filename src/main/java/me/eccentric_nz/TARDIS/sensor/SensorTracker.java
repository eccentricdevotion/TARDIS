package me.eccentric_nz.TARDIS.sensor;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.artron.TARDISArtronLevels;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCharging;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetSensors;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.HashSet;
import java.util.Set;

public class SensorTracker {

    public static Set<Integer> isCharging = new HashSet<>();

    public static void resetChargingSensors() {
        for (int id : isCharging) {
            ResultSetSensors rss = new ResultSetSensors(TARDIS.plugin, id);
            if (rss.resultSet()) {
                SensorToggle toggle = new SensorToggle();
                Block block = toggle.getBlock(rss.getSensors().getCharging());
                if (block != null && block.getType() == Material.REDSTONE_BLOCK) {
                    toggle.setState(block);
                }
            }
        }
    }

    // restart recharging after server restarts
    public static void restartCharging(TARDIS plugin) {
        ResultSetCharging rsc = new ResultSetCharging(plugin);
        if (rsc.resultSet()) {
            for (int id : rsc.getData()) {
                new TARDISArtronLevels(plugin).recharge(id);
            }
        }
    }
}
