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

import me.crafter.mc.lockettepro.LocketteProAPI;
import me.eccentric_nz.TARDIS.TARDIS;
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
        // WorldGuard
        if (plugin.isWorldGuardOnServer() && !plugin.getWorldGuardUtils().canBuild(player, block.getLocation())) {
            return false;
        }
        // Towny
        if (plugin.getPM().isPluginEnabled("Towny") && !(new TARDISTownyChecker(plugin).playerHasPermission(player, block))) {
            return false;
        }
        // GriefPrevention
        if (plugin.getPM().isPluginEnabled("GriefPrevention") && new TARDISGriefPreventionChecker(plugin).isInClaim(player, block.getLocation())) {
            return false;
        }
        // LockettePro
        if (plugin.getPM().isPluginEnabled("LockettePro") && LocketteProAPI.isProtected(block)) {
            return false;
        }
        // BlockLocker
        if (plugin.getPM().isPluginEnabled("BlockLocker") && BlockLockerAPIv2.isProtected(block)) {
            return false;
        }
        // Lockette
        if (plugin.getPM().isPluginEnabled("Lockette") && Lockette.isProtected(block)) {
            return false;
        }
        return true;
    }
}
