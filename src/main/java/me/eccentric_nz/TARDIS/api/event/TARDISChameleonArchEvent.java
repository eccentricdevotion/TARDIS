/*
 * Copyright (C) 2026 eccentric_nz
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

import me.eccentric_nz.TARDIS.arch.FobWatchData;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * @author eccentric_nz
 */
public final class TARDISChameleonArchEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private final Player player;
    private final FobWatchData fobWatchData;

    /**
     * A TARDIS Chameleon Arch event.
     *
     * @param player          the player who is using the Chameleon Arch
     * @param fobWatchData the data involved in the event
     */
    public TARDISChameleonArchEvent(Player player, FobWatchData fobWatchData) {
        this.player = player;
        this.fobWatchData = fobWatchData;
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
     * Returns the Chameleon Arch {@link FobWatchData TARDISWatchData} object. This
     * contains the 'arched' player's new display name, and the time (in milliseconds) that they became 'arched'.
     *
     * @return the TARDISWatchData object
     */
    public FobWatchData getTardisWatchData() {
        return fobWatchData;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
}
