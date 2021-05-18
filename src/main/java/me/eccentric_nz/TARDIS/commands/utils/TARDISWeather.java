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
package me.eccentric_nz.TARDIS.commands.utils;

import me.eccentric_nz.TARDIS.enumeration.Weather;
import org.bukkit.World;

public class TARDISWeather {

	public static void setRain(World world) {
		world.setStorm(true);
	}

	public static void setThunder(World world) {
		world.setStorm(true);
		world.setThundering(true);
	}

	public static void setClear(World world) {
		world.setThundering(false);
		world.setStorm(false);
	}

	public static void setWeather(World world, Weather weather) {
		switch (weather) {
			case RAIN:
				setRain(world);
				break;
			case THUNDER:
				setThunder(world);
				break;
			default:
				setClear(world);
				break;
		}
	}
}
