/*
 * Copyright (C) 2023 eccentric_nz
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

import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.internal.platform.WorldGuardPlatform;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.RegionGroup;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.managers.storage.StorageException;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.builders.TARDISTIPSData;
import me.eccentric_nz.TARDIS.floodgate.TARDISFloodgate;
import me.eccentric_nz.TARDIS.planets.TARDISAliasResolver;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.logging.Level;

/**
 * Before a TARDIS becomes fully functional, it must be primed with the biological imprint of a Time Lord, normally done
 * by simply having a Time Lord operate the TARDIS for the first time.
 *
 * @author eccentric_nz
 */
public class TARDISWorldGuardUtils {

    private final TARDIS plugin;
    private WorldGuardPlugin wgp;
    private WorldGuardPlatform wg;

    /**
     * Checks if WorldGuard is on the server.
     *
     * @param plugin an instance of the TARDIS plugin
     */
    public TARDISWorldGuardUtils(TARDIS plugin) {
        this.plugin = plugin;
        if (plugin.isWorldGuardOnServer()) {
            wgp = (WorldGuardPlugin) plugin.getPM().getPlugin("WorldGuard");
            wg = WorldGuard.getInstance().getPlatform();
        }
    }

    /**
     * Checks if a player can build (a Police Box) at this location.
     *
     * @param p the player to check
     * @param l the location to check
     * @return true of false depending on whether the player has permission to build at this location
     */
    public boolean canBuild(Player p, Location l) {
        BlockVector3 vector = BlockVector3.at(l.getX(), l.getY(), l.getZ());
        RegionManager rm = wg.getRegionContainer().get(new BukkitWorld(l.getWorld()));
        ApplicableRegionSet rs = rm.getApplicableRegions(vector);
        return rs.testState(wgp.wrapPlayer(p), Flags.BUILD);
    }

    /**
     * Checks if a player can land (a Police Box) at this location.
     *
     * @param p the player to check
     * @param l the location to check
     * @return true of false depending on whether the player has permission to land at this location
     */
    public boolean canLand(Player p, Location l) {
        // get the flag we should be checking
        String f = plugin.getConfig().getString("preferences.respect_worldguard");
        if (f.toLowerCase(Locale.ENGLISH).equals("none")) {
            return true;
        }
        // WorldGuard will throw an IllegalArgumentException if the build flag is given to allows()
        if (f.toLowerCase(Locale.ENGLISH).equals("build")) {
            BlockVector3 vector = BlockVector3.at(l.getX(), l.getY(), l.getZ());
            RegionManager rm = wg.getRegionContainer().get(new BukkitWorld(l.getWorld()));
            ApplicableRegionSet rs = rm.getApplicableRegions(vector);
            return rs.testState(wgp.wrapPlayer(p), Flags.BUILD);
        }
        // get the flag to check
        StateFlag flag = TARDISWorldGuardFlag.getFLAG_LOOKUP().get(f.toLowerCase(Locale.ENGLISH));
        if (flag == null) {
            return true;
        }
        // get the regions for this location
        BlockVector3 vector = BlockVector3.at(l.getX(), l.getY(), l.getZ());
        RegionManager rm = wg.getRegionContainer().get(new BukkitWorld(l.getWorld()));
        ApplicableRegionSet rs = rm.getApplicableRegions(vector);
        return rs.testState(null, flag);
    }

