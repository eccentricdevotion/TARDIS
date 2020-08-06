package me.eccentric_nz.TARDIS.builders;

import me.eccentric_nz.TARDIS.TARDISConstants;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FractalFence {

    public static void grow(Block start, int i) {
        Material material = start.getType();
        for (Location location : getPoints(start.getLocation(), i)) {
            Block block = location.getBlock();
            if (block.getType().isAir()) {
                block.setType(material, true);
            }
        }
    }

    private static Set<Location> getPoints(Location location, int i) {
        List<Vector> points = new ArrayList<>();
        points.add(location.toVector());
        Set<Location> adjacent = new HashSet<>();
        World world = location.getWorld();
        int minx = location.getBlockX();
        int miny = location.getBlockY() + 1;
        int minz = location.getBlockZ();
        switch (i) {
            case 1:
                minx -= 4;
                minz -= 4;
                break;
            case 2:
                minx -= 4;
                minz -= 1;
                break;
            case 3:
                minx -= 1;
                minz -= 4;
                break;
            default: // 0
                minx -= 1;
                minz -= 1;
                break;
        }
        int n = 0;
        while (n < 60) {
            int rx = minx + TARDISConstants.RANDOM.nextInt(6);
            int ry = miny + TARDISConstants.RANDOM.nextInt(5);
            int rz = minz + TARDISConstants.RANDOM.nextInt(6);
            points.add(new Vector(rx, ry, rz));
            n++;
        }
        for (int j = 0; j < points.size() - 1; j++) {
            for (int k = 1; k < points.size(); k++) {
                Vector a = points.get(j);
                Vector b = points.get(k);
                int diff = Math.abs(a.getBlockX() - b.getBlockX()) + Math.abs(a.getBlockY() - b.getBlockY()) + Math.abs(a.getBlockZ() - b.getBlockZ());
                if (diff == 1) {
                    adjacent.add(new Location(world, a.getBlockX(), a.getBlockY(), a.getBlockZ()));
                }
            }
        }
        return adjacent;
    }
}
