package me.eccentric_nz.plugins.TARDIS;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

public class TARDISTimetravel {

    private static Location dest;
    private TARDIS plugin;

    public TARDISTimetravel(TARDIS plugin) {
        this.plugin = plugin;
    }

    public Location randomDestination(Player p, World w, byte rx, byte rz, byte ry, String dir) {
        int level, row, col, x, y, z, startx, starty, startz, resetx, resetz, listlen, rw;
        World randworld = w;
        Boolean danger = true;
        int count;
        // there needs to be room for the TARDIS and the player!
        Random rand = new Random();
        // get max_radius from config
        int max = plugin.config.getInt("tp_radius");
        int quarter = (max + 4 - 1) / 4;
        int range = quarter + 1;
        int wherex = 0, highest = 252, wherez = 0;
        Constants.COMPASS d = Constants.COMPASS.valueOf(dir);
        // get worlds
        Set<String> worldlist = plugin.config.getConfigurationSection("worlds").getKeys(false);
        List<World> normalWorlds = new ArrayList<World>();
        for (String o : worldlist) {
            Environment env = plugin.getServer().getWorld(o).getEnvironment();
            if (env == Environment.NORMAL || (env == Environment.THE_END && plugin.config.getBoolean("the_end") == true)) {
                if (plugin.config.getBoolean("include_default_world") == Boolean.valueOf("true")
                        || plugin.config.getBoolean("default_world") == Boolean.valueOf("false")) {
                    if (plugin.config.getBoolean("worlds." + o) == Boolean.valueOf("true")) {
                        normalWorlds.add(plugin.getServer().getWorld(o));
                    }
                } else {
                    if (!o.equals(plugin.config.getString("default_world_name"))) {
                        if (plugin.config.getBoolean("worlds." + o) == Boolean.valueOf("true")) {
                            normalWorlds.add(plugin.getServer().getWorld(o));
                        }
                    }
                }
            }
        }
        listlen = normalWorlds.size();
        // random world
        rw = rand.nextInt(listlen);
        int i = 0;
        for (World wobj : normalWorlds) {
            if (i == rw) {
                randworld = wobj;
            }
            i = i + 1;
        }
        TARDISWorldGuardChecker wgchk = null;
        if (plugin.WorldGuardOnServer) {
            wgchk = new TARDISWorldGuardChecker(plugin);
        }
        TARDISArea ta = new TARDISArea(plugin);
        if (randworld.getEnvironment().equals(Environment.THE_END)) {
            while (danger == true) {
                wherex = rand.nextInt(240);
                wherez = rand.nextInt(240);
                wherex = wherex - 120;
                wherez = wherez - 120;
                // get the spawn point
                Location endSpawn = randworld.getSpawnLocation();
                highest = randworld.getHighestBlockYAt(endSpawn.getBlockX() + wherex, endSpawn.getBlockZ() + wherez);
                if (highest > 40) {
                    Block currentBlock = randworld.getBlockAt(wherex, highest, wherez);
                    Location chunk_loc = currentBlock.getLocation();
                    if (plugin.WorldGuardOnServer && wgchk.cantBuild(p, chunk_loc) && plugin.config.getBoolean("respect_worldguard")) {
                        count = 1;
                    }
                    if (ta.areaCheckLocPlayer(p, chunk_loc)) {
                        plugin.trackPerm.remove(p.getName());
                        count = 1;
                    }
                    randworld.getChunkAt(chunk_loc).load();
                    randworld.getChunkAt(chunk_loc).load(true);
                    while (!randworld.getChunkAt(chunk_loc).isLoaded()) {
                        randworld.getChunkAt(chunk_loc).load();
                    }
                    // get start location for checking there is enough space
                    int gsl[] = getStartLocation(chunk_loc, d);
                    startx = gsl[0];
                    resetx = gsl[1];
                    starty = chunk_loc.getBlockY() + 1;
                    startz = gsl[2];
                    resetz = gsl[3];
                    x = gsl[4];
                    z = gsl[5];
                    count = safeLocation(startx, starty, startz, resetx, resetz, x, z, randworld, d);
                } else {
                    count = 1;
                }
                if (count == 0) {
                    danger = false;
                    break;
                }
            }
            dest = new Location(randworld, wherex, highest, wherez);
        } else {
            while (danger == true) {
                count = 0;
                wherex = rand.nextInt(range);
                wherez = rand.nextInt(range);
                // add the distance from the x and z repeaters
                if (rx >= 4 && rx <= 7) {
                    wherex += (quarter);
                }
                if (rx >= 8 && rx <= 11) {
                    wherex += (quarter * 2);
                }
                if (rx >= 12 && rx <= 15) {
                    wherex += (quarter * 3);
                }
                if (rz >= 4 && rz <= 7) {
                    wherez += (quarter);
                }
                if (rz >= 8 && rz <= 11) {
                    wherez += (quarter * 2);
                }
                if (rz >= 12 && rz <= 15) {
                    wherez += (quarter * 3);
                }

                // add chance of negative values
                wherex = wherex * 2;
                wherez = wherez * 2;
                wherex = wherex - max;
                wherez = wherez - max;

                // use multiplier based on position of third repeater
                if (ry >= 4 && ry <= 7) {
                    wherex = wherex * 2;
                    wherez = wherez * 2;
                }
                if (ry >= 8 && ry <= 11) {
                    wherex = wherex * 3;
                    wherez = wherez * 3;
                }
                if (ry >= 12 && ry <= 15) {
                    wherex = wherex * 4;
                    wherez = wherez * 4;
                }

                highest = randworld.getHighestBlockYAt(wherex, wherez);
                if (highest > 3) {
                    Block currentBlock = randworld.getBlockAt(wherex, highest, wherez);
                    if (currentBlock.getType() == Material.AIR || currentBlock.getType() == Material.SNOW || currentBlock.getType() == Material.LONG_GRASS || currentBlock.getType() == Material.RED_ROSE || currentBlock.getType() == Material.YELLOW_FLOWER || currentBlock.getType() == Material.BROWN_MUSHROOM || currentBlock.getType() == Material.RED_MUSHROOM || currentBlock.getType() == Material.SAPLING || currentBlock.getType() == Material.SNOW) {
                        currentBlock = currentBlock.getRelative(BlockFace.DOWN);
                    }
                    Location chunk_loc = currentBlock.getLocation();
                    if (plugin.WorldGuardOnServer && wgchk.cantBuild(p, chunk_loc) && plugin.config.getBoolean("respect_worldguard")) {
                        count = 1;
                    }
                    if (ta.areaCheckLocPlayer(p, chunk_loc)) {
                        plugin.trackPerm.remove(p.getName());
                        count = 1;
                    }
                    randworld.getChunkAt(chunk_loc).load();
                    randworld.getChunkAt(chunk_loc).load(true);
                    while (!randworld.getChunkAt(chunk_loc).isLoaded()) {
                        randworld.getChunkAt(chunk_loc).load();
                    }
                    // get start location for checking there is enough space
                    int gsl[] = getStartLocation(chunk_loc, d);
                    startx = gsl[0];
                    resetx = gsl[1];
                    starty = chunk_loc.getBlockY() + 1;
                    startz = gsl[2];
                    resetz = gsl[3];
                    x = gsl[4];
                    z = gsl[5];
                    count = safeLocation(startx, starty, startz, resetx, resetz, x, z, randworld, d);
                } else {
                    count = 1;
                }
                if (count == 0) {
                    danger = false;
                    break;
                }
            }
            dest = new Location(randworld, wherex, highest, wherez);
        }
        return dest;
    }

