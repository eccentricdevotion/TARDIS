package me.eccentric_nz.tardisvortexmanipulator.listeners;

import java.util.HashMap;
import java.util.UUID;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMResultSetBlock;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class TVMMoveListener implements Listener {

    private final TARDIS plugin;

    public TVMMoveListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        if (!plugin.getTvmSettings().getBeaconSetters().contains(uuid)) {
            return;
        }
        // if only the pitch or yaw has changed
        if (compareXYZ(event.getFrom(), event.getTo())) {
            return;
        }
        if (!event.getTo().getBlock().getType().equals(Material.BEACON)) {
            plugin.getTvmSettings().getBeaconSetters().remove(uuid);
            // remove beacon
            TVMResultSetBlock rs = new TVMResultSetBlock(plugin, uuid.toString());
            if (rs.resultSet()) {
                rs.getBlocks().forEach((tvmb) -> {
                    tvmb.getBlock().setBlockData(tvmb.getBlockData());
                    // remove protection
                    plugin.getTvmSettings().getBlocks().remove(tvmb.getBlock().getLocation());
                });
                HashMap<String, Object> where = new HashMap<>();
                where.put("uuid", uuid.toString());
                plugin.getQueryFactory().doDelete("beacons", where);
            }
        }
    }

    private boolean compareXYZ(Location from, Location to) {
        return from.getBlockX() == to.getBlockX() && from.getBlockY() == to.getBlockY() && from.getBlockZ() == to.getBlockZ();
    }
}
