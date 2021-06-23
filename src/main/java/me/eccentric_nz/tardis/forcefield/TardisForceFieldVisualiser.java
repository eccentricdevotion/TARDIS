/*
 * Copyright (C) 2021 eccentric_nz
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
 * along with plugin program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.tardis.forcefield;

import me.eccentric_nz.tardis.TardisConstants;
import me.eccentric_nz.tardis.TardisPlugin;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;

class TardisForceFieldVisualiser {

    private final TardisPlugin plugin;

    TardisForceFieldVisualiser(TardisPlugin plugin) {
        this.plugin = plugin;
    }

    void showBorder(Location location, int d) {

        TardisForceFieldLocation tffl = new TardisForceFieldLocation(location, plugin.getConfig().getDouble("allow.force_field") - 1.0d);

        World world = location.getWorld();
        for (int i = 0; i < 14; i++) {
            // topLeft
            double space = 1.0d;
            assert world != null;
            world.spawnParticle(Particle.REDSTONE, tffl.getTopFrontLeft().add(0, 0, space), 1, TardisConstants.DUST_OPTIONS.get(d));
            // topBack
            world.spawnParticle(Particle.REDSTONE, tffl.getTopBackLeft().add(space, 0, 0), 1, TardisConstants.DUST_OPTIONS.get(d));
            // topRight
            world.spawnParticle(Particle.REDSTONE, tffl.getTopBackRight().add(0, 0, -space), 1, TardisConstants.DUST_OPTIONS.get(d));
            // topFront
            world.spawnParticle(Particle.REDSTONE, tffl.getTopFrontRight().add(-space, 0, 0), 1, TardisConstants.DUST_OPTIONS.get(d));
            // bottomLeft
            world.spawnParticle(Particle.REDSTONE, tffl.getBottomFrontLeft().add(0, 0, space), 1, TardisConstants.DUST_OPTIONS.get(d));
            // bottomBack
            world.spawnParticle(Particle.REDSTONE, tffl.getBottomBackLeft().add(space, 0, 0), 1, TardisConstants.DUST_OPTIONS.get(d));
            // bottomRight
            world.spawnParticle(Particle.REDSTONE, tffl.getBottomBackRight().add(0, 0, -space), 1, TardisConstants.DUST_OPTIONS.get(d));
            // bottomFront
            world.spawnParticle(Particle.REDSTONE, tffl.getBottomFrontRight().add(-space, 0, 0), 1, TardisConstants.DUST_OPTIONS.get(d));
        }
    }
}