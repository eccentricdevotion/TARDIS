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

import me.eccentric_nz.TARDIS.arch.TARDISWatchData;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * @author eccentric_nz
 */
public final class TARDISChameleonArchEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private final Player player;
    private final TARDISWatchData tardisWatchData;

    /**
     * A TARDIS Chameleon Arch event.
     *
     * @param player          the player who is using the Chameleon Arch
     * @param tardisWatchData the data involved in the event
     */
    public TARDISChameleonArchEvent(Player player, TARDISWatchData tardisWatchData) {
        this.player = player;
        this.tardisWatchData = tardisWatchData;
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
     * Returns the Chameleon Arch {@link me.eccentric_nz.TARDIS.arch.TARDISWatchData TARDISWatchData} object. This
     * contains the 'arched' player's new display name, and the time (in milliseconds) that they became 'arched'.
     *
     * @return the TARDISWatchData object
     */
    public TARDISWatchData getTardisWatchData() {
        return tardisWatchData;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }
}
