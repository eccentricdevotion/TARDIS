package me.eccentric_nz.TARDIS.control.actions;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.builders.LightLevel;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetControls;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Light;

import java.util.HashMap;

public class ConsoleLampAction {

    private final TARDIS plugin;

    public ConsoleLampAction(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void illuminate(int id, int level, int control) {
        int setLevel = (level + 1) > 7 ? 0 : level + 1;
        // set control record
        HashMap<String, Object> set = new HashMap<>();
        HashMap<String, Object> wherec = new HashMap<>();
        if (control != -1) {
            set.put("secondary", setLevel);
            wherec.put("c_id", control);
        } else {
            set.put("secondary", setLevel);
            wherec.put("tardis_id", id);
            wherec.put("type", 56);
        }
        plugin.getQueryFactory().doSyncUpdate("controls", set, wherec);
        // get console lamp record
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        where.put("type", 56);
        ResultSetControls rs = new ResultSetControls(plugin, where, false);
        if (rs.resultSet()) {
            // get the console lamp light block
            Location location = TARDISStaticLocationGetters.getLocationFromBukkitString(rs.getLocation());
            if (location != null) {
                Block block = location.getBlock();
                if (block.getType() == Material.LIGHT) {
                    Light light = (Light) block.getBlockData();
                    light.setLevel(LightLevel.interior_level[control == -1 ? level : setLevel]);
                    block.setBlockData(light);
                }
            }
        }
    }
}
