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
package me.eccentric_nz.TARDIS.utility;

import me.crafter.mc.lockettepro.LocketteProAPI;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCount;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetDiskStorage;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import me.eccentric_nz.tardischunkgenerator.TARDISChunkGenerator;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Various utility methods.
 * <p>
 * The TARDIS can be programmed to execute automatic functions based on certain conditions. It also automatically
 * repairs after too much damage.
 *
 * @author eccentric_nz
 */
public class TARDISUtils {

    private final TARDIS plugin;

    public TARDISUtils(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean compareLocations(Location a, Location b) {
        if (a.getWorld().equals(b.getWorld())) {
            double rd = plugin.getArtronConfig().getDouble("recharge_distance");
            double squared = rd * rd;
            return (a.distanceSquared(b) <= squared);
        }
        return false;
    }

    public boolean canGrowRooms(String chunk) {
        String[] data = chunk.split(":");
        World room_world = TARDISStaticLocationGetters.getWorld(chunk);
        ChunkGenerator gen = room_world.getGenerator();
        WorldType wt = room_world.getWorldType();
        String dn = "TARDIS_TimeVortex";
        if (plugin.getConfig().getBoolean("creation.default_world")) {
            dn = plugin.getConfig().getString("creation.default_world_name");
        }
        boolean special = (data[0].equalsIgnoreCase(dn) && (wt.equals(WorldType.FLAT) || gen instanceof TARDISChunkGenerator));
        return (data[0].contains("TARDIS_WORLD_") || special);
    }

    public boolean inTARDISWorld(Player player) {
        // check they are still in the TARDIS world
        World world = plugin.getServer().getWorlds().get(0);
        String name = "";
        if (player != null && player.isOnline()) {
            world = player.getLocation().getWorld();
            name = world.getName();
        }
        ChunkGenerator gen = world.getGenerator();
        // get default world name
        String dn = "TARDIS_TimeVortex";
        if (plugin.getConfig().getBoolean("creation.default_world")) {
            dn = plugin.getConfig().getString("creation.default_world_name");
        }
        boolean special = ((name.equals(dn) || name.equals("TARDIS_Zero_Room")) && (world.getWorldType().equals(WorldType.FLAT) || gen instanceof TARDISChunkGenerator));
        return name.equals("TARDIS_WORLD_" + player.getName()) || special;
    }

    public boolean inTARDISWorld(Location loc) {
        // check they are still in the TARDIS world
        World world = loc.getWorld();
        String name = world.getName();
        ChunkGenerator gen = world.getGenerator();
        // get default world name
        String dn = "TARDIS_TimeVortex";
        if (plugin.getConfig().getBoolean("creation.default_world")) {
            dn = plugin.getConfig().getString("creation.default_world_name");
        }
        boolean special = (name.equals(dn) && (world.getWorldType().equals(WorldType.FLAT) || gen instanceof TARDISChunkGenerator));
        return name.startsWith("TARDIS_WORLD_") || special;
    }

    /**
     * Checks if player has storage record, and update the tardis_id field if they do.
     *
     * @param uuid the player's UUID
     * @param id   the player's TARDIS ID
     */
    public void updateStorageId(String uuid, int id) {
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", uuid);
        ResultSetDiskStorage rss = new ResultSetDiskStorage(plugin, where);
        if (rss.resultSet()) {
            HashMap<String, Object> wherej = new HashMap<>();
            wherej.put("uuid", uuid);
            HashMap<String, Object> setj = new HashMap<>();
            setj.put("tardis_id", id);
            plugin.getQueryFactory().doUpdate("storage", setj, wherej);
        }
    }

    /**
     * Gets the chat colour to use on the Police Box sign.
     *
     * @return the configured chat colour
     */
    public ChatColor getSignColour() {
        ChatColor colour;
        String cc = plugin.getConfig().getString("police_box.sign_colour");
        try {
            colour = ChatColor.valueOf(cc);
        } catch (IllegalArgumentException e) {
            colour = ChatColor.WHITE;
        }
        return colour;
    }

    public int getHighestNetherBlock(World w, int wherex, int wherez) {
        int y = 100;
        Block startBlock = w.getBlockAt(wherex, y, wherez);
        while (!startBlock.getType().isAir()) {
            startBlock = startBlock.getRelative(BlockFace.DOWN);
        }
        int air = 0;
        while (startBlock.getType().isAir() && startBlock.getLocation().getBlockY() > 30) {
            startBlock = startBlock.getRelative(BlockFace.DOWN);
            air++;
        }
        Material mat = startBlock.getType();
        if (air >= 4 && (plugin.getGeneralKeeper().getGoodNether().contains(mat) || plugin.getPlanetsConfig().getBoolean("planets." + w.getName() + ".false_nether"))) {
            y = startBlock.getLocation().getBlockY() + 1;
        }
        return y;
    }

    public boolean inGracePeriod(Player p, boolean update) {
        boolean inGracePeriod = false;
        // check grace period
        int grace = plugin.getConfig().getInt("travel.grace_period");
        if (grace > 0) {
            ResultSetCount rsc = new ResultSetCount(plugin, p.getUniqueId().toString());
            if (rsc.resultSet()) {
                int grace_count = rsc.getGrace();
                if (grace_count < grace) {
                    inGracePeriod = true;
                    if (update) {
                        TARDISMessage.send(p, "GRACE_PERIOD", String.format("%d", (grace - (grace_count + 1))));
                        // update the grace count if the TARDIS has travelled
                        HashMap<String, Object> where = new HashMap<>();
                        where.put("uuid", p.getUniqueId().toString());
                        HashMap<String, Object> set = new HashMap<>();
                        set.put("grace", (grace_count + 1));
                        plugin.getQueryFactory().doUpdate("t_count", set, where);
                    }
                } else if (plugin.getConfig().getBoolean("allow.player_difficulty") && TARDISPermission.hasPermission(p, "tardis.difficulty")) {
                    // check player difficulty preference
                    ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, p.getUniqueId().toString());
                    if (rsp.resultSet()) {
                        inGracePeriod = rsp.isEasyDifficulty();
                    }
                }
            }
        } else if (plugin.getConfig().getBoolean("allow.player_difficulty") && TARDISPermission.hasPermission(p, "tardis.difficulty")) {
            // check player difficulty preference
            ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, p.getUniqueId().toString());
            if (rsp.resultSet()) {
                inGracePeriod = rsp.isEasyDifficulty();
            }
        }
        return inGracePeriod;
    }

    public List<Entity> getJunkTravellers(Location loc) {
        // spawn an entity
        Entity orb = loc.getWorld().spawnEntity(loc, EntityType.EXPERIENCE_ORB);
        List<Entity> ents = orb.getNearbyEntities(16.0d, 16.0d, 16.0d);
        orb.remove();
        return ents;
    }

    public boolean checkSurrounding(Block under) {
        List<BlockFace> faces = Arrays.asList(BlockFace.EAST, BlockFace.NORTH, BlockFace.WEST, BlockFace.SOUTH, BlockFace.NORTH_EAST, BlockFace.NORTH_WEST, BlockFace.SOUTH_EAST, BlockFace.SOUTH_WEST);
        for (BlockFace f : faces) {
            if (LocketteProAPI.isProtected(under.getRelative(f))) {
                return true;
            }
        }
        return false;
    }
}
