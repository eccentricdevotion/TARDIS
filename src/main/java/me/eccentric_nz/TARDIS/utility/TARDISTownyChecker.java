/*
 * Copyright (C) 2013 eccentric_nz
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

import com.palmergames.bukkit.towny.Towny;
import com.palmergames.bukkit.towny.object.TownyUniverse;
import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * Due to the age of the TARDIS, it is inclined to break down. The Doctor is
 * often seen with his head stuck in a panel carrying out maintenance of some
 * kind or another, and he occasionally has to give it "percussive maintenance"
 * (a good thump on the console) to get it to start working properly.
 *
 * @author eccentric_nz
 */
public class TARDISTownyChecker {

    private Towny towny;

    public TARDISTownyChecker(TARDIS plugin, boolean onServer) {
        if (onServer) {
            towny = (Towny) plugin.getPM().getPlugin("Towny");
        }
    }

    /**
     * Checks whether a location is in 'wilderness'... ie NOT in a Towny town.
     *
     * @param p the player
     * @param l the location instance to check.
     * @return true or false depending on whether the player can build in this
     * location
     */
    public boolean isWilderness(Player p, Location l) {
        boolean bool = true;
        if (towny != null) {
            if (!TownyUniverse.isWilderness(l.getBlock())) {
                bool = false;
            }
        }
        return bool;
    }
}
