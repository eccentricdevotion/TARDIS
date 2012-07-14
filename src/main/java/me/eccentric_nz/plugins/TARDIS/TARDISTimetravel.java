package me.eccentric_nz.plugins.TARDIS;

import java.util.Random;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

public class TARDISTimetravel {

    private static Location dest;
    private TARDIS plugin;

    public TARDISTimetravel(TARDIS plugin) {
        this.plugin = plugin;
    }

    public Location randomDestination(Player p, World w, byte rx, byte rz, byte ry) {
        int level, row, col, x, y, z, startx, starty, startz, resetx, resetz;
        int[] bad_blockids = {8, 9, 10, 11, 51, 81};
        Boolean danger = true;
        // there needs to be room for the TARDIS and the player!
        Random rand = new Random();
        // get max_radius from config
        int max = plugin.config.getInt("tp_radius");
        int quarter = (max + 4 - 1) / 4;
        int range = quarter + 1;
        int wherex = 0, wherey = 0, wherez = 0;

        while (danger == true) {
            wherex = rand.nextInt(range);
            wherey = rand.nextInt(range);
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
            if (ry >= 4 && ry <= 7) {
                wherey += (quarter);
            }
            if (ry >= 8 && ry <= 11) {
                wherey += (quarter * 2);
            }
            if (ry >= 12 && ry <= 15) {
                wherey += (quarter * 3);
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
            wherex = wherex * 2;
            wherez = wherez * 2;
            wherex = wherex - max;
            wherez = wherez - max;

            Block currentBlock = w.getBlockAt(wherex, wherey, wherez);
            while (currentBlock.getType() == Material.AIR || currentBlock.getType() == Material.SNOW || currentBlock.getType() == Material.LONG_GRASS || currentBlock.getType() == Material.RED_ROSE || currentBlock.getType() == Material.YELLOW_FLOWER || currentBlock.getType() == Material.BROWN_MUSHROOM || currentBlock.getType() == Material.RED_MUSHROOM && currentBlock.getType() != Material.SAPLING) {
                currentBlock = currentBlock.getRelative(BlockFace.DOWN);
            }
            Location chunk_loc = currentBlock.getLocation();
            double getY = chunk_loc.getBlockY();

            w.getChunkAt(chunk_loc).load();
            w.getChunkAt(chunk_loc).load(true);
            while (!w.getChunkAt(chunk_loc).isLoaded()) {
                w.getChunkAt(chunk_loc).load();
            }
            Constants.COMPASS d = Constants.COMPASS.valueOf(plugin.timelords.getString(p.getName() + ".direction"));
            // get start location for checking there is enough space
            int gsl[] = getStartLocation(chunk_loc, d);
            startx = gsl[0];
            resetx = gsl[1];
            starty = chunk_loc.getBlockY();
            startz = gsl[2];
            resetz = gsl[3];
            x = gsl[4];
            z = gsl[5];
            for (level = 0; level < 4; level++) {
                for (row = 0; row < 3; row++) {
                    for (col = 0; col < 5; col++) {
                        int id = w.getBlockAt(startx, starty, startz).getTypeId();
                        if (isItSafe(id)) {
                            danger = true;
                        } else {
                            danger = false;
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
            p.sendMessage("Finding safe location...");
        }
        wherey = w.getHighestBlockYAt(wherex, wherez) + 2;
        dest = new Location(w, wherex, wherey, wherez);
        return dest;
    }

    private boolean isItSafe(int id) {
        if (id == 0 || id == 80 || id == 31 || id == 38 || id == 37 || id == 39 || id == 40 || id == 6) {
            return false;
        } else {
            return true;
        }
    }
    private static int[] startLoc = new int[6];

    public int[] getStartLocation(Location loc, Constants.COMPASS dir) {
        switch (dir) {
            case NORTH:
                startLoc[0] = loc.getBlockX() + 1;
                startLoc[1] = startLoc[0];
                startLoc[2] = loc.getBlockZ() + 3;
                startLoc[3] = startLoc[2];
                startLoc[4] = -1;
                startLoc[5] = -1;
                break;
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
        }
        return startLoc;
    }
}