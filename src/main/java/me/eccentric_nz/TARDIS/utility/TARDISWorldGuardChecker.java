/*
 * Copyright (C) 2013 eccentric_nz
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

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.databases.ProtectionDatabaseException;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.RegionGroup;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

/**
 * Before a TARDIS becomes fully functional, it must be primed with the
 * biological imprint of a Time Lord, normally done by simply having a Time Lord
 * operate the TARDIS for the first time.
 *
 * @author eccentric_nz
 */
public class TARDISWorldGuardChecker {

    private TARDIS plugin;
    private WorldGuardPlugin wg;

    public TARDISWorldGuardChecker(TARDIS plugin) {
        this.plugin = plugin;
        if (plugin.worldGuardOnServer) {
            wg = (WorldGuardPlugin) plugin.getServer().getPluginManager().getPlugin("WorldGuard");
        }
    }

    /**
     * Checks if a player can build (a Police Box) at this location.
     *
     * @param p the player to check
     * @param l the location to check
     */
    public boolean cantBuild(Player p, Location l) {
        return (plugin.worldGuardOnServer) && (!wg.canBuild(p, l));
    }

    /**
     * Adds a WorldGuard protected region to the inner TARDIS location. This
     * stops other players and mobs from griefing the TARDIS :)
     */
    public void addWGProtection(Player p, Location one, Location two) {
        RegionManager rm = wg.getRegionManager(one.getWorld());
        BlockVector b1;
        BlockVector b2;
        int cube = plugin.getConfig().getInt("border_radius") * 16;
        if (plugin.getConfig().getBoolean("create_worlds")) {
            // make a big cuboid region
            b1 = new BlockVector(cube, 256, cube);
            b2 = new BlockVector(-cube, 0, -cube);
        } else {
            // just get the TARDIS size
            b1 = makeBlockVector(one);
            b2 = makeBlockVector(two);
        }
        ProtectedCuboidRegion region = new ProtectedCuboidRegion("tardis_" + p.getName(), b1, b2);
        DefaultDomain dd = new DefaultDomain();
        dd.addPlayer(p.getName());
        region.setOwners(dd);
        HashMap<Flag<?>, Object> flags = new HashMap<Flag<?>, Object>();
        flags.put(DefaultFlag.TNT, State.DENY);
        flags.put(DefaultFlag.CREEPER_EXPLOSION, State.DENY);
        flags.put(DefaultFlag.FIRE_SPREAD, State.DENY);
        flags.put(DefaultFlag.LAVA_FIRE, State.DENY);
        flags.put(DefaultFlag.LAVA_FLOW, State.DENY);
        flags.put(DefaultFlag.LIGHTER, State.DENY);
        flags.put(DefaultFlag.MOB_SPAWNING, State.DENY);
        flags.put(DefaultFlag.CONSTRUCT, RegionGroup.OWNERS);
        flags.put(DefaultFlag.CHEST_ACCESS, State.ALLOW);
        region.setFlags(flags);
        rm.addRegion(region);
        try {
            rm.save();
        } catch (ProtectionDatabaseException e) {
            plugin.console.sendMessage(plugin.pluginName + "could not create WorldGuard Protection for TARDIS! " + e);
        }
    }

    /**
     * Adds a WorldGuard protected region to recharger location. A 3x3 block
     * area is protected.
     */
    public void addRechargerProtection(Player p, String name, Location one, Location two) {
        RegionManager rm = wg.getRegionManager(one.getWorld());
        BlockVector b1;
        BlockVector b2;
        b1 = makeBlockVector(one);
        b2 = makeBlockVector(two);
        ProtectedCuboidRegion region = new ProtectedCuboidRegion("tardis_recharger_" + name, b1, b2);
        DefaultDomain dd = new DefaultDomain();
        dd.addPlayer(p.getName());
        region.setOwners(dd);
        HashMap<Flag<?>, Object> flags = new HashMap<Flag<?>, Object>();
        flags.put(DefaultFlag.TNT, State.DENY);
        flags.put(DefaultFlag.CREEPER_EXPLOSION, State.DENY);
        flags.put(DefaultFlag.FIRE_SPREAD, State.DENY);
        flags.put(DefaultFlag.LAVA_FIRE, State.DENY);
        flags.put(DefaultFlag.LAVA_FLOW, State.DENY);
        flags.put(DefaultFlag.LIGHTER, State.DENY);
        flags.put(DefaultFlag.CONSTRUCT, RegionGroup.OWNERS);
        region.setFlags(flags);
        rm.addRegion(region);
        try {
            rm.save();
        } catch (ProtectionDatabaseException e) {
            plugin.console.sendMessage(plugin.pluginName + "could not create WorldGuard Protection for recharger! " + e);
        }
    }

    /**
     * Removes the WorldGuard region when the TARDIS is deleted.
     */
    public void removeRegion(World w, String p) {
        RegionManager rm = wg.getRegionManager(w);
        rm.removeRegion("tardis_" + p);
        try {
            rm.save();
        } catch (ProtectionDatabaseException e) {
            plugin.console.sendMessage(plugin.pluginName + "could not remove WorldGuard Protection! " + e);
        }
    }

    /**
     * Removes the WorldGuard region when the recharger is removed.
     */
    public void removeRechargerRegion(String name) {
        World w = plugin.getServer().getWorld(plugin.getConfig().getString("rechargers." + name + ".world"));
        RegionManager rm = wg.getRegionManager(w);
        rm.removeRegion("tardis_recharger_" + name);
        try {
            rm.save();
        } catch (ProtectionDatabaseException e) {
            plugin.console.sendMessage(plugin.pluginName + "could not remove recharger WorldGuard Protection! " + e);
        }
    }

    /**
     * Turns a location object into a vector.
     */
    public BlockVector makeBlockVector(Location location) {
        return new BlockVector(location.getX(), location.getY(), location.getZ());
    }
}
