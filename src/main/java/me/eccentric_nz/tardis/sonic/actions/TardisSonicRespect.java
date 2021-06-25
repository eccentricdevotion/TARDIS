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
package me.eccentric_nz.tardis.sonic.actions;

import com.griefcraft.cache.ProtectionCache;
import com.griefcraft.lwc.LWC;
import com.griefcraft.model.Protection;
import me.crafter.mc.lockettepro.LocketteProAPI;
import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.utility.TardisFactionsChecker;
import me.eccentric_nz.tardis.utility.TardisGriefPreventionChecker;
import me.eccentric_nz.tardis.utility.TardisRedProtectChecker;
import me.eccentric_nz.tardis.utility.TardisTownyChecker;
import nl.rutgerkok.blocklocker.BlockLockerAPIv2;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class TardisSonicRespect {

    /**
     * Check whether a player can alter a block with their Sonic Screwdriver
     *
     * @param plugin an instance of the tardis plugin
     * @param player the player to check
     * @param block  the block to check
     * @return true if the player has permission to alter the block, otherwise false
     */
    public static boolean checkBlockRespect(TardisPlugin plugin, Player player, Block block) {
        // WorldGuard is probably on server + possibly another protection plugin
        if (plugin.isWorldGuardOnServer() && !plugin.getWorldGuardUtils().canBuild(player, block.getLocation())) {
            return false;
        } else { // keep checking

            // Factions
            if (plugin.getPluginManager().isPluginEnabled("Factions") && plugin.getConfig().getBoolean("preferences.respect_factions")) {
                return TardisFactionsChecker.isInFaction(player, block.getLocation());
            }
            // Towny
            if (plugin.getPluginManager().isPluginEnabled("Towny")) {
                return new TardisTownyChecker(plugin).playerHasPermission(player, block);
            }
            // GriefPrevention
            if (plugin.getPluginManager().isPluginEnabled("GriefPrevention")) {
                return !(new TardisGriefPreventionChecker(plugin).isInClaim(player, block.getLocation()));
            }
            // RedProtect
            if (plugin.getPluginManager().isPluginEnabled("RedProtect")) {
                return TardisRedProtectChecker.canSonic(player, block);
            }
            // LockettePro
            if (plugin.getPluginManager().isPluginEnabled("LockettePro")) {
                return !LocketteProAPI.isProtected(block);
            }
            // LWCX
            if (plugin.getPluginManager().isPluginEnabled("LWC")) {
                ProtectionCache protectionCache = LWC.getInstance().getProtectionCache();
                if (protectionCache != null) {
                    Protection protection = protectionCache.getProtection(block);
                    if (protection != null && !protection.isOwner(player)) {
                        return false;
                    }
                }
            }
            // BlockLocker
            if (plugin.getPluginManager().isPluginEnabled("BlockLocker")) {
                return !BlockLockerAPIv2.isProtected(block);
            }
        }
        return true;
    }
}