    public int safeLocation(int startx, int starty, int startz, int resetx, int resetz, int x, int z, World w, Constants.COMPASS d) {
        int level, row, col, rowcount, colcount, count = 0;
        switch (d) {
            case EAST:
            case WEST:
                rowcount = 5;
                colcount = 3;
                break;
            default:
                rowcount = 3;
                colcount = 5;
                break;
        }
        for (level = 0; level < 4; level++) {
            for (row = 0; row < rowcount; row++) {
                for (col = 0; col < colcount; col++) {
                    int id = w.getBlockAt(startx, starty, startz).getTypeId();
                    if (isItSafe(id)) {
                        count++;
                    }
                    switch (d) {
                        case NORTH:
                        case SOUTH:
                            startx += x;
                            break;
                        case EAST:
                        case WEST:
                            startz += z;
                            break;
                    }
                }
                switch (d) {
                    case NORTH:
                    case SOUTH:
                        startx = resetx;
                        startz += z;
                        break;
                    case EAST:
                    case WEST:
                        startz = resetz;
                        startx += x;
                        break;
                }
            }
            switch (d) {
                case NORTH:
                case SOUTH:
                    startz = resetz;
                    break;
                case EAST:
                case WEST:
                    startx = resetx;
                    break;
            }
            starty += 1;
        }
        return count;
    }

    private boolean isItSafe(int id) {
        boolean safe = true;
        if (id == 0 || id == 6 || id == 31 || id == 32 || id == 37 || id == 38 || id == 39 || id == 40 || id == 78) {
            safe = false;
        }
        return safe;
    }
    private static int[] startLoc = new int[6];

    public int[] getStartLocation(Location loc, Constants.COMPASS dir) {
        switch (dir) {
            case EAST:
                startLoc[0] = loc.getBlockX() - 3;
                startLoc[1] = startLoc[0];
                startLoc[2] = loc.getBlockZ() + 1;
                startLoc[3] = startLoc[2];
                startLoc[4] = 1;
                startLoc[5] = -1;
                break;
            case SOUTH:
                startLoc[0] = loc.getBlockX() - 1;
                startLoc[1] = startLoc[0];
                startLoc[2] = loc.getBlockZ() - 3;
                startLoc[3] = startLoc[2];
                startLoc[4] = 1;
                startLoc[5] = 1;
                break;
            case WEST:
                startLoc[0] = loc.getBlockX() + 3;
                startLoc[1] = startLoc[0];
                startLoc[2] = loc.getBlockZ() - 1;
                startLoc[3] = startLoc[2];
                startLoc[4] = -1;
                startLoc[5] = 1;
                break;
            case NORTH:
                startLoc[0] = loc.getBlockX() + 1;
                startLoc[1] = startLoc[0];
                startLoc[2] = loc.getBlockZ() + 3;
                startLoc[3] = startLoc[2];
                startLoc[4] = -1;
                startLoc[5] = -1;
                break;
        }
        return startLoc;
    }
}
