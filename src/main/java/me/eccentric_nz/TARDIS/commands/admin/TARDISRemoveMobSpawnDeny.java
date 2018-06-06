/*
 * Copyright (C) 2018 eccentric_nz
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
package me.eccentric_nz.TARDIS.commands.admin;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

import java.util.Map;

/**
 * @author eccentric_nz
 */
public class TARDISRemoveMobSpawnDeny {

    private final TARDIS plugin;
    private WorldGuardPlugin wg;

    public TARDISRemoveMobSpawnDeny(TARDIS plugin) {
        this.plugin = plugin;
        if (plugin.isWorldGuardOnServer()) {
            wg = (WorldGuardPlugin) plugin.getPM().getPlugin("WorldGuard");
        }
    }

    public boolean doAllowMobSpawning(CommandSender sender) {
        if (!plugin.isWorldGuardOnServer()) {
            sender.sendMessage(plugin.getPluginName() + "WorldGuard is not enabled on this server!");
            return true;
        }
        if (!plugin.getConfig().getBoolean("creation.default_world")) {
            sender.sendMessage(plugin.getPluginName() + "This command only works if TARDISes are created in a default world!");
            return true;
        }
        // get default world name
        String world = plugin.getConfig().getString("creation.default_world_name");
        World vortex = plugin.getServer().getWorld(world);
        // get the regions for this world
        Map<String, ProtectedRegion> regions = wg.getRegionManager(vortex).getRegions();
        regions.entrySet().forEach((map) -> {
            if (map.getKey().startsWith("tardis_")) {
                plugin.getServer().dispatchCommand(plugin.getConsole(), "rg flag " + map.getKey() + " mob-spawning -w " + world);
            }
        });
        return true;
    }
}
