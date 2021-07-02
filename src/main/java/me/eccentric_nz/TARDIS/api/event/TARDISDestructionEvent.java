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
package me.eccentric_nz.TARDIS.api.event;

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
     * A TARDIS extermination event.
     *
     * @param player   the player who is destroying the TARDIS - may be null if it is initiated by the console.
     * @param location the location of the TARDIS exterior preset
     * @param owner    the Time Lord of the TARDIS
     */
    public TARDISDestructionEvent(Player player, Location location, String owner) {
        this.player = player;
        this.location = location;
        this.owner = owner;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    /**
     * Gets the player involved in the event
     *
     * @return the player who is destroying the TARDIS
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Returns the exterior location of the TARDIS being destroyed.
     *
     * @return the TARDIS location
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Returns the Time Lord of the TARDIS being destroyed. This may be different from the player who is doing the
     * destroying, i.e. a server admin.
     *
     * @return the TARDIS owner's name
     */
    public String getOwner() {
        return owner;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }
}
