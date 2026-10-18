package me.eccentric_nz.TARDIS.listeners;

import com.destroystokyo.paper.event.player.PlayerPostRespawnEvent;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.builders.interior.TARDISInteriorPositioning;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisID;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTravellers;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashMap;

public class RespawnListener implements Listener {

    private  final TARDIS plugin;

    public RespawnListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onRespawn(PlayerPostRespawnEvent event) {
        Location location = event.getRespawnLocation();
        if (plugin.getUtils().inTARDISWorld(location)) {
            Player player = event.getPlayer();
            // set TARDIS to occupied?
            HashMap<String, Object> where = new HashMap<>();
            where.put("uuid", player.getUniqueId().toString());
            ResultSetTravellers rst = new ResultSetTravellers(plugin, where, false);
            if (rst.resultSet()) {
                // already in the TARDIS world
                return;
            }
            ResultSetTardisID rsid = new ResultSetTardisID(plugin);
            // if TIPS determine tardis_id from player location
            if (plugin.getConfig().getBoolean("creation.default_world") && !player.hasPermission("tardis.create_world")) {
                int slot = TARDISInteriorPositioning.getTIPSSlot(player.getLocation());
                if (!rsid.fromTIPSSlot(slot)) {
                    return;
                }
            } else if (!rsid.fromUUID(player.getUniqueId().toString())) {
                return;
            }
            int id = rsid.getTardisId();
            HashMap<String, Object> insert = new HashMap<>();
            insert.put("tardis_id", id);
            insert.put("uuid", player.getUniqueId().toString());
            plugin.getQueryFactory().doInsert("travellers", insert);
        }
    }
}
