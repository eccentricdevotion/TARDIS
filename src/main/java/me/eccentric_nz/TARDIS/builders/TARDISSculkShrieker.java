package me.eccentric_nz.TARDIS.builders;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetShrieker;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import me.eccentric_nz.tardischunkgenerator.helpers.Shrieker;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.SculkShrieker;

public class TARDISSculkShrieker {

    public static void setRotor(int id) {
        ResultSetShrieker rs = new ResultSetShrieker(TARDIS.plugin, id);
        if (rs.resultSet()) {
            Location location = TARDISStaticLocationGetters.getLocationFromBukkitString(rs.getLocation());
            Block block = location.getBlock();
            if (block.getType() == Material.SCULK_SHRIEKER) {
                SculkShrieker shrieker = (SculkShrieker) block.getBlockData();
                int task = TARDIS.plugin.getServer().getScheduler().scheduleSyncRepeatingTask(TARDIS.plugin, () -> {
                    Shrieker.shriek(block);
                }, 2L, 90L);
                TARDIS.plugin.getTrackerKeeper().getShriekers().put(id, task);
            }
        }
    }

    public static void stopRotor(int id) {
        int task = TARDIS.plugin.getTrackerKeeper().getShriekers().getOrDefault(id, -1);
        if (task != -1) {
            TARDIS.plugin.getServer().getScheduler().cancelTask(task);
            ResultSetShrieker rs = new ResultSetShrieker(TARDIS.plugin, id);
            if (rs.resultSet()) {
                Location location = TARDISStaticLocationGetters.getLocationFromBukkitString(rs.getLocation());
                Block block = location.getBlock();
                if (block.getType() == Material.SCULK_SHRIEKER) {
                    SculkShrieker shrieker = (SculkShrieker) block.getBlockData();
                    shrieker.setShrieking(false);
                    block.setBlockData(shrieker);
                }
            }
        }
    }
}
