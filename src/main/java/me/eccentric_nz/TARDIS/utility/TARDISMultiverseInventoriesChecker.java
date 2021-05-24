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

import com.onarandombox.multiverseinventories.MultiverseInventories;
import com.onarandombox.multiverseinventories.WorldGroup;
import com.onarandombox.multiverseinventories.profile.WorldGroupManager;
import com.onarandombox.multiverseinventories.share.Sharables;
import com.onarandombox.multiverseinventories.share.Shares;
import org.bukkit.Bukkit;

import java.util.List;

/**
 * @author eccentric_nz
 */
public class TARDISMultiverseInventoriesChecker {

	public static boolean checkWorldsCanShare(String from, String to) {
		MultiverseInventories mvi = (MultiverseInventories) Bukkit.getServer().getPluginManager().getPlugin("Multiverse-Inventories");
		WorldGroupManager gm = mvi.getGroupManager();
		if (gm.hasGroup(from)) {
			List<WorldGroup> profiles = gm.getGroupsForWorld(from);
			for (WorldGroup wgp : profiles) {
				if (wgp.containsWorld(to)) {
					Shares shares = wgp.getShares();
					if (!shares.isSharing(Sharables.INVENTORY) && !shares.isSharing(Sharables.ALL_INVENTORY) && !shares.isSharing(Sharables.ALL_DEFAULT)) {
						return false;
					}
				} else {
					return false;
				}
			}
		} else {
			return false;
		}
		return true;
	}
}
