package me.eccentric_nz.TARDIS.utility;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetForcefield;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Map;
import java.util.UUID;

public class TARDISForceField implements Runnable {

    private final TARDIS plugin;
    private final int range;

    public TARDISForceField(TARDIS plugin) {
        this.plugin = plugin;
        range = this.plugin.getConfig().getInt("allow.force_field");
    }

    @Override
    public void run() {
        for (Map.Entry<UUID, Location> map : plugin.getTrackerKeeper().getActiveForceFields().entrySet()) {
            Player player = plugin.getServer().getPlayer(map.getKey());
            if (player == null || !player.isOnline()) {
                continue;
            }
            for (Player other : Bukkit.getServer().getOnlinePlayers()) {
                if (player.equals(other)) {
                    continue;
                }
                if (offset(other, map.getValue()) > range) {
                    continue;
                }
                if (other.getGameMode() == GameMode.SPECTATOR) {
                    continue;
                }
                if (other.hasPermission("tardis.admin")) {
                    continue;
                }
                Entity entity = other;
                while (entity.getVehicle() != null) {
                    entity = entity.getVehicle();
                }
                velocity(entity, getTrajectory2d(player, entity), 1.6d, true, 0.8d, 0.0d, 10.0d);
                other.getWorld().playSound(other.getLocation(), "tardis_force_field", 1.0f, 1.0f);
            }
        }
    }

    private double offset(Entity entity, Location location) {
        return entity.getLocation().toVector().subtract(location.toVector()).length();
    }

    private Vector getTrajectory2d(Entity from, Entity to) {
        return to.getLocation().toVector().subtract(from.getLocation().toVector()).setY(0).normalize();
    }

    private void velocity(Entity ent, Vector vec, double str, boolean ySet, double yBase, double yAdd, double yMax) {
        if (Double.isNaN(vec.getX()) || Double.isNaN(vec.getY()) || Double.isNaN(vec.getZ()) || vec.length() == 0.0D) {
            return;
        }
        if (ySet) {
            vec.setY(yBase);
        }
        vec.normalize();
        vec.multiply(str);
        vec.setY(vec.getY() + yAdd);
        if (vec.getY() > yMax) {
            vec.setY(yMax);
        }
        ent.setFallDistance(0.0F);
        ent.setVelocity(vec);
    }

    public static void addToTracker(Player player) {
        ResultSetForcefield rsff = new ResultSetForcefield(TARDIS.plugin, player.getUniqueId().toString());
        if (rsff.resultSet()) {
            TARDIS.plugin.getTrackerKeeper().getActiveForceFields().put(rsff.getUuid(), rsff.getLocation());
        }
    }
}
