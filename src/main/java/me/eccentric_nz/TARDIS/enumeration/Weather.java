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
package me.eccentric_nz.tardis.enumeration;

public enum Weather {

	CLEAR,
	RAIN,
	THUNDER;

	public static Weather fromString(String s) {
		String lower = s.toLowerCase();
		return switch (lower) {
			case "r", "rain", "w", "wet" -> RAIN;
			case "t", "thunder", "l", "lightning" -> THUNDER;
			default -> CLEAR;
		};
	}
}
