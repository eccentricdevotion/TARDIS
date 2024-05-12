package me.eccentric_nz.TARDIS.commands.dev;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItemUtils;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.UUID;

public class TARDISInteractionCommand {

    private final TARDIS plugin;

    public TARDISInteractionCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean process(UUID uuid) {
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", uuid.toString());
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 2);
        if (rs.resultSet()) {
            int id = rs.getTardis().getTardisId();
            HashMap<String, Object> wherec = new HashMap<>();
            wherec.put("tardis_id", id);
            ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherec);
            if (rsc.resultSet()) {
                Location location = new Location(rsc.getWorld(), rsc.getX(), rsc.getY(), rsc.getZ());
                TARDISDisplayItemUtils.setInteraction(location.getBlock(), id);
            }
        }
        return true;
    }
}
