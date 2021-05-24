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
package me.eccentric_nz.tardis.api;

import org.bukkit.Location;

import java.util.List;

/**
 * @author eccentric_nz
 */
public class TARDISData {

	private final Location location;
	private final String console;
	private final String chameleon;
	private final String door;
	private final String powered;
	private final String siege;
	private final String abandoned;
	private final List<String> occupants;

	TARDISData(Location location, String console, String chameleon, String door, String powered, String siege, String abandoned, List<String> occupants) {
		this.location = location;
		this.console = console;
		this.chameleon = chameleon;
		this.door = door;
		this.powered = powered;
		this.siege = siege;
		this.abandoned = abandoned;
		this.occupants = occupants;
	}

	public Location getLocation() {
		return location;
	}

	public String getConsole() {
		return console;
	}

	public String getChameleon() {
		return chameleon;
	}

	public String getDoor() {
		return door;
	}

	public String getPowered() {
		return powered;
	}

	public String getSiege() {
		return siege;
	}

	public String getAbandoned() {
		return abandoned;
	}

	public List<String> getOccupants() {
		return occupants;
	}
}
