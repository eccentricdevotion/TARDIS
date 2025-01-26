/*
 * Copyright (C) 2024 eccentric_nz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.forcefield;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetForcefield;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisCompanions;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Phantom;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TARDISForceField implements Runnable {

    private final TARDIS plugin;
    private final int range;
    private final int doubleRange;
    private int dust = 0;

    public TARDISForceField(TARDIS plugin) {
        this.plugin = plugin;
        range = this.plugin.getConfig().getInt("allow.force_field");
        plugin.debug("Starting force fields with a range of " + range + " blocks.");
        doubleRange = range * 2;
    }

    public static Vector getTrajectory2d(Location from, Entity to) {
        return to.getLocation().toVector().subtract(from.toVector()).setY(0).normalize();
    }

    public static Vector getTrajectory3d(Location from, Entity to) {
        return to.getLocation().toVector().subtract(from.toVector()).normalize();
    }

    public static void velocity(Entity ent, Vector vec, double strength) {
        if (Double.isNaN(vec.getX()) || Double.isNaN(vec.getY()) || Double.isNaN(vec.getZ()) || vec.length() == 0.0D) {
            return;
        }
        vec.normalize();
        vec.multiply(strength);
        ent.setFallDistance(0.0F);
        ent.setVelocity(vec);
    }

    public static boolean addToTracker(Player player) {
        ResultSetForcefield rsff = new ResultSetForcefield(TARDIS.plugin, player.getUniqueId().toString());
        if (rsff.resultSet()) {
            TARDIS.plugin.getTrackerKeeper().getActiveForceFields().put(rsff.getUuid(), rsff.getLocation());
            return true;
        }
        return false;
    }

    @Override
    public void run() {
        for (Map.Entry<UUID, Location> map : plugin.getTrackerKeeper().getActiveForceFields().entrySet()) {
            Player player = plugin.getServer().getPlayer(map.getKey());
            if (player == null || !player.isOnline()) {
                continue;
            }
            Location location = map.getValue().clone().add(0.5, 0.5, 0.5);
            new TARDISForceFieldVisualiser(plugin).showBorder(location.clone(), dust);
            for (Entity other : location.getWorld().getNearbyEntities(location, doubleRange, doubleRange, doubleRange)) {
                if (!(other instanceof LivingEntity)) {
                    continue;
                }
                if (other instanceof Player) {
                    if (player.equals(other)) {
                        continue;
                    }
                    if (((Player) other).getGameMode() == GameMode.SPECTATOR) {
                        continue;
                    }
                    if (other.hasPermission("tardis.admin")) {
                        continue;
                    }
                    if (isCompanion((Player) other, player)) {
                        continue;
                    }
                }
                if (offset(other, map.getValue()) > range) {
                    continue;
                }
                while (other.getVehicle() != null) {
                    other = other.getVehicle();
                }
//                velocity(other, getTrajectory2d(map.getValue(), other), 0.5d);
                velocity(other, getTrajectory3d(map.getValue(), other), (other instanceof Phantom) ? 1.5d : 0.5d);
                other.getWorld().playSound(other.getLocation(), "tardis_force_field", 0.5f, 1.0f);
            }
            dust++;
            if (dust > 11) {
                dust = 0;
            }
        }
    }

    private double offset(Entity entity, Location location) {
        return (entity.getWorld() != location.getWorld()) ? range + 999.0d : entity.getLocation().toVector().subtract(location.toVector()).length();
    }

    private boolean isCompanion(Player other, Player player) {
        ResultSetTardisCompanions rs = new ResultSetTardisCompanions(plugin);
        if (rs.fromUUID(player.getUniqueId().toString())) {
            List<String> comps;
            if (rs.getCompanions() != null && !rs.getCompanions().isEmpty()) {
                if (rs.getCompanions().equalsIgnoreCase("everyone")) {
                    return true;
                } else {
                    comps = List.of(rs.getCompanions().split(":"));
                    return comps.contains(other.getUniqueId().toString());
                }
            }
        }
        return false;
    }
}
