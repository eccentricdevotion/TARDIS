/*
 * Copyright (C) 2014 eccentric_nz
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
import com.sk89q.worldguard.bukkit.listener.SpongeUtil;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.managers.storage.StorageException;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.builders.TARDISTIPSData;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

/**
 * Before a TARDIS becomes fully functional, it must be primed with the
 * biological imprint of a Time Lord, normally done by simply having a Time Lord
 * operate the TARDIS for the first time.
 *
 * @author eccentric_nz
 */
public class TARDISWorldGuardUtils {

    private final TARDIS plugin;
    private WorldGuardPlugin wg;

    /**
     * Checks if WorldGuard is on the server.
     *
     * @param plugin an instance of the TARDIS plugin
     */
    public TARDISWorldGuardUtils(TARDIS plugin) {
        this.plugin = plugin;
        if (plugin.isWorldGuardOnServer()) {
            wg = (WorldGuardPlugin) plugin.getPM().getPlugin("WorldGuard");
        }
    }

    /**
     * Checks if a player can build (a Police Box) at this location.
     *
     * @param p the player to check
     * @param l the location to check
     * @return true of false depending on whether the player has permission to
     * build at this location
     */
    public boolean canBuild(Player p, Location l) {
        return wg.canBuild(p, l);
    }

    /**
     * Checks if a player can land (a Police Box) at this location.
     *
     * @param p the player to check
     * @param l the location to check
     * @return true of false depending on whether the player has permission to
     * land at this location
     */
    public boolean canLand(Player p, Location l) {
        // get the flag we should be checking
        String f = plugin.getConfig().getString("preferences.respect_worldguard");
        if (f.toLowerCase().equals("none")) {
            return true;
        }
        // WorldGuard will throw an IllegalArgumentException if the build flag is given to allows()
        if (f.toLowerCase().equals("build")) {
            return wg.canBuild(p, l);
        }
        // get the flag to check
        StateFlag flag = TARDISWorldGuardFlag.getFLAG_LOOKUP().get(f.toLowerCase());
        if (flag == null) {
            return true;
        }
        // get the regions for this location
        ApplicableRegionSet rs = wg.getRegionManager(l.getWorld()).getApplicableRegions(l);
        return rs.testState(null, flag);
    }