    /**
     * Adds a WorldGuard protected region to the inner TARDIS location. This stops other players and mobs from griefing
     * the TARDIS :)
     *
     * @param p   the player to assign as the owner of the region
     * @param one a start location of a cuboid region
     * @param two an end location of a cuboid region
     */
    public void addWGProtection(Player p, Location one, Location two) {
        RegionManager rm = wg.getRegionContainer().get(new BukkitWorld(one.getWorld()));
        BlockVector3 b1;
        BlockVector3 b2;
        int cube = plugin.getConfig().getInt("creation.border_radius") * 16;
        if (plugin.getConfig().getBoolean("creation.create_worlds")) {
            // make a big cuboid region
            b1 = BlockVector3.at(cube, 256, cube);
            b2 = BlockVector3.at(-cube, 0, -cube);
        } else {
            // just get the TARDIS size
            b1 = makeBlockVector(one);
            b2 = makeBlockVector(two);
        }
        // check floodgate
        String name = (TARDISFloodgate.isFloodgateEnabled() && TARDISFloodgate.isBedrockPlayer(p.getUniqueId())) ? TARDISFloodgate.sanitisePlayerName(p.getName()) : p.getName();
        ProtectedCuboidRegion region = new ProtectedCuboidRegion("TARDIS_" + name, b1, b2);
        DefaultDomain dd = new DefaultDomain();
        dd.addPlayer(p.getName());
        region.setOwners(dd);
        HashMap<Flag<?>, Object> flags = new HashMap<>();
        //flags.put(Flags.TNT, State.DENY);
        flags.put(Flags.ENDER_BUILD, State.DENY);
        flags.put(Flags.FIRE_SPREAD, State.DENY);
        flags.put(Flags.LAVA_FIRE, State.DENY);
        flags.put(Flags.LAVA_FLOW, State.DENY);
        flags.put(Flags.LIGHTER, State.DENY);
        flags.put(Flags.USE, State.ALLOW);
        region.setFlags(flags);
        rm.addRegion(region);
        try {
            rm.save();
        } catch (StorageException e) {
            plugin.getLogger().log(Level.INFO, "Could not create WorldGuard Protection for TARDIS! " + e.getMessage());
        }
    }

    /**
     * Adds a WorldGuard protected region for a TIPS slot. This stops other players and mobs from griefing the TARDIS
     * :)
     *
     * @param player the player to assign as the owner of the region
     * @param data   a TIPS Data container
     * @param world  the world we are creating the region in
     */
    public void addWGProtection(Player player, TARDISTIPSData data, World world, boolean junk) {
        RegionManager rm = wg.getRegionContainer().get(new BukkitWorld(world));
        BlockVector3 b1 = BlockVector3.at(data.getMinX(), 0, data.getMinZ());
        BlockVector3 b2 = BlockVector3.at(data.getMaxX(), 256, data.getMaxZ());
        UUID uuid;
        String name;
        if (junk) {
            uuid = UUID.fromString("00000000-aaaa-bbbb-cccc-000000000000");
            name = "junk";
        } else {
            uuid = player.getUniqueId();
            // check floodgate for region name
            name = (TARDISFloodgate.isFloodgateEnabled() && TARDISFloodgate.isBedrockPlayer(player.getUniqueId())) ? TARDISFloodgate.sanitisePlayerName(player.getName()) : player.getName();
        }
        String region_id = "TARDIS_" + name;
        ProtectedCuboidRegion region = new ProtectedCuboidRegion(region_id, b1, b2);
        DefaultDomain dd = new DefaultDomain();
        dd.addPlayer(uuid);
        region.setOwners(dd);
        HashMap<Flag<?>, Object> flags = new HashMap<>();
        if (!junk && !plugin.getConfig().getBoolean("preferences.open_door_policy")) {
            flags.put(Flags.ENTRY, State.DENY);
        }
        flags.put(Flags.FIRE_SPREAD, State.DENY);
        flags.put(Flags.LAVA_FIRE, State.DENY);
        flags.put(Flags.LAVA_FLOW, State.DENY);
        flags.put(Flags.LIGHTER, State.DENY);
        flags.put(Flags.USE, State.ALLOW);
        region.setFlags(flags);
        rm.addRegion(region);
        if (!junk) {
            // deny exit to all
            // usage = "<id> <flag> [-w world] [-g group] [value]",
            plugin.getServer().dispatchCommand(plugin.getConsole(), "rg flag " + region_id + " exit -w " + world.getName() + " deny");
        }
        try {
            rm.save();
        } catch (StorageException e) {
            plugin.getLogger().log(Level.INFO, "Could not create WorldGuard Protection for TARDIS! " + e.getMessage());
        }
    }

