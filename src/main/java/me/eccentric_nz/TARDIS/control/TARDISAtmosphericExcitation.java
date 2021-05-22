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
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.tardis.control;

import me.eccentric_nz.tardis.TARDIS;
import me.eccentric_nz.tardis.database.resultset.ResultSetCurrentLocation;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.HashMap;

/**
 * @author eccentric_nz
 * <p>
 * Atmospheric excitation is an unnatural disturbance in the atmosphere which causes the weather to change. The Tenth
 * Doctor's sonic screwdriver, the tardis, and moving a planet can all cause atmospheric excitation.
 * <p>
 * The Tenth Doctor used a device above the inside of the door of the tardis to excite the atmosphere, causing snow, in
 * an attempt to cheer up Donna Noble.
 */
public class TARDISAtmosphericExcitation {

	private final TARDIS plugin;

	public TARDISAtmosphericExcitation(TARDIS plugin) {
		this.plugin = plugin;
	}

	public void excite(int tid, Player p) {
		HashMap<String, Object> where = new HashMap<>();
		where.put("tardis_id", tid);
		ResultSetCurrentLocation rs = new ResultSetCurrentLocation(plugin, where);
		if (rs.resultSet()) {
			// not if underwater
			if (rs.isSubmarine()) {
				return;
			}
			// get tardis location
			Location l = new Location(rs.getWorld(), rs.getX(), rs.getY(), rs.getZ());
			// get lamp block location
			l.add(0, 18, 0);
			// construct a firework effect and shoot it from lamp block location
			Firework firework = (Firework) l.getWorld().spawnEntity(l, EntityType.FIREWORK);
			FireworkMeta fireworkMeta = firework.getFireworkMeta();
			fireworkMeta.addEffect(FireworkEffect.builder().flicker(false).withColor(Color.SILVER).withFade(Color.WHITE).with(Type.BURST).withTrail().build());
			fireworkMeta.setPower(3);
			firework.setFireworkMeta(fireworkMeta);
			plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
				firework.detonate();
				// after x ticks, start snow particles and place snow on ground
				TARDISExcitationRunnable runnable = new TARDISExcitationRunnable(plugin, l, p);
				runnable.task = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, runnable, 35L, 10L);
			}, 2L);
		}
	}
}
