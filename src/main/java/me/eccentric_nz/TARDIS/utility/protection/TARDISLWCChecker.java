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
package me.eccentric_nz.TARDIS.utility.protection;

import com.griefcraft.cache.ProtectionCache;
import com.griefcraft.lwc.LWC;
import com.griefcraft.model.Protection;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class TARDISLWCChecker {

    public boolean isBlockProtected(Block block, Player player) {
        ProtectionCache protectionCache = LWC.getInstance().getProtectionCache();
        if (protectionCache != null) {
            Protection protection = protectionCache.getProtection(block);
            if (protection != null && !protection.isOwner(player)) {
                return true;
            }
        }
        return false;
    }

    public boolean isBlockProtected(Block eyeBlock, Block under, Player player) {
        ProtectionCache protectionCache = LWC.getInstance().getProtectionCache();
        if (protectionCache != null) {
            Protection protection = protectionCache.getProtection(eyeBlock);
            Protection underProtection = protectionCache.getProtection(under);
            if (protection != null && !protection.isOwner(player) || underProtection != null && !underProtection.isOwner(player)) {
                return true;
            }
        }
        return false;
    }
}
