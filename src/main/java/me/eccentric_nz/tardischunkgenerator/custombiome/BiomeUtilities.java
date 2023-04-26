package me.eccentric_nz.tardischunkgenerator.custombiome;

import com.mojang.datafixers.util.Pair;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.biome.Biome;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_19_R3.CraftServer;
import org.bukkit.craftbukkit.v1_19_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_19_R3.util.CraftNamespacedKey;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import net.minecraft.core.Vec3i;

public class BiomeUtilities {

    public static String getLevelName() {
        try {
            BufferedReader is = new BufferedReader(new FileReader("server.properties"));
            Properties props = new Properties();
            props.load(is);
            is.close();
            return props.getProperty("level-name");
        } catch (IOException e) {
            return "world"; // minecraft / spigot default
        }
    }

    public static void addBiomes(String basePath) {
        // get the TARDIS planets config
        FileConfiguration planets = YamlConfiguration.loadConfiguration(new File(basePath + "planets.yml"));
        if (planets.getBoolean("planets.gallifrey.enabled")) {
            Bukkit.getConsoleSender().sendMessage(TardisModule.HELPER.getName() + "Adding custom biomes for planet Gallifrey...");
            CustomBiome.addCustomBiome(TARDISBiomeData.BADLANDS);
        }
        if (planets.getBoolean("planets.skaro.enabled")) {
            Bukkit.getConsoleSender().sendMessage(TardisModule.HELPER.getName() + "Adding custom biomes for planet Skaro...");
            CustomBiome.addCustomBiome(TARDISBiomeData.DESERT);
        }
    }

    public static Location searchBiome(World world, org.bukkit.block.Biome biome, Location policeBox) {
        ServerLevel worldServer = ((CraftWorld) world).getHandle();
        Registry<Biome> biomeRegistry = ((CraftServer) Bukkit.getServer()).getServer().registryAccess().registry(Registries.BIOME).get();
        Holder<Biome> biomeHolder = biomeRegistry.getHolderOrThrow(ResourceKey.create(Registries.BIOME, CraftNamespacedKey.toMinecraft(biome.getKey())));
        Vec3i vector = new Vec3i(policeBox.getBlockX(), policeBox.getBlockY(), policeBox.getBlockZ());
        BlockPos startPosition = new BlockPos(vector);
        Pair<BlockPos, Holder<Biome>> biomePosition = worldServer.findClosestBiome3d((b) -> b == biomeHolder, startPosition, 6400, 32, 64);
        if (biomePosition != null) {
            return new Location(world, biomePosition.getFirst().getX(), biomePosition.getFirst().getY(), biomePosition.getFirst().getZ());
        }
        return null;
    }
}
