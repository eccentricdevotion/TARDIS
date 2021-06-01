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

import java.util.HashMap;

public enum Time {

	DAY(0),
	MORNING(0),
	NOON(6000),
	NIGHT(12000),
	MIDNIGHT(18000),
	AM_6(0),
	AM_7(1000),
	AM_8(2000),
	AM_9(3000),
	AM_10(4000),
	AM_11(5000),
	PM_12(6000),
	PM_1(7000),
	PM_2(8000),
	PM_3(9000),
	PM_4(10000),
	PM_5(11000),
	PM_6(12000),
	PM_7(13000),
	PM_8(14000),
	PM_9(15000),
	PM_10(16000),
	PM_11(17000),
	AM_12(18000),
	AM_1(19000),
	AM_2(20000),
	AM_3(21000),
	AM_4(22000),
	AM_5(23000);

	private static final HashMap<String, Time> BY_NAME = new HashMap<>();

	static {
		for (Time time : values()) {
			BY_NAME.put(time.name, time);
		}
	}

	private final long ticks;
	private final String name;

	Time(long ticks) {
		this.ticks = ticks;
		name = getName();
	}

	public static HashMap<String, Time> getByName() {
		return BY_NAME;
	}

	public long getTicks() {
		return ticks;
	}

	private String getName() {
		String[] split = toString().split("_");
		return (split.length == 2) ? split[1] + split[0] : split[0];
	}
}