    /**
     * Adds a WorldGuard protected region for a TIPS slot. This stops other players and mobs from griefing the TARDIS
     * :)
     *
     * @param name  the player to assign as the owner of the region
     * @param data  a TIPS Data container
     * @param world the world we are creating the region in
     */
    public void addWGProtection(UUID uuid, String name, TARDISTIPSData data, World world) {
        RegionManager rm = wg.getRegionContainer().get(new BukkitWorld(world));
        BlockVector3 b1 = BlockVector3.at(data.getMinX(), 0, data.getMinZ());
        BlockVector3 b2 = BlockVector3.at(data.getMaxX(), 256, data.getMaxZ());
        String region_id = "TARDIS_" + name;
        ProtectedCuboidRegion region = new ProtectedCuboidRegion(region_id, b1, b2);
        DefaultDomain dd = new DefaultDomain();
        dd.addPlayer(uuid);
        region.setOwners(dd);
        HashMap<Flag<?>, Object> flags = new HashMap<>();
        if (name.length() != 36 && !plugin.getConfig().getBoolean("preferences.open_door_policy")) {
            flags.put(Flags.ENTRY, State.DENY);
        }
        flags.put(Flags.FIRE_SPREAD, State.DENY);
        flags.put(Flags.LAVA_FIRE, State.DENY);
        flags.put(Flags.LAVA_FLOW, State.DENY);
        flags.put(Flags.LIGHTER, State.DENY);
        flags.put(Flags.USE, State.ALLOW);
        region.setFlags(flags);
        rm.addRegion(region);
        // deny exit to all
        // usage = "<id> <flag> [-w world] [-g group] [value]",
        plugin.getServer().dispatchCommand(plugin.getConsole(), "rg flag " + region_id + " exit -w " + world.getName() + " deny");
        try {
            rm.save();
        } catch (StorageException e) {
            plugin.getLogger().log(Level.INFO, "Could not create WorldGuard Protection for TARDIS! " + e.getMessage());
        }
    }

    /**
     * Adds a WorldGuard protected region to recharger location. A 3x3 block area is protected.
     *
     * @param p    the player to assign as the owner of the region
     * @param name the name of the recharger
     * @param one  a start location of a cuboid region
     * @param two  an end location of a cuboid region
     */
    public void addRechargerProtection(Player p, String name, Location one, Location two) {
        RegionManager rm = wg.getRegionContainer().get(new BukkitWorld(one.getWorld()));
        BlockVector3 b1;
        BlockVector3 b2;
        b1 = makeBlockVector(one);
        b2 = makeBlockVector(two);
        ProtectedCuboidRegion region = new ProtectedCuboidRegion("tardis_recharger_" + name, b1, b2);
        DefaultDomain dd = new DefaultDomain();
        dd.addPlayer(p.getUniqueId());
        region.setOwners(dd);
        HashMap<Flag<?>, Object> flags = new HashMap<>();
        flags.put(Flags.TNT, State.DENY);
        flags.put(Flags.CREEPER_EXPLOSION, State.DENY);
        flags.put(Flags.FIRE_SPREAD, State.DENY);
        flags.put(Flags.LAVA_FIRE, State.DENY);
        flags.put(Flags.LAVA_FLOW, State.DENY);
        flags.put(Flags.LIGHTER, State.DENY);
        region.setFlags(flags);
        rm.addRegion(region);
        try {
            rm.save();
        } catch (StorageException e) {
            plugin.getLogger().log(Level.INFO, "Could not create WorldGuard Protection for recharger! " + e.getMessage());
        }
    }

    /**
     * Adds a WorldGuard protected region to exterior renderer room.
     *
     * @param name the name of the player growing the render room
     * @param one  a start location of a cuboid region
     * @param two  an end location of a cuboid region
     */
    public void addRendererProtection(String name, Location one, Location two) {
        RegionManager rm = wg.getRegionContainer().get(new BukkitWorld(one.getWorld()));
        BlockVector3 b1;
        BlockVector3 b2;
        b1 = makeBlockVector(one);
        b2 = makeBlockVector(two);
        if (TARDISFloodgate.isFloodgateEnabled()) {
            name = TARDISFloodgate.sanitisePlayerName(name);
        }
        ProtectedCuboidRegion region = new ProtectedCuboidRegion("renderer_" + name, b1, b2);
        HashMap<Flag<?>, Object> flags = new HashMap<>();
        flags.put(Flags.TNT, State.DENY);
        flags.put(Flags.CREEPER_EXPLOSION, State.DENY);
        flags.put(Flags.FIRE_SPREAD, State.DENY);
        flags.put(Flags.LAVA_FIRE, State.DENY);
        flags.put(Flags.LAVA_FLOW, State.DENY);
        flags.put(Flags.LIGHTER, State.DENY);
        flags.put(Flags.LEAF_DECAY, State.DENY);
        region.setFlags(flags);
        rm.addRegion(region);
        try {
            rm.save();
        } catch (StorageException e) {
            plugin.getLogger().log(Level.INFO, "Could not create WorldGuard Protection for exterior renderering room! " + e.getMessage());
        }
    }

