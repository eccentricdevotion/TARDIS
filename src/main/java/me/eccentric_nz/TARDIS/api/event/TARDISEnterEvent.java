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

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * @author eccentric_nz
 */
public final class TARDISEnterEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private final Player player;
    private final World from;

    /**
     * A TARDIS Entry event.
     *
     * @param player the player who is entering the TARDIS.
     * @param from   the world the player is entering from
     */
    public TARDISEnterEvent(Player player, World from) {
        this.player = player;
        this.from = from;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    /**
     * Gets the player involved in the event
     *
     * @return the player who is entering the TARDIS
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets the world involved in the event
     *
     * @return the world the player is entering from
     */
    public World getFrom() {
        return from;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
}
