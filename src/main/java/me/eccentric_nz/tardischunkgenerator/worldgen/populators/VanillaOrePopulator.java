package me.eccentric_nz.tardischunkgenerator.worldgen.populators;

import me.eccentric_nz.tardischunkgenerator.worldgen.caves.OreConfig;
import me.eccentric_nz.tardischunkgenerator.worldgen.caves.OreDistribution;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.LimitedRegion;
import org.bukkit.generator.WorldInfo;

import java.util.List;
import java.util.Random;
import java.util.Set;

public class VanillaOrePopulator extends BlockPopulator {

    private final List<OreConfig> oreConfigs = List.of(
            // COAL: Y=1 to 192 (Peak at Y=96)
            new OreConfig(Material.COAL_ORE, Material.DEEPSLATE_COAL_ORE, 1, 192, 96, OreDistribution.TRIANGLE, 20, 17, null),
            // IRON: Y=1 to 72 (Peak at Y=16)
            new OreConfig(Material.IRON_ORE, Material.DEEPSLATE_IRON_ORE, 1, 72, 16, OreDistribution.TRIANGLE, 10, 9, null),
            // COPPER: Y=1 to 112 (Peak at Y=48)
            new OreConfig(Material.COPPER_ORE, Material.DEEPSLATE_COPPER_ORE, 1, 112, 48, OreDistribution.TRIANGLE, 14, 10, null),
            // GOLD: Y=1 to 32 (Peak at Y=-16)
            new OreConfig(Material.GOLD_ORE, Material.DEEPSLATE_GOLD_ORE, 1, 32, 16, OreDistribution.TRIANGLE, 4, 9, null),
            // GOLD (Badlands extra generation): Y=1 to 128
            new OreConfig(Material.GOLD_ORE, Material.DEEPSLATE_GOLD_ORE, 1, 128, 96, OreDistribution.UNIFORM, 15, 9, Set.of("badlands")),
            // REDSTONE: Y=-64 to 15 (Increases down to -64)
            new OreConfig(Material.REDSTONE_ORE, Material.DEEPSLATE_REDSTONE_ORE, 1, 15, 8, OreDistribution.TRIANGLE, 8, 8, null),
            // LAPIS_LAZULI: Y=-32 to 32 (Peak at Y=0)
            new OreConfig(Material.LAPIS_ORE, Material.DEEPSLATE_LAPIS_ORE, 1, 32, 12, OreDistribution.TRIANGLE, 4, 7, null),
            // DIAMOND: Y=-64 to 16 (Increases down to -64)
            new OreConfig(Material.DIAMOND_ORE, Material.DEEPSLATE_DIAMOND_ORE, 1, 16, 16, OreDistribution.TRIANGLE, 7, 5, null),
            // EMERALD: Y=-16 to 320 (Peak Y=232, Mountain Biomes only)
            new OreConfig(Material.EMERALD_ORE, Material.DEEPSLATE_EMERALD_ORE, 1, 128, 64, OreDistribution.TRIANGLE, 6, 3, Set.of("peaks", "mountain", "slope", "meadow"))
    );

    @Override
    public void populate(WorldInfo worldInfo, Random random, int chunkX, int chunkZ, LimitedRegion region) {
        int startX = chunkX << 4;
        int startZ = chunkZ << 4;
        for (OreConfig config : oreConfigs) {
            for (int i = 0; i < config.veinsPerChunk(); i++) {
                int x = startX + random.nextInt(16);
                int z = startZ + random.nextInt(16);
                int y = calculateY(random, config);
                // apply biome constraints if set
                if (config.biomeFilter() != null && !config.biomeFilter().isEmpty()) {
                    Biome biome = region.getBiome(x, y, z);
                    String biomeKey = biome.getKey().getKey().toLowerCase();
                    boolean matches = config.biomeFilter().stream().anyMatch(biomeKey::contains);
                    if (!matches) continue;
                }
                generateVein(region, random, x, y, z, config);
            }
        }
    }

    private int calculateY(Random random, OreConfig config) {
        if (config.oreDistribution() == OreDistribution.UNIFORM) {
            return config.minHeight() + random.nextInt(config.maxHeight() - config.minHeight() + 1);
        }
        // triangular distribution around peakHeight
        double r1 = random.nextDouble();
        double centerNormalized = (double) (config.peakHeight() - config.minHeight()) / (config.maxHeight() - config.minHeight());
        double sampledNormalized = (r1 < centerNormalized)
                ? Math.sqrt(r1 * centerNormalized)
                : 1.0 - Math.sqrt((1.0 - r1) * (1.0 - centerNormalized));
        return (int) Math.round(config.minHeight() + sampledNormalized * (config.maxHeight() - config.minHeight()));
    }

    private void generateVein(LimitedRegion region, Random random, int startX, int startY, int startZ, OreConfig config) {
        int cx = startX;
        int cy = startY;
        int cz = startZ;
        // random walk cluster to form cohesive veins
        for (int i = 0; i < config.veinSize(); i++) {
            if (region.isInRegion(cx, cy, cz)) {
                Material target = region.getType(cx, cy, cz);
                if (isReplaceableStone(target)) {
                    region.setType(cx, cy, cz, config.stoneOre());
                } else if (target == Material.DEEPSLATE) {
                    region.setType(cx, cy, cz, config.deepslateOre());
                }
            }
            // step adjacent
            cx += random.nextInt(3) - 1;
            cy += random.nextInt(3) - 1;
            cz += random.nextInt(3) - 1;
        }
    }

    private boolean isReplaceableStone(Material mat) {
        return mat == Material.ANDESITE || mat == Material.CALCITE || mat == Material.DEAD_BRAIN_CORAL_BLOCK
                || mat == Material.DEEPSLATE || mat == Material.DIORITE || mat == Material.DIRT
                || mat == Material.DRIPSTONE_BLOCK || mat == Material.GRANITE || mat == Material.MOSSY_COBBLESTONE
                || mat == Material.MUD || mat == Material.MYCELIUM || mat == Material.PRISMARINE
                || mat == Material.SANDSTONE || mat == Material.SNOW_BLOCK || mat == Material.STONE
                || mat == Material.SULFUR || mat == Material.TERRACOTTA || mat == Material.TUFF;
    }
}