    /**
     * Removes the WorldGuard region when the TARDIS is deleted.
     *
     * @param world the world the region is located in
     * @param name  the player's name
     */
    public void removeRegion(World world, String name) {
        RegionManager rm = wg.getRegionContainer().get(new BukkitWorld(world));
        Set<ProtectedRegion> regions = rm.removeRegion("TARDIS_" + name);
        if (regions.size() == 0 && TARDISFloodgate.isFloodgateEnabled()) {
            // try sanitised name
            rm.removeRegion("TARDIS_" + TARDISFloodgate.sanitisePlayerName(name));
        }
        try {
            rm.save();
        } catch (StorageException e) {
            plugin.getLogger().log(Level.INFO, "Could not remove WorldGuard Protection for TARDIS! " + e.getMessage());
        }
    }

    /**
     * Removes the WorldGuard region when the TARDIS is deleted.
     *
     * @param l the TARDIS interior location
     */
    public void removeRegion(Location l) {
        RegionManager rm = wg.getRegionContainer().get(new BukkitWorld(l.getWorld()));
        BlockVector3 vector = BlockVector3.at(l.getX(), l.getY(), l.getZ());
        ApplicableRegionSet ars = rm.getApplicableRegions(vector);
        if (ars.size() > 0) {
            LinkedList<String> parentNames = new LinkedList<>();
            LinkedList<String> regions = new LinkedList<>();
            for (ProtectedRegion pr : ars) {
                String id = pr.getId();
                regions.add(id);
                ProtectedRegion parent = pr.getParent();
                while (parent != null) {
                    parentNames.add(parent.getId());
                    parent = parent.getParent();
                }
            }
            parentNames.forEach(regions::remove);
            rm.removeRegion(regions.getFirst());
            try {
                rm.save();
            } catch (StorageException e) {
                plugin.getLogger().log(Level.INFO, "Could not remove WorldGuard Protection for TARDIS! " + e.getMessage());
            }
        } else {
            plugin.getLogger().log(Level.INFO, "Could not get WorldGuard region for TARDIS location!");
        }
    }

    /**
     * Removes the WorldGuard region when the recharger is removed.
     *
     * @param name the name of the recharger to remove
     */
    public void removeRechargerRegion(String name) {
        World w = TARDISAliasResolver.getWorldFromAlias(plugin.getConfig().getString("rechargers." + name + ".world"));
        RegionManager rm = wg.getRegionContainer().get(new BukkitWorld(w));
        rm.removeRegion("tardis_recharger_" + name);
        try {
            rm.save();
        } catch (StorageException e) {
            plugin.getLogger().log(Level.INFO, "Could not remove recharger WorldGuard Protection for recharger! " + e.getMessage());
        }
    }

    /**
     * Removes the exterior rendering room region when the room is jettisoned or the TARDIS is deleted.
     *
     * @param world the world the region is located in
     * @param name  the player's name
     * @param room  the room region to remove
     */
    public void removeRoomRegion(World world, String name, String room) {
        boolean save = false;
        RegionManager rm = wg.getRegionContainer().get(new BukkitWorld(world));
        if (rm.hasRegion(room + "_" + name)) {
            rm.removeRegion(room + "_" + name);
            save = true;
        } else if (TARDISFloodgate.isFloodgateEnabled()) {
            String sanitised = TARDISFloodgate.sanitisePlayerName(name);
            if (rm.hasRegion(room + "_" + sanitised)) {
                rm.removeRegion(room + "_" + sanitised);
                save = true;
            }
        }
        if (save) {
            try {
                rm.save();
            } catch (StorageException e) {
                plugin.getLogger().log(Level.INFO, "Could not remove WorldGuard Protection for " + room + " room! " + e.getMessage());
            }
        }
    }

