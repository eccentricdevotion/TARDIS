package me.eccentric_nz.TARDIS.sonic.actions;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetTardisID;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

public class TARDISSonicDispersed {

    public static void assemble(TARDIS plugin, Player player) {
        // check player's location
        Location tmp = player.getLocation();
        Location pl = new Location(tmp.getWorld(), tmp.getBlockX(), tmp.getBlockY(), tmp.getBlockZ());
        Location pb = plugin.getTrackerKeeper().getDispersed().get(player.getUniqueId());
        if (pl.equals(pb)) {
            UUID uuid = player.getUniqueId();
            // get TARDIS id
            ResultSetTardisID rs = new ResultSetTardisID(plugin);
            if (rs.fromUUID(uuid.toString())) {
                // rebuild
                plugin.getTrackerKeeper().getDispersed().remove(uuid);
                plugin.getTrackerKeeper().getDispersedTARDII().remove(Integer.valueOf(rs.getTardis_id()));
                player.performCommand("tardis rebuild");
            }
        }
    }
}
