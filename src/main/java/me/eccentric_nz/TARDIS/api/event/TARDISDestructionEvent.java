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
public final class TARDISDestructionEvent extends Event {

	private static final HandlerList HANDLERS = new HandlerList();
	private final Player player;
	private final Location location;
	private final String owner;

	/**
	 * A tardis extermination event.
	 *
	 * @param player   the player who is destroying the tardis - may be null if it is initiated by the console.
	 * @param location the location of the tardis exterior preset
	 * @param owner    the Time Lord of the tardis
	 */
	public TARDISDestructionEvent(Player player, Location location, String owner) {
		this.player = player;
		this.location = location;
		this.owner = owner;
	}

	public static HandlerList getHandlerList() {
		return HANDLERS;
	}

	public Player getPlayer() {
		return player;
	}

	/**
	 * Returns the exterior location of the tardis being destroyed.
	 *
	 * @return the tardis location
	 */
	public Location getLocation() {
		return location;
	}

	/**
	 * Returns the Time Lord of the tardis being destroyed. This may be different from the player who is doing the
	 * destroying, i.e. a server admin.
	 *
	 * @return the tardis owner's name
	 */
	public String getOwner() {
		return owner;
	}

	@Override
	public @NotNull HandlerList getHandlers() {
		return HANDLERS;
	}
}