    /**
     * Adds a player to a region's membership.
     *
     * @param w     the world the region is located in
     * @param owner the player whose region it is
     * @param uuid  the UUID of the player to add
     */
    public void addMemberToRegion(World w, String owner, UUID uuid) {
        RegionManager rm = wg.getRegionContainer().get(new BukkitWorld(w));
        ProtectedRegion protectedRegion = null;
        if (rm.hasRegion("TARDIS_" + owner)) {
            protectedRegion = rm.getRegion("TARDIS_" + owner);
        } else if (TARDISFloodgate.isFloodgateEnabled()) {
            String sanitised = TARDISFloodgate.sanitisePlayerName(owner);
            if (rm.hasRegion("TARDIS_" + sanitised)) {
                protectedRegion = rm.getRegion("TARDIS_" + sanitised);
            }
        }
        if (protectedRegion != null) {
            DefaultDomain members = protectedRegion.getMembers();
            members.addPlayer(uuid);
            try {
                rm.save();
            } catch (StorageException e) {
                plugin.getLogger().log(Level.INFO, "Could not update WorldGuard flags for everyone entry & exit! " + e.getMessage());
            }
        }
    }

    /**
     * Removes a player from a region's membership.
     *
     * @param world the world the region is located in
     * @param owner the player whose region it is
     * @param uuid  the UUID of the player to remove
     */
    public void removeMemberFromRegion(World world, String owner, UUID uuid) {
        RegionManager rm = wg.getRegionContainer().get(new BukkitWorld(world));
        ProtectedRegion protectedRegion = null;
        if (rm.hasRegion("TARDIS_" + owner)) {
            protectedRegion = rm.getRegion("TARDIS_" + owner);
        } else if (TARDISFloodgate.isFloodgateEnabled()) {
            String sanitised = TARDISFloodgate.sanitisePlayerName(owner);
            if (rm.hasRegion("TARDIS_" + sanitised)) {
                protectedRegion = rm.getRegion("TARDIS_" + sanitised);
            }
        }
        if (protectedRegion != null) {
            DefaultDomain members = protectedRegion.getMembers();
            members.removePlayer(uuid);
            try {
                rm.save();
            } catch (StorageException e) {
                plugin.getLogger().log(Level.INFO, "Could not update WorldGuard flags for everyone entry & exit! " + e.getMessage());
            }
        }
    }

    /**
     * Removes all members from a region's membership.
     *
     * @param w     the world the region is located in
     * @param owner the player whose region it is
     */
    public void removeAllMembersFromRegion(World w, String owner, UUID uuid) {
        RegionManager rm = wg.getRegionContainer().get(new BukkitWorld(w));
        ProtectedRegion protectedRegion = null;
        if (rm.hasRegion("TARDIS_" + owner)) {
            protectedRegion = rm.getRegion("TARDIS_" + owner);
        } else if (TARDISFloodgate.isFloodgateEnabled()) {
            String sanitised = TARDISFloodgate.sanitisePlayerName(owner);
            if (rm.hasRegion("TARDIS_" + sanitised)) {
                protectedRegion = rm.getRegion("TARDIS_" + sanitised);
            }
        }
        if (protectedRegion != null) {
            DefaultDomain members = protectedRegion.getMembers();
            // remove all
            members.removeAll();
            // add the owner back in
            members.addPlayer(uuid);
            plugin.getServer().dispatchCommand(plugin.getConsole(), "rg addmember TARDIS_" + owner + " " + owner + " -w " + w.getName());
        }
    }

    /**
     * Updates the TARDIS WorldGuard region when the player name has changed.
     *
     * @param world the world the region is located in
     * @param owner the owner's name
     * @param uuid  the UUID of the player
     * @param which the region type to update
     */
    public void updateRegionForNameChange(World world, String owner, UUID uuid, String which) {
        RegionManager rm = wg.getRegionContainer().get(new BukkitWorld(world));
        String region = which + "_" + owner;
        if (rm.hasRegion(region)) {
            ProtectedRegion pr = rm.getRegion(region);
            DefaultDomain dd = pr.getOwners();
            dd.addPlayer(uuid);
            pr.setOwners(dd);
            try {
                rm.save();
            } catch (StorageException e) {
                plugin.getLogger().log(Level.INFO, "Could not update WorldGuard Protection for TARDIS owner name change! " + e.getMessage());
            }
        }
    }