    /**
     * Adds a WorldGuard protected region to the inner TARDIS location. This
     * stops other players and mobs from griefing the TARDIS :)
     *
     * @param p the player to assign as the owner of the region
     * @param one a start location of a cuboid region
     * @param two an end location of a cuboid region
     */
    public void addWGProtection(Player p, Location one, Location two) {
        RegionManager rm = wg.getRegionManager(one.getWorld());
        BlockVector b1;
        BlockVector b2;
        int cube = plugin.getConfig().getInt("creation.border_radius") * 16;
        if (plugin.getConfig().getBoolean("creation.create_worlds")) {
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
        flags.put(DefaultFlag.ENDER_BUILD, State.DENY);
        flags.put(DefaultFlag.FIRE_SPREAD, State.DENY);
        flags.put(DefaultFlag.LAVA_FIRE, State.DENY);
        flags.put(DefaultFlag.LAVA_FLOW, State.DENY);
        flags.put(DefaultFlag.LIGHTER, State.DENY);
        flags.put(DefaultFlag.CHEST_ACCESS, State.ALLOW);
        region.setFlags(flags);
        rm.addRegion(region);
        try {
            rm.save();
        } catch (StorageException e) {
            plugin.getConsole().sendMessage(plugin.getPluginName() + "Could not create WorldGuard Protection for TARDIS! " + e.getMessage());
        }
    }

    /**
     * Adds a WorldGuard protected region for a TIPS slot. This stops other
     * players and mobs from griefing the TARDIS :)
     *
     * @param p the player to assign as the owner of the region
     * @param data a TIPS Data container
     * @param w the world we are creating the region in
     */
    public void addWGProtection(String p, TARDISTIPSData data, World w) {
        RegionManager rm = wg.getRegionManager(w);
        BlockVector b1 = new BlockVector(data.getMinX(), 0, data.getMinZ());
        BlockVector b2 = new BlockVector(data.getMaxX(), 256, data.getMaxZ());
        String region_id = "tardis_" + p;
        ProtectedCuboidRegion region = new ProtectedCuboidRegion(region_id, b1, b2);
        DefaultDomain dd = new DefaultDomain();
        dd.addPlayer(p);
        region.setOwners(dd);
        HashMap<Flag<?>, Object> flags = new HashMap<Flag<?>, Object>();
        if (!p.equals("junk")) {
            flags.put(DefaultFlag.ENTRY, State.DENY);
        } else {
            flags.put(DefaultFlag.BUILD, State.DENY);
        }
        flags.put(DefaultFlag.TNT, State.DENY);
        flags.put(DefaultFlag.FIRE_SPREAD, State.DENY);
        flags.put(DefaultFlag.LAVA_FIRE, State.DENY);
        flags.put(DefaultFlag.LAVA_FLOW, State.DENY);
        flags.put(DefaultFlag.LIGHTER, State.DENY);
        flags.put(DefaultFlag.CHEST_ACCESS, State.ALLOW);
        region.setFlags(flags);
        rm.addRegion(region);
        if (!p.equals("junk")) {
            // deny access to anyone but the owner - companions will be added as the player defines them
            // usage = "<id> <flag> [-w world] [-g group] [value]",
            plugin.getServer().dispatchCommand(plugin.getConsole(), "rg flag " + region_id + " entry -w " + w.getName() + " -g nonmembers deny");
        }
        try {
            rm.save();
        } catch (StorageException e) {
            plugin.getConsole().sendMessage(plugin.getPluginName() + "Could not create WorldGuard Protection for TARDIS! " + e.getMessage());
        }
    }

    /**
     * Adds a WorldGuard protected region to recharger location. A 3x3 block
     * area is protected.
     *
     * @param p the player to assign as the owner of the region
     * @param name the name of the recharger
     * @param one a start location of a cuboid region
     * @param two an end location of a cuboid region
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
        region.setFlags(flags);
        rm.addRegion(region);
        try {
            rm.save();
        } catch (StorageException e) {
            plugin.getConsole().sendMessage(plugin.getPluginName() + "Could not create WorldGuard Protection for recharger! " + e.getMessage());
        }
    }

    /**
     * Adds a WorldGuard protected region to exterior renderer room.
     *
     * @param name the name of the recharger
     * @param one a start location of a cuboid region
     * @param two an end location of a cuboid region
     */
    public void addRendererProtection(String name, Location one, Location two) {
        RegionManager rm = wg.getRegionManager(one.getWorld());
        BlockVector b1;
        BlockVector b2;
        b1 = makeBlockVector(one);
        b2 = makeBlockVector(two);
        ProtectedCuboidRegion region = new ProtectedCuboidRegion("renderer_" + name, b1, b2);
        HashMap<Flag<?>, Object> flags = new HashMap<Flag<?>, Object>();
        flags.put(DefaultFlag.TNT, State.DENY);
        flags.put(DefaultFlag.CREEPER_EXPLOSION, State.DENY);
        flags.put(DefaultFlag.FIRE_SPREAD, State.DENY);
        flags.put(DefaultFlag.LAVA_FIRE, State.DENY);
        flags.put(DefaultFlag.LAVA_FLOW, State.DENY);
        flags.put(DefaultFlag.LIGHTER, State.DENY);
        flags.put(DefaultFlag.LEAF_DECAY, State.DENY);
        region.setFlags(flags);
        rm.addRegion(region);
        try {
            rm.save();
        } catch (StorageException e) {
            plugin.getConsole().sendMessage(plugin.getPluginName() + "Could not create WorldGuard Protection for exterior renderering room! " + e.getMessage());
        }
    }

    /**
     * Removes the WorldGuard region when the TARDIS is deleted.
     *
     * @param w the world the region is located in
     * @param p the player's name
     */
    public void removeRegion(World w, String p) {
        RegionManager rm = wg.getRegionManager(w);
        rm.removeRegion("tardis_" + p);
        try {
            rm.save();
        } catch (StorageException e) {
            plugin.getConsole().sendMessage(plugin.getPluginName() + "Could not remove WorldGuard Protection for TARDIS! " + e.getMessage());
        }
    }

    /**
     * Removes the WorldGuard region when the recharger is removed.
     *
     * @param name the name of the recharger to remove
     */
    public void removeRechargerRegion(String name) {
        World w = plugin.getServer().getWorld(plugin.getConfig().getString("rechargers." + name + ".world"));
        RegionManager rm = wg.getRegionManager(w);
        rm.removeRegion("tardis_recharger_" + name);
        try {
            rm.save();
        } catch (StorageException e) {
            plugin.getConsole().sendMessage(plugin.getPluginName() + "Could not remove recharger WorldGuard Protection for recharger! " + e.getMessage());
        }
    }

    /**
     * Removes the exterior rendering room region when the room is jettisoned or
     * the TARDIS is deleted.
     *
     * @param w the world the region is located in
     * @param p the player's name
     * @param r the room region to remove
     */
    public void removeRoomRegion(World w, String p, String r) {
        RegionManager rm = wg.getRegionManager(w);
        if (rm.hasRegion(r + "_" + p)) {
            rm.removeRegion(r + "_" + p);
            try {
                rm.save();
            } catch (StorageException e) {
                plugin.getConsole().sendMessage(plugin.getPluginName() + "Could not remove WorldGuard Protection for " + r + " room! " + e.getMessage());
            }
        }
    }

    /**
     * Adds a player to a region's membership.
     *
     * @param w the world the region is located in
     * @param owner the player whose region it is
     * @param a the player to add
     */
    public void addMemberToRegion(World w, String owner, String a) {
        RegionManager rm = wg.getRegionManager(w);
        if (rm.hasRegion("tardis_" + owner)) {
            plugin.getServer().dispatchCommand(plugin.getConsole(), "rg addmember tardis_" + owner + " " + a + " -w " + w.getName());
        }
    }

    /**
     * Removes a player from a region's membership.
     *
     * @param w the world the region is located in
     * @param owner the player whose region it is
     * @param a the player to add
     */
    public void removeMemberFromRegion(World w, String owner, String a) {
        RegionManager rm = wg.getRegionManager(w);
        if (rm.hasRegion("tardis_" + owner)) {
            plugin.getServer().dispatchCommand(plugin.getConsole(), "rg removemember tardis_" + owner + " " + a + " -w " + w.getName());
        }
    }

    /**
     * Updates the TARDIS WorldGuard region when the player name has changed.
     *
     * @param w the world the region is located in
     * @param o the owner's name
     * @param uuid the UUID of the player
     * @param which the region type to update
     */
    public void updateRegionForNameChange(World w, String o, UUID uuid, String which) {
        RegionManager rm = wg.getRegionManager(w);
        String region = which + "_" + o;
        if (rm.hasRegion(region)) {
            ProtectedRegion pr = rm.getRegion(region);
            DefaultDomain dd = pr.getOwners();
            dd.addPlayer(uuid);
            pr.setOwners(dd);
            try {
                rm.save();
            } catch (StorageException e) {
                plugin.getConsole().sendMessage(plugin.getPluginName() + "Could not update WorldGuard Protection for TARDIS owner name change! " + e.getMessage());
            }
        }
    }

    /**
     * Turns a location object into a BlockVector.
     *
     * @param location the Location to convert to BlockVector
     * @return a BlockVector
     */
    public BlockVector makeBlockVector(Location location) {
        return new BlockVector(location.getX(), location.getY(), location.getZ());
    }

    /**
     * Sets a block with the properties of a sponge
     *
     * @param b the sponge block
     * @param clear whether to set the sponge area or remove it
     */
    public void sponge(Block b, boolean clear) {
        if (clear) {
            // remove water
            SpongeUtil.clearSpongeWater(wg, b.getWorld(), b.getX(), b.getY(), b.getZ());
        } else {
            // put water back
            SpongeUtil.addSpongeWater(wg, b.getWorld(), b.getX(), b.getY(), b.getZ());
        }
    }

    /**
     * Checks whether a block can be broken
     *
     * @param p the player trying to break the block
     * @param b the block
     * @return whether the block can be broken
     */
    public boolean canBreakBlock(Player p, Block b) {
        return wg.canBuild(p, b);
    }

    /**
     * Gets a TARDIS WorldGuard region.
     *
     * @param world the world the region is in
     * @param p the Time Lord whose region it is
     * @return the protected region
     */
    public ProtectedRegion getRegion(String world, String p) {
        World w = plugin.getServer().getWorld(world);
        if (w == null) {
            return null;
        }
        RegionManager rm = wg.getRegionManager(w);
        return rm.getRegion("tardis_" + p);
    }

    /**
     * Gets a List of all TARDIS regions in a world.
     *
     * @param w the world to remove the flags from
     * @return a list of TARDIS region names for this world
     */
    public List<String> getRegions(World w) {
        List<String> regions = new ArrayList<String>();
        RegionManager rm = wg.getRegionManager(w);
        for (Map.Entry<String, ProtectedRegion> pr : rm.getRegions().entrySet()) {
            if (pr.getKey().contains("tardis") && pr.getValue().getFlags().containsKey(DefaultFlag.BUILD)) {
                regions.add(pr.getKey());
            }
        }
        return regions;
    }

    /**
     * Checks whether there is a protected region at a location and if so
     * whether mobs can spawn.
     *
     * @param l the location to check
     * @return true if mobs can spawn, otherwise false
     */
    public boolean mobsCanSpawnAtLocation(Location l) {
        RegionManager rm = wg.getRegionManager(l.getWorld());
        ApplicableRegionSet ars = rm.getApplicableRegions(l);
        return ars.testState(null, DefaultFlag.MOB_SPAWNING);
    }
}
