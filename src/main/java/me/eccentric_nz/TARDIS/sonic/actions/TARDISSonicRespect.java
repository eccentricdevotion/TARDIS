/*
 * Copyright (C) 2020 eccentric_nz
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
import me.eccentric_nz.TARDIS.utility.TARDISTownyChecker;
import nl.rutgerkok.blocklocker.BlockLockerAPIv2;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.yi.acru.bukkit.Lockette.Lockette;

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
        boolean allow = true;
        // Factions
        if (plugin.getPM().isPluginEnabled("Factions") && plugin.getConfig().getBoolean("preferences.respect_factions")) {
            allow = TARDISFactionsChecker.isInFaction(player, block.getLocation());
        }
        // WorldGuard
        if (plugin.isWorldGuardOnServer()) {
            allow = plugin.getWorldGuardUtils().canBuild(player, block.getLocation());
        }
        // Towny
        if (plugin.getPM().isPluginEnabled("Towny")) {
            allow = new TARDISTownyChecker(plugin).playerHasPermission(player, block);
        }
        // GriefPrevention
        if (plugin.getPM().isPluginEnabled("GriefPrevention")) {
            allow = !(new TARDISGriefPreventionChecker(plugin).isInClaim(player, block.getLocation()));
        }
        // LockettePro
        if (plugin.getPM().isPluginEnabled("LockettePro")) {
            allow = !LocketteProAPI.isProtected(block);
        }
        // LWCX
        if (plugin.getPM().isPluginEnabled("LWC")) {
            ProtectionCache protectionCache = LWC.getInstance().getProtectionCache();
            if (protectionCache != null) {
                Protection protection = protectionCache.getProtection(block);
                if (protection != null && !protection.isOwner(player)) {
                    allow = false;
                }
            }
        }
        // BlockLocker
        if (plugin.getPM().isPluginEnabled("BlockLocker")) {
            allow = !BlockLockerAPIv2.isProtected(block);
        }
        // Lockette
        if (plugin.getPM().isPluginEnabled("Lockette")) {
            allow = !Lockette.isProtected(block);
        }
        return allow;
    }
}
