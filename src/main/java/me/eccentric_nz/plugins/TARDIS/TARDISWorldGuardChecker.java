package me.eccentric_nz.plugins.TARDIS;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class TARDISWorldGuardChecker {

    private TARDIS plugin;
    private WorldGuardPlugin wg;
    private boolean WorldGuardOnServer = false;

    public TARDISWorldGuardChecker(TARDIS plugin) {
        this.plugin = plugin;
        Plugin wgp = plugin.getServer().getPluginManager().getPlugin("WorldGuard");
        WorldGuardOnServer = (wgp != null);
        if (WorldGuardOnServer) {
            wg = ((WorldGuardPlugin) wgp);
        }
    }

    public boolean cantBuild(Player p, Location l) {
        return (WorldGuardOnServer) && (!wg.canBuild(p, l));
    }
}