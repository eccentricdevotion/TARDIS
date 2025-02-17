package me.eccentric_nz.tardischunkgenerator.custombiome;

import org.bukkit.Location;
import org.bukkit.World;
import org.terraform.biome.BiomeBank;
import org.terraform.biome.BiomeType;
import org.terraform.data.TerraformWorld;
import org.terraform.utils.GenUtils;
import org.terraform.utils.Vector2f;

import java.util.Locale;

public class TerraBiomeLocator {

    private final World world;
    private final Location current;
    private final String biome;

    public TerraBiomeLocator(World world, Location current, String biome) {
        this.world = world;
        this.current = current;
        this.biome = biome;
    }

    public Location execute() {
        try {
            TerraformWorld terraformWorld = TerraformWorld.get(world);
            BiomeBank biomeBank = BiomeBank.valueOf(biome.toUpperCase(Locale.ROOT));
            Vector2f location;
            int x = current.getBlockX();
            int z = current.getBlockZ();
            if (biomeBank.getType() == BiomeType.BEACH || biomeBank.getType() == BiomeType.RIVER) {
                location = GenUtils.locateHeightDependentBiome(terraformWorld, biomeBank, new Vector2f(x, z), 5000, 25);
            } else {
                location = GenUtils.locateHeightIndependentBiome(terraformWorld, biomeBank, new Vector2f(x, z));
            }
            if (location != null) {
                return new Location(world, location.x, world.getHighestBlockYAt((int) location.x, (int) location.y), location.y);
            }
            return null;
        } catch (IllegalArgumentException ignored) {
            return null;
        }
    }
}
