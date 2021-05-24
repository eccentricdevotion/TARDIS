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
package me.eccentric_nz.tardis.utility;

import me.eccentric_nz.tardis.TARDISPlugin;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * The tardis grants its passengers the ability to understand and speak other languages. This is due to the tardis's
 * telepathic field.
 *
 * @author eccentric_nz
 */
public class TARDISGriefPreventionChecker {

	private final GriefPrevention griefprevention;

	public TARDISGriefPreventionChecker(TARDISPlugin plugin) {
		griefprevention = (GriefPrevention) plugin.getPM().getPlugin("GriefPrevention");
	}

	/**
	 * Checks to see whether the specified location is within a GriefPrevention claim.
	 *
	 * @param p the player to check access for.
	 * @param l the location to check.
	 * @return true or false depending on whether the player has access to the claim
	 */
	public boolean isInClaim(Player p, Location l) {
		boolean bool = true;
		if (griefprevention != null) {
			Claim claim = griefprevention.dataStore.getClaimAt(l, true, null);
			// if no claim at this location or the player has access to the claim - allow
			if (claim == null || claim.allowAccess(p) == null) {
				bool = false;
			}
		}
		return bool;
	}
}
