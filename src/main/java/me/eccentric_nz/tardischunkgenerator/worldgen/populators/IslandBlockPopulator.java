package me.eccentric_nz.tardischunkgenerator.worldgen.populators;

import com.mojang.datafixers.util.Pair;
import me.eccentric_nz.tardischunkgenerator.worldgen.utils.IslandSpiral;
import me.eccentric_nz.tardischunkgenerator.worldgen.utils.WaterCircle;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.LimitedRegion;
import org.bukkit.generator.WorldInfo;

import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;
import java.util.concurrent.ThreadLocalRandom;

public class IslandBlockPopulator extends BlockPopulator {

    private final WeightedChoice<Material> stones = new WeightedChoice<Material>().add(60, Material.STONE).add(10, Material.ANDESITE).add(10, Material.DIORITE).add(10, Material.GRANITE).add(10, Material.COAL_ORE);
    private final WeightedChoice<Material> dirts = new WeightedChoice<Material>().add(70, Material.DIRT).add(10, Material.GRAVEL).add(20, Material.COARSE_DIRT);
    private final WeightedChoice<Material> flora = new WeightedChoice<Material>().add(70, Material.GRASS).add(10, Material.RED_TULIP).add(10, Material.OXEYE_DAISY).add(10, Material.CORNFLOWER);

    @Override
    public void populate(WorldInfo worldInfo, Random random, int x, int z, LimitedRegion limitedRegion) {
        if ((x == 0 && z == 0) || random.nextInt(1000) < 1) {
            // make an island
            // get a spiral
            IslandSpiral spiral = new IslandSpiral();
            double[][] island = spiral.createMatrix(16, 16, random, 0.01);
            // create a blob
            boolean[][] blob = WaterCircle.makeBlob();
            // set starting values
            int starty = 95;
            int startx = x * 16;
            int startz = z * 16;
            // get tree position
            Pair<Integer, Integer> treePos = spiral.getTreePosition();
            int treeX = treePos.getFirst();
            int treeZ = treePos.getSecond();
            int treeY = 95;
            // loop through the chunk coords and set blocks
            // top layer grass
            // three layers of dirt
            // the rest stone
            for (int r = 1; r < 15; r++) {
                for (int c = 1; c < 15; c++) {
                    if (blob[r][c]) {
                        double n = island[r][c];
                        int top = (n > 0) ? (int) (n * 6) : 0;
                        if (r == treeX && c == treeZ) {
                            treeY = starty + top + 1;
                        }
                        int bottom = -6 - (int) (n * 40);
                        for (int h = top; h >= bottom; h--) {
                            int wx = startx + r;
                            int wy = starty + h;
                            int wz = startz + c;
                            if (limitedRegion.isInRegion(wx, wy, wz)) {
                                Material material;
                                if (h == top) {
                                    material = Material.GRASS_BLOCK;
                                    if (random.nextInt(10) < 3) {
                                        limitedRegion.setType(wx, wy + 1, wz, flora.next());
                                    }
                                } else if (h > top - 3) {
                                    material = dirts.next();
                                } else {
                                    material = stones.next();
                                }
                                limitedRegion.setType(wx, wy, wz, material);
                                System.out.print(".");
                            }
                        }
                    }
                }
            }
            // try to add a tree
            if (limitedRegion.isInRegion(startx + treeX, treeY, startx + treeZ)) {
                Location location = new Location(null, startx + treeX, treeY, startx + treeZ);
                // always set a dirt block under the tree location
                limitedRegion.setType(location.clone().add(0, -1, 0), Material.DIRT);
                if (!limitedRegion.generateTree(location, random, TreeType.TREE)) {
                    limitedRegion.setType(startx + treeX, treeY, startx + treeZ, Material.OAK_SAPLING);
                }
            }
        }
    }

    private static class WeightedChoice<E> {

        private final NavigableMap<Double, E> map = new TreeMap<>();
        private double total = 0;

        public WeightedChoice<E> add(double weight, E result) {
            if (weight <= 0) {
                return this;
            }
            total += weight;
            map.put(total, result);
            return this;
        }

        public E next() {
            double value = ThreadLocalRandom.current().nextDouble() * total;
            return map.higherEntry(value).getValue();
        }
    }
}
