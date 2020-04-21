package me.eccentric_nz.TARDIS.flight;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetControls;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Switch;

import java.util.HashMap;

public class TARDISHandbrake {

    public static void setLevers(Block block, boolean powered, boolean inside, String handbrake_loc, int id, TARDIS plugin) {
        Switch lever = (Switch) block.getBlockData();
        lever.setPowered(powered);
        block.setBlockData(lever);
        if (inside) {
            // get other handbrakes in this TARDIS
            HashMap<String, Object> where = new HashMap<>();
            where.put("tardis_id", id);
            where.put("type", 0);
            ResultSetControls rsc = new ResultSetControls(plugin, where, true);
            if (rsc.resultSet()) {
                for (HashMap<String, String> map : rsc.getData()) {
                    if (!map.get("location").equals(handbrake_loc)) {
                        Location location = TARDISStaticLocationGetters.getLocationFromBukkitString(map.get("location"));
                        if (location != null) {
                            Block other = location.getBlock();
                            BlockData blockData = other.getBlockData();
                            if (blockData instanceof Switch) {
                                Switch brake = (Switch) blockData;
                                brake.setPowered(powered);
                                other.setBlockData(brake);
                            } else {
                                // remove the control record because the lever no longer exists
                                HashMap<String, Object> wherec = new HashMap<>();
                                wherec.put("tardis_id", id);
                                wherec.put("type", 0);
                                wherec.put("location", map.get("location"));
                                new QueryFactory(plugin).doDelete("controls", wherec);
                            }
                        }
                    }
                }
            }
        }
    }
}
