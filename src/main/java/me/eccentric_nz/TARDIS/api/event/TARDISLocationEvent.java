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
package me.eccentric_nz.tardis.api.event;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * @author eccentric_nz
 */
public class TARDISLocationEvent extends Event {

	private static final HandlerList HANDLERS = new HandlerList();
	private final Player player;
	private final int tardisId;
	private final Location location;

	TARDISLocationEvent(Player player, int tardisId, Location location) {
		this.player = player;
		this.tardisId = tardisId;
		this.location = location;
	}

	public static HandlerList getHandlerList() {
		return HANDLERS;
	}

	/**
	 * Returns the player involved in this event.
	 *
	 * @return the player
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * Returns the tardisId of the tardis in the database.
	 *
	 * @return the tardis location
	 */
	public int getTardisId() {
		return tardisId;
	}

	/**
	 * Returns the exterior location of the tardis.
	 *
	 * @return the tardis location
	 */
	public Location getLocation() {
		return location;
	}

	@Override
	public @NotNull HandlerList getHandlers() {
		return HANDLERS;
	}
}
