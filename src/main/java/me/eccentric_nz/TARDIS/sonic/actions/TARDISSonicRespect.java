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
import nl.rutgerkok.blocklocker.BlockLockerAPI;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.yi.acru.bukkit.Lockette.Lockette;

public class TARDISSonicRespect {

    public static boolean checkBlockRespect(TARDIS plugin, Player player, Block block) {
        boolean gpr = false;
        boolean wgu = false;
        boolean lke = false;
        boolean pro = false;
        boolean bll = false;
        boolean tny = false;
        // GriefPrevention
        if (plugin.getPM().isPluginEnabled("GriefPrevention")) {
            gpr = new TARDISGriefPreventionChecker(plugin).isInClaim(player, block.getLocation());
        }
        // WorldGuard
        if (plugin.isWorldGuardOnServer()) {
            wgu = plugin.getWorldGuardUtils().canBuild(player, block.getLocation());
        }
        // Lockette
        if (plugin.getPM().isPluginEnabled("Lockette")) {
            lke = Lockette.isProtected(block);
        }
        if (plugin.getPM().isPluginEnabled("LockettePro")) {
            pro = LocketteProAPI.isProtected(block);
        }
        // BlockLocker
        if (plugin.getPM().isPluginEnabled("BlockLocker")) {
            bll = BlockLockerAPI.isProtected(block);
        }
        // Towny
        if (plugin.getPM().isPluginEnabled("Towny")) {
            tny = new TARDISTownyChecker(plugin).playerHasPermission(player, block);
        }
        return (gpr || !wgu || lke || pro || bll || !tny);
    }
}
