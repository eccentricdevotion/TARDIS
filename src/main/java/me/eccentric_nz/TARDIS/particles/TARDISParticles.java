/*
 * Copyright (C) 2026 eccentric_nz
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
package me.eccentric_nz.TARDIS.particles;

import me.eccentric_nz.TARDIS.TARDISConstants;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/**
 * @author eccentric_nz
 */
public class TARDISParticles {

    public static void sendVortexParticles(Location l, Player p) {
        double radials = 0.19634954084936207d;
        int step = 0;
        for (int x = 0; x < 20; x++) {
            for (int i = 0; i < 10; i++) {
                double angle = step * radials + 6.283185307179586D * i / 10;
                Vector v = new Vector(Math.cos(angle) * 3, step * 0.2f, Math.sin(angle) * 3);
                v = rotateAroundAxisX(v, l.getPitch() * 0.017453292F);
                v = rotateAroundAxisY(v, -l.getYaw() * 0.017453292F);
                l.add(v);
                p.spawnParticle(Particle.ENTITY_EFFECT, l, 10, 0, 0, 0, 0, Color.WHITE, false);
                l.subtract(v);
            }
            step += 1;
        }
    }

    public static void sendSnowParticles(Location l, Player p) {
        for (int i = 0; i < 20; i++) {
            double x = randomDouble();
            double y = randomDouble();
            double z = randomDouble();
            l.add(x, y, z);
            if (l.getBlock().isEmpty()) {
                if (l.getWorld().getHighestBlockYAt(l) + 1 < l.getY()) {
                    p.spawnParticle(Particle.CLOUD, l, 10);
                }
            }
            l.subtract(x, y, z);
        }
    }

    private static Vector rotateAroundAxisX(Vector v, double angle) {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        double y = v.getY() * cos - v.getZ() * sin;
        double z = v.getY() * sin + v.getZ() * cos;
        return v.setY(y).setZ(z);
    }

    private static Vector rotateAroundAxisY(Vector v, double angle) {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        double x = v.getX() * cos + v.getZ() * sin;
        double z = v.getX() * -sin + v.getZ() * cos;
        return v.setX(x).setZ(z);
    }

    private static Double randomDouble() {
        return 10.0 * TARDISConstants.RANDOM.nextDouble();
    }
}
