package me.eccentric_nz.TARDIS.sensor;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetSensors;
import org.bukkit.block.Block;

public class HandbrakeSensor {

    private final TARDIS plugin;
    private final int id;

    public HandbrakeSensor(TARDIS plugin, int id) {
        this.plugin = plugin;
        this.id = id;
    }

    public void toggle() {
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
