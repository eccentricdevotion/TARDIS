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
package me.eccentric_nz.tardis.api.event;

import me.eccentric_nz.tardis.TARDISPlugin;
import me.eccentric_nz.tardis.database.resultset.ResultSetTardisArtron;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class TARDISArtronEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private final Player player;
    private final int amount;
    private final int tardisId;
    private int artronLevel = 0;

    public TARDISArtronEvent(Player player, int amount, int tardisId) {
        this.player = player;
        this.amount = amount;
        this.tardisId = tardisId;
        ResultSetTardisArtron rs = new ResultSetTardisArtron(TARDISPlugin.plugin);
        if (rs.fromID(this.tardisId)) {
            artronLevel = rs.getArtronLevel();
        }
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
     * Returns the amount of energy involved in this event. This could be a positive or negative amount.
     *
     * @return the amount
     */
    public int getAmount() {
        return amount;
    }

    /**
     * Returns the tardis id involved in this event.
     *
     * @return the tardis id
     */
    public int getTardisId() {
        return tardisId;
    }

    /**
     * Returns the Artron Level after the amount has been added / subtracted.
     *
     * @return the player
     */
    public int getLevel() {
        return artronLevel;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }
}
