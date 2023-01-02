/*
 * Copyright (C) 2023 eccentric_nz
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
package me.eccentric_nz.TARDIS.utility;

import com.massivecraft.factions.*;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * The Cloister Bell is a signal to the crew that a galaxy-scale catastrophe is occurring or a warning that they are in
 * immediate danger. In short it is a call to "battle-stations." Though it is located in the Cloister Room, the Bell can
 * be heard from anywhere in a TARDIS.
 *
 * @author eccentric_nz
 */
class TARDISFactionsUUID {

    /**
     * Checks whether a location is in the player's faction or 'wilderness'... ie NOT in a claimed faction that this
     * player doesn't belong to.
     *
     * @param p a player
     * @param l the location instance to check.
     * @return true or false depending on whether the player belongs to the faction who controls the location
     */
    boolean isInFaction(Player p, Location l) {
        boolean bool = true;
        FPlayer uplayer = FPlayers.getInstance().getByPlayer(p);
        Faction ufac = uplayer.getFaction();
        FLocation flocation = new FLocation(l);
        Faction lfac = Board.getInstance().getFactionAt(flocation);
        if (!ufac.equals(lfac) && !lfac.isWilderness()) {
            bool = false;
        }
        return bool;
    }
}
