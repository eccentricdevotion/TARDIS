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
package me.eccentric_nz.TARDIS.sonic.actions;

import com.griefcraft.cache.ProtectionCache;
import com.griefcraft.lwc.LWC;
import com.griefcraft.model.Protection;
import me.crafter.mc.lockettepro.LocketteProAPI;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.utility.TARDISFactionsChecker;
import me.eccentric_nz.TARDIS.utility.TARDISGriefPreventionChecker;
import me.eccentric_nz.TARDIS.utility.TARDISRedProtectChecker;
import me.eccentric_nz.TARDIS.utility.TARDISTownyChecker;
import nl.rutgerkok.blocklocker.BlockLockerAPIv2;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class TARDISSonicRespect {

    /**
     * Check whether a player can alter a block with their Sonic Screwdriver
     *
     * @param plugin an instance of the TARDIS plugin
     * @param player the player to check
     * @param block  the block to check
     * @return true if the player has permission to alter the block, otherwise false
     */
    public static boolean checkBlockRespect(TARDIS plugin, Player player, Block block) {
        // WorldGuard is probably on server + possibly another protection plugin
        if (plugin.isWorldGuardOnServer() && !plugin.getWorldGuardUtils().canBuild(player, block.getLocation())) {
            return false;
        } else { // keep checking

            // Factions
            if (plugin.getPM().isPluginEnabled("Factions") && plugin.getConfig().getBoolean("preferences.respect_factions")) {
                return TARDISFactionsChecker.isInFaction(player, block.getLocation());
            }
            // Towny
            if (plugin.getPM().isPluginEnabled("Towny")) {
                return new TARDISTownyChecker(plugin).playerHasPermission(player, block);
            }
            // GriefPrevention
            if (plugin.getPM().isPluginEnabled("GriefPrevention")) {
                return !(new TARDISGriefPreventionChecker(plugin).isInClaim(player, block.getLocation()));
            }
            // RedProtect
            if (plugin.getPM().isPluginEnabled("RedProtect")) {
                return TARDISRedProtectChecker.canSonic(player, block);
            }
            // LockettePro
            if (plugin.getPM().isPluginEnabled("LockettePro")) {
                return !LocketteProAPI.isProtected(block);
            }
            // LWCX
            if (plugin.getPM().isPluginEnabled("LWC")) {
                ProtectionCache protectionCache = LWC.getInstance().getProtectionCache();
                if (protectionCache != null) {
                    Protection protection = protectionCache.getProtection(block);
                    if (protection != null && !protection.isOwner(player)) {
                        return false;
                    }
                }
            }
            // BlockLocker
            if (plugin.getPM().isPluginEnabled("BlockLocker")) {
                return !BlockLockerAPIv2.isProtected(block);
            }
        }
        return true;
    }
}
