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
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.tardis.travel;

import me.eccentric_nz.tardis.TARDISPlugin;
import me.eccentric_nz.tardis.api.Parameters;
import me.eccentric_nz.tardis.blueprints.TARDISPermission;
import me.eccentric_nz.tardis.enumeration.COMPASS;
import me.eccentric_nz.tardis.enumeration.Flag;
import me.eccentric_nz.tardis.planets.TARDISAliasResolver;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * The Randomiser is a device that ensures a tardis lands at unpredictable times and places. When activated it scrambles
 * the tardis' coordinate settings, giving the Doctor even less control than usual over the destination of his ship.
 *
 * @author eccentric_nz
 */
public class TARDISRandomiserCircuit {

	private final TARDISPlugin plugin;

	public TARDISRandomiserCircuit(TARDISPlugin plugin) {
		this.plugin = plugin;
	}

	public Location getRandomlocation(Player p, COMPASS d) {
		// get a random world
		Set<String> worldlist = plugin.getPlanetsConfig().getConfigurationSection("planets").getKeys(false);
		List<String> allowedWorlds = new ArrayList<>();
		worldlist.forEach((o) -> {
			World ww = TARDISAliasResolver.getWorldFromAlias(o);
			if (ww != null) {
				if (plugin.getConfig().getBoolean("travel.include_default_world") || !plugin.getConfig().getBoolean("creation.default_world")) {
					if (plugin.getPlanetsConfig().getBoolean("planets." + o + ".time_travel")) {
						allowedWorlds.add(o);
					}
				} else {
					if (!o.equals(plugin.getConfig().getString("creation.default_world_name"))) {
						if (plugin.getPlanetsConfig().getBoolean("planets." + o + ".time_travel")) {
							allowedWorlds.add(o);
						}
					}
				}
				// remove the world if the player doesn't have permission
				if (allowedWorlds.size() > 1 && plugin.getConfig().getBoolean("travel.per_world_perms") && !TARDISPermission.hasPermission(p, "tardis.travel." + o)) {
					allowedWorlds.remove(o);
				}
			}
		});
		Parameters params = new Parameters(p, Flag.getDefaultFlags());
		params.setCompass(d);
		return plugin.getTardisAPI().getRandomLocation(allowedWorlds, null, params);
	}
}