    /**
     * Updates the TARDIS WorldGuard region when the TARDIS has been claimed by a new player.
     *
     * @param location the TARDIS interior location
     * @param uuid     the UUID of the player
     */
    public void updateRegionForClaim(Location location, UUID uuid) {
        RegionManager rm = wg.getRegionContainer().get(new BukkitWorld(location.getWorld()));
        BlockVector3 vector = BlockVector3.at(location.getX(), location.getY(), location.getZ());
        ApplicableRegionSet ars = rm.getApplicableRegions(vector);
        if (ars.size() > 0) {
            LinkedList<String> parentNames = new LinkedList<>();
            LinkedList<String> regions = new LinkedList<>();
            for (ProtectedRegion pr : ars) {
                String id = pr.getId();
                regions.add(id);
                ProtectedRegion parent = pr.getParent();
                while (parent != null) {
                    parentNames.add(parent.getId());
                    parent = parent.getParent();
                }
            }
            parentNames.forEach(regions::remove);
            String region = regions.getFirst();
            ProtectedRegion pr = rm.getRegion(region);
            pr.setFlag(Flags.ENTRY, State.DENY);
            DefaultDomain dd = pr.getOwners();
            dd.addPlayer(uuid);
            pr.setOwners(dd);
            try {
                rm.save();
            } catch (StorageException e) {
                plugin.getLogger().log(Level.INFO, "Could not update WorldGuard Protection for abandoned TARDIS claim! " + e.getMessage());
            }
        }
    }

    /**
     * Sets a player's CHEST_ACCESS flag to DENY in their TARDIS region.
     *
     * @param world the world the region is located in
     * @param owner the player whose region it is
     */
    public void lockContainers(World world, String owner) {
        RegionManager rm = wg.getRegionContainer().get(new BukkitWorld(world));
        ProtectedRegion region = null;
        if (rm.hasRegion("TARDIS_" + owner)) {
            region = rm.getRegion("TARDIS_" + owner);
        } else if (TARDISFloodgate.isFloodgateEnabled()) {
            String sanitised = TARDISFloodgate.sanitisePlayerName(owner);
            if (rm.hasRegion("TARDIS_" + sanitised)) {
                region = rm.getRegion("TARDIS_" + sanitised);
            }
        }
        if (region != null) {
            region.setFlag(Flags.CHEST_ACCESS, State.DENY);
            region.setFlag(Flags.CHEST_ACCESS.getRegionGroupFlag(), RegionGroup.NON_MEMBERS);
            try {
                rm.save();
            } catch (StorageException e) {
                plugin.getLogger().log(Level.INFO, "Could not set WorldGuard CHEST_ACCESS flag to DENY! " + e.getMessage());
            }
        }
    }

    /**
     * Sets a player's CHEST_ACCESS flag to ALLOW in their TARDIS region.
     *
     * @param world the world the region is located in
     * @param owner the player whose region it is
     */
    public void unlockContainers(World world, String owner) {
        RegionManager rm = wg.getRegionContainer().get(new BukkitWorld(world));
        ProtectedRegion region = null;
        if (rm.hasRegion("TARDIS_" + owner)) {
            region = rm.getRegion("TARDIS_" + owner);
        } else if (TARDISFloodgate.isFloodgateEnabled()) {
            String sanitised = TARDISFloodgate.sanitisePlayerName(owner);
            if (rm.hasRegion("TARDIS_" + sanitised)) {
                region = rm.getRegion("TARDIS_" + sanitised);
            }
        }
        if (region != null) {
            region.setFlag(Flags.CHEST_ACCESS, State.ALLOW);
            region.setFlag(Flags.CHEST_ACCESS.getRegionGroupFlag(), RegionGroup.ALL);
            try {
                rm.save();
            } catch (StorageException e) {
                plugin.getLogger().log(Level.INFO, "Could not set WorldGuard CHEST_ACCESS flag to ALLOW! " + e.getMessage());
            }
        }
    }

    /**
     * Gets the state of a player's CHEST_ACCESS flag in their TARDIS region.
     *
     * @param world the world the region is located in
     * @param owner the player whose region it is
     * @return true if containers are accessible
     */
    public boolean queryContainers(World world, String owner) {
        RegionManager rm = wg.getRegionContainer().get(new BukkitWorld(world));
        ProtectedRegion region = null;
        if (rm.hasRegion("TARDIS_" + owner)) {
            region = rm.getRegion("TARDIS_" + owner);
        } else if (TARDISFloodgate.isFloodgateEnabled()) {
            String sanitised = TARDISFloodgate.sanitisePlayerName(owner);
            if (rm.hasRegion("TARDIS_" + sanitised)) {
                region = rm.getRegion("TARDIS_" + sanitised);
            }
        }
        if (region != null) {
            State state = region.getFlag(Flags.CHEST_ACCESS);
            return state == null || state.equals(State.ALLOW);
        }
        return true;
    }

