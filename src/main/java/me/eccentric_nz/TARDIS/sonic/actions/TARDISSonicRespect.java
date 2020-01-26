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
            wgu = !plugin.getWorldGuardUtils().canBuild(player, block.getLocation());
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
            tny = new TARDISTownyChecker(plugin).checkTowny(player, block.getLocation());
        }
        return (gpr || wgu || lke || pro || bll || tny);
    }
}
