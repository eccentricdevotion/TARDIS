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
package me.eccentric_nz.tardis.enumeration;

import java.util.HashMap;

public enum FlightMode {

	NORMAL(1),
	REGULATOR(2),
	MANUAL(3);

	private static final HashMap<Integer, FlightMode> byMode = new HashMap<>();

	static {
		for (FlightMode fm : values()) {
			byMode.put(fm.mode, fm);
		}
	}

	private final int mode;

	FlightMode(int mode) {
		this.mode = mode;
	}

	public static HashMap<Integer, FlightMode> getByMode() {
		return byMode;
	}

	public int getMode() {
		return mode;
	}
}