    /**
     * Turns a location object into a BlockVector.
     *
     * @param location the Location to convert to BlockVector
     * @return a BlockVector
     */
    private BlockVector3 makeBlockVector(Location location) {
        return BlockVector3.at(location.getX(), location.getY(), location.getZ());
    }

    /**
     * Gets a TARDIS WorldGuard region.
     *
     * @param world the world the region is in
     * @param name  the Time Lord whose region it is
     * @return the protected region
     */
    public ProtectedRegion getRegion(String world, String name) {
        World w = TARDISAliasResolver.getWorldFromAlias(world);
        if (w == null) {
            return null;
        }
        RegionManager rm = wg.getRegionContainer().get(new BukkitWorld(w));
        ProtectedRegion protectedRegion = null;
        if (rm.hasRegion("TARDIS_" + name)) {
            protectedRegion = rm.getRegion("TARDIS_" + name);
        } else if (TARDISFloodgate.isFloodgateEnabled()) {
            String sanitised = TARDISFloodgate.sanitisePlayerName(name);
            if (rm.hasRegion("TARDIS_" + sanitised)) {
                protectedRegion = rm.getRegion("TARDIS_" + sanitised);
            }
        }
        return protectedRegion;
    }

    /**
     * Gets a List of all TARDIS regions in a world.
     *
     * @param world the world to get the regions for
     * @return a list of TARDIS region names for this world
     */
    public List<String> getTARDISRegions(World world) {
        List<String> regions = new ArrayList<>();
        RegionManager rm = wg.getRegionContainer().get(new BukkitWorld(world));
        rm.getRegions().forEach((key, value) -> {
            if (key.contains("tardis")) {
                regions.add(key);
            }
        });
        return regions;
    }

    /**
     * Checks whether there is a protected region at a location and if so whether mobs can spawn.
     *
     * @param l the location to check
     * @return true if mobs can spawn, otherwise false
     */
    public boolean mobsCanSpawnAtLocation(Location l) {
        RegionManager rm = wg.getRegionContainer().get(new BukkitWorld(l.getWorld()));
        BlockVector3 vector = BlockVector3.at(l.getX(), l.getY(), l.getZ());
        ApplicableRegionSet ars = rm.getApplicableRegions(vector);
        return ars.testState(null, Flags.MOB_SPAWNING);
    }

    /**
     * Sets the entry and exit flags for a region.
     *
     * @param world the world in which the region is located
     * @param owner the player who owns the region
     * @param allow whether the flag state should be set to allow or deny
     */
    public void setEntryExitFlags(String world, String owner, boolean allow) {
        World w = TARDISAliasResolver.getWorldFromAlias(world);
        ProtectedRegion region = null;
        if (w != null) {
            RegionManager rm = wg.getRegionContainer().get(new BukkitWorld(w));
            if (rm.hasRegion("TARDIS_" + owner)) {
                region = rm.getRegion("TARDIS_" + owner);
            } else if (TARDISFloodgate.isFloodgateEnabled()) {
                String sanitised = TARDISFloodgate.sanitisePlayerName(owner);
                if (rm.hasRegion("TARDIS_" + sanitised)) {
                    region = rm.getRegion("TARDIS_" + sanitised);
                }
            }
            // always allow region entry if open door policy is true
            State flag = (allow || plugin.getConfig().getBoolean("preferences.open_door_policy")) ? State.ALLOW : State.DENY;
            if (region != null) {
                Map<Flag<?>, Object> flags = region.getFlags();
                flags.put(Flags.ENTRY, flag);
                flags.put(Flags.EXIT, flag);
                region.setFlags(flags);
                try {
                    rm.save();
                } catch (StorageException e) {
                    plugin.getLogger().log(Level.INFO, "Could not update WorldGuard flags for everyone entry & exit! " + e.getMessage());
                }
            }
        }
    }
}
