package me.eccentric_nz.TARDIS.flight.vehicle;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.builders.BuildData;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentWithPreset;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.EntitiesLoadEvent;
import org.bukkit.event.world.WorldLoadEvent;

public class VehicleLoadListener implements Listener {

    private final TARDIS plugin;

    public VehicleLoadListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onVehicleLoad(EntitiesLoadEvent event) {
        for (Entity e : event.getEntities()) {
            if (e.getType() == EntityType.ARMOR_STAND) {
                Location location = e.getLocation();
                ResultSetCurrentWithPreset rsc = new ResultSetCurrentWithPreset(plugin, location.getWorld().getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ());
                if (rsc.resultSet()) {
                    BuildData data = new BuildData(null);
                    data.setTardisID(rsc.getId());
                    data.setAddSign(rsc.hasSign());
                    VehicleUtility.convertStand(location, rsc.getPreset(), data);
                }
            }
        }
    }

    @EventHandler
    public void onWorldLoad(WorldLoadEvent event) {
        for (Entity e : event.getWorld().getEntities()) {
            if (e.getType() == EntityType.ARMOR_STAND) {
                Location location = e.getLocation();
                ResultSetCurrentWithPreset rsc = new ResultSetCurrentWithPreset(plugin, location.getWorld().getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ());
                if (rsc.resultSet()) {
                    BuildData data = new BuildData(null);
                    data.setTardisID(rsc.getId());
                    data.setAddSign(rsc.hasSign());
                    VehicleUtility.convertStand(location, rsc.getPreset(), data);
                }
            }
        }
    }
}
