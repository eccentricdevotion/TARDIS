/*
 * Copyright (C) 2025 eccentric_nz
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

import org.mvplugins.multiverse.inventories.MultiverseInventoriesApi;
import org.mvplugins.multiverse.inventories.profile.group.WorldGroup;
import org.mvplugins.multiverse.inventories.profile.group.WorldGroupManager;
import org.mvplugins.multiverse.inventories.share.Sharables;
import org.mvplugins.multiverse.inventories.share.Shares;

import java.util.List;

/**
 * @author eccentric_nz
 */
public class TARDISMultiverseInventoriesChecker {

    public static boolean checkWorldsCanShare(String from, String to) {
        MultiverseInventoriesApi mvi = MultiverseInventoriesApi.get();
        WorldGroupManager gm = mvi.getWorldGroupManager();
        if (gm.hasConfiguredGroup(from)) {
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
