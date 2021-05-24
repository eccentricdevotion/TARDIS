/*
 * Copyright (C) 2020 eccentric_nz
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

import me.eccentric_nz.tardis.TARDISPlugin;
import me.eccentric_nz.tardis.TARDISConstants;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;

public class TARDISForceFieldVisualiser {

	private final TARDISPlugin plugin;
	private final double SPACE = 1.0d;

	public TARDISForceFieldVisualiser(TARDISPlugin plugin) {
		this.plugin = plugin;
	}

	public void showBorder(Location location, int d) {

		TARDISForceFieldLocation tffl = new TARDISForceFieldLocation(location, plugin.getConfig().getDouble("allow.force_field") - 1.0d);

		World world = location.getWorld();
		for (int i = 0; i < 14; i++) {
			// topLeft
			world.spawnParticle(Particle.REDSTONE, tffl.getTopFrontLeft().add(0, 0, SPACE), 1, TARDISConstants.DUSTOPTIONS.get(d));
			// topBack
			world.spawnParticle(Particle.REDSTONE, tffl.getTopBackLeft().add(SPACE, 0, 0), 1, TARDISConstants.DUSTOPTIONS.get(d));
			// topRight
			world.spawnParticle(Particle.REDSTONE, tffl.getTopBackRight().add(0, 0, -SPACE), 1, TARDISConstants.DUSTOPTIONS.get(d));
			// topFront
			world.spawnParticle(Particle.REDSTONE, tffl.getTopFrontRight().add(-SPACE, 0, 0), 1, TARDISConstants.DUSTOPTIONS.get(d));
			// bottomLeft
			world.spawnParticle(Particle.REDSTONE, tffl.getBottomFrontLeft().add(0, 0, SPACE), 1, TARDISConstants.DUSTOPTIONS.get(d));
			// bottomBack
			world.spawnParticle(Particle.REDSTONE, tffl.getBottomBackLeft().add(SPACE, 0, 0), 1, TARDISConstants.DUSTOPTIONS.get(d));
			// bottomRight
			world.spawnParticle(Particle.REDSTONE, tffl.getBottomBackRight().add(0, 0, -SPACE), 1, TARDISConstants.DUSTOPTIONS.get(d));
			// bottomFront
			world.spawnParticle(Particle.REDSTONE, tffl.getBottomFrontRight().add(-SPACE, 0, 0), 1, TARDISConstants.DUSTOPTIONS.get(d));
		}
	}
}